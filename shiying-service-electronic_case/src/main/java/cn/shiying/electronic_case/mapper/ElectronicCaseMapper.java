package cn.shiying.electronic_case.mapper;

import cn.shiying.electronic_case.entity.ElectronicCase;
import cn.shiying.electronic_case.entity.vo.ElectronicCaseVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    List<ElectronicCaseVO> listElectronicCaseVO();

}