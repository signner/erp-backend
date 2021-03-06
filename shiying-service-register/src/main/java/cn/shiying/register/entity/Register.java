package cn.shiying.register.entity;

import java.math.BigDecimal;

import cn.shiying.common.entity.patient.PatientDetailed;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2020-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Register implements Serializable {

    private static final long serialVersionUID = 1L;

    private String registerId;

    private Integer patientId;

    private Integer status;

    private double registerCost;

    private Integer departmentId;

    private String processInstanceId;

}
