package cn.shiying.test_correlationaffiliate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author tyb
 * @since 2020-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TestCorrelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 综合（父级）化验项目名
     */
    private Integer testSynthesizeId;

    /**
     * 父级化验项目下的化验内容
     */
    private Integer testProjectsId;

    /**
     * 下限
     */
    private Double floor;

    /**
     * 上限
     */
    private Double ceiling;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 创建时间
     */
    private LocalDateTime createtime;

    /**
     * 创建者
     */
    private Integer uid;


}
