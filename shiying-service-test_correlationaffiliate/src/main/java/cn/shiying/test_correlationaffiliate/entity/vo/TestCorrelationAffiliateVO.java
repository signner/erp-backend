package cn.shiying.test_correlationaffiliate.entity.vo;


import cn.shiying.test_correlationaffiliate.entity.TestCorrelation;
import lombok.Data;

@Data
public class TestCorrelationAffiliateVO extends TestCorrelation {
//    父级化验项目的名称
    private String  testSynthesizeName;
//      子项目化验的名称
    private  String testName;
}
