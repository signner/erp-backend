package cn.shiying.ucenter.service.impl;

import cn.shiying.common.constants.SysConstants;
import cn.shiying.common.entity.sys.SysMenu;
import cn.shiying.common.entity.sys.SysRole;
import cn.shiying.common.entity.sys.SysUser;
import cn.shiying.common.enums.ErrorEnum;
import cn.shiying.common.exception.ExceptionCast;
import cn.shiying.common.utils.PageUtils;
import cn.shiying.common.utils.Query;
import cn.shiying.ucenter.mapper.SysMenuMapper;
import cn.shiying.ucenter.mapper.SysUserMapper;
import cn.shiying.ucenter.service.SysRoleService;
import cn.shiying.ucenter.service.SysUserRoleService;
import cn.shiying.ucenter.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bobbi
 * @since 2018-10-08
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    @Lazy
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuMapper sysMenuMapper;


    /**
     * 查询用户菜单
     *
     * @param userId
     * @return
     */
    @Override
    public List<Integer> queryAllMenuId(Integer userId) {
        return baseMapper.queryAllMenuId(userId);
    }

    /**
     * 根据用户名查询
     *
     * @param username
     * @return
     */
    @Override
    public SysUser selectByUsername(String username) {
        SysUser user=baseMapper.selectOne(new QueryWrapper<SysUser>().eq("username",username));
        if (user==null) return null;
        if (SysConstants.SUPER_ADMIN.equals(user.getUserId())){
            List<SysMenu> menuList=sysMenuMapper.selectList(null);
            List<String> permsList=new ArrayList<>(menuList.size());
            menuList.forEach(menu ->  permsList.add(menu.getPerms()));
            List<SysRole> roles=sysRoleService.list();
            List<Integer> roleList=new ArrayList<>();
            roles.forEach(sysRole -> roleList.add(sysRole.getRoleId()));
            user.setRoleIdList(roleList);
            user.setPerms(permsList);
        }else {
            user.setPerms(baseMapper.queryAllPerms(user.getUserId()));
            user.setRoleIdList(sysUserRoleService.queryRoleIdList(user.getUserId()));
        }
        return user;
    }

    /**
     * 分页查询用户信息
     *
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String username = (String)params.get("username");
        Integer createUserId = (Integer)params.get("createUserId");
        IPage<SysUser> page = baseMapper.selectPage(
                new Query<SysUser>(params).getPage(),
                new QueryWrapper<SysUser>().lambda()
                        .like(StringUtils.isNotBlank(username),SysUser::getUsername, username)
                        .eq(createUserId != null,SysUser::getCreateUserId, createUserId));
        return new PageUtils(page);
    }

    /**
     * 更新密码
     *
     * @param userId
     * @param password
     * @param newPassword
     * @return
     */
    @Override
    public boolean updatePassword(Integer userId, String password, String newPassword) {
        SysUser sysUser=new SysUser();
        sysUser.setPassword(newPassword);
        return this.update(sysUser, new UpdateWrapper<SysUser>().lambda()
                .eq(SysUser::getUserId,userId).eq(SysUser::getPassword,password));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(SysUser user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        this.baseMapper.insert(user);
        checkRole(user);
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(SysUser user) {
        if(StringUtils.isBlank(user.getPassword())){
            user.setPassword(null);
        }else{
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        this.baseMapper.updateById(user);

        //检查角色是否越权
        checkRole(user);

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Integer[] userIds) {
        this.removeByIds(Arrays.asList(userIds));
        //删除用户与角色关联
        sysUserRoleService.deleteBatchByUserId(userIds);

    }

    /**
     * 检查角色是否越权
     */
    private void checkRole(SysUser user){
        if(user.getRoleIdList() == null || user.getRoleIdList().size() == 0){
            return;
        }
        //如果不是超级管理员，则需要判断用户的角色是否自己创建
        if(SysConstants.SUPER_ADMIN.equals(user.getCreateUserId())){
            return ;
        }

        //查询用户创建的角色列表
        List<Integer> roleIdList = sysRoleService.queryRoleIdList(user.getCreateUserId());

        //判断是否越权
        if(!roleIdList.containsAll(user.getRoleIdList())){
            ExceptionCast.cast(ErrorEnum.NEED_MORE_AUTH);
        }
    }

}
