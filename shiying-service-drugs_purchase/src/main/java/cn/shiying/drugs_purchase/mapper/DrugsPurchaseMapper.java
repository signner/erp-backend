package cn.shiying.drugs_purchase.mapper;

import cn.shiying.common.entity.Drugs.DrugsDetailed;
import cn.shiying.common.entity.Drugs.DrugsPurchaseDetailed;
import cn.shiying.common.entity.supplier.SupplierDetailed;
import cn.shiying.drugs_purchase.entity.DrugsPurchase;
import cn.shiying.drugs_purchase.entity.form.DrugsAndDetailed;
import cn.shiying.drugs_purchase.entity.vo.DrugsPurchaseDetailedVO;
import cn.shiying.drugs_purchase.entity.vo.PurchaseSupplierVo;
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
 * @since 2020-04-22
 */
@Mapper
public interface DrugsPurchaseMapper extends BaseMapper<DrugsPurchase> {
    List<DrugsPurchaseDetailedVO> selectBypid(String pid);
    List<SupplierDetailed> selectSname();
    List<DrugsDetailed> selectDname();
    int insertPurchase(DrugsPurchase drugsPurchase);
    int insertDetailed(DrugsPurchaseDetailed drugsPurchaseDetailed);

    //添加药品购入详细表
    void addDrugsPurchaseDetailed(List<DrugsPurchaseDetailed> drugsPurchaseDetailed);

    List<PurchaseSupplierVo> DrugsPurchaseList(Page<DrugsPurchase> page, @Param("params") Map<String, Object> params, @Param("ids") List<String> ids);

    //根据单号查询详细表
    List<DrugsPurchaseDetailed> getByDrugsId(@Param("purchaseId") String id);

    //根据单号修改进货表
    void updateDrugs(DrugsPurchase drugsPurchase);

    //根据单号删除进货详细表
    void delDrugs(String purchaseId);
}
