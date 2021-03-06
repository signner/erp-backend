package cn.shiying.electronic_case.mapper;

import cn.shiying.common.entity.electronicCaseTemplate.ElectronicCaseTemplate;
import cn.shiying.electronic_case.entity.ElectronicCase;
import cn.shiying.electronic_case.entity.vo.ElectronicCaseVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tyb
 * @since 2020-04-17
 */
@Mapper
public interface ElectronicCaseMapper extends BaseMapper<ElectronicCase> {

    List<ElectronicCaseVO> listElectronicCaseVO(Page<ElectronicCaseVO> page, @Param("params") Map<String, Object> params);
    List<Integer> getIcd();

    void deleteByid(@Param("id") String id);
}
