package happy.schoolcarfront.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 木月丶
 * @since 2023-03-16
 */
@Getter
@Setter
@TableName("charging_pile")
@ApiModel(value = "ChargingPile对象")
public class ChargingPile implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("充电桩id")
    @TableId(value = "charging_pile_id", type = IdType.AUTO)
    private Long chargingPileId;

    @ApiModelProperty("充电桩序列号")
    @TableField("serial_number")
    private String serialNumber;

    @ApiModelProperty("建设时间")
    @TableField("build_time")
    private LocalDateTime buildTime;

    @ApiModelProperty("所处位置区域id")
    @TableField("location_id")
    private Long locationId;

    @ApiModelProperty("当前使用状态,默认为0，0为无人使用，1为正在使用")
    @TableField("current_state")
    private Integer currentState;

    @ApiModelProperty("设备服务状态,默认为1，0为正在维修，1为可正常使用")
    @TableField("service_state")
    private Integer serviceState;


}
