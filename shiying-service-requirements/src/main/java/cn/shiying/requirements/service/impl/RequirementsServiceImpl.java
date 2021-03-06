package cn.shiying.requirements.service.impl;

import cn.shiying.common.dto.Result;
import cn.shiying.requirements.entity.Requirements;
import cn.shiying.requirements.entity.TestSynthesizeAll;
import cn.shiying.requirements.entity.Vo.Requirements_Vo;
import cn.shiying.requirements.mapper.RequirementsMapper;
import cn.shiying.requirements.service.RequirementsService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.shiying.common.utils.Query;
import cn.shiying.common.utils.PageUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tyb
 * @since 2020-05-02
 */
@Service
public class RequirementsServiceImpl extends ServiceImpl<RequirementsMapper, Requirements> implements RequirementsService {

    /**
     * 分页查询
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page page=new Query<Requirements_Vo>(params).getPage();
        List<Requirements_Vo> list = baseMapper.All(page,params);
        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    public void updatestate(Integer[] id) {
        for (Integer integer : id) {
            baseMapper.updatestate(integer);
        }
    }

    @Override
    public List<TestSynthesizeAll> TestSynthesizeAll(String id) {
        return baseMapper.TestSynthesizeAll(id);
    }

    @Override
    public List<TestSynthesizeAll> topFive(Integer uid) {
        return baseMapper.topFive(uid);
    }

}
