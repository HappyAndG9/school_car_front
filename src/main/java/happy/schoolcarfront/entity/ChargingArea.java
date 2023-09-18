package happy.schoolcarfront.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
 * @since 2023-03-25
 */
@Getter
@Setter
@TableName("charging_area")
@ApiModel(value = "ChargingPileArea对象")
public class ChargingArea implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("充电区域id")
    @TableId(value = "area_id", type = IdType.AUTO)
    private Long areaId;

    @ApiModelProperty("位置描述")
    @TableField("description")
    private String description;

    @ApiModelProperty("坐标数据")
    @TableField("coordinate")
    private String coordinate;


}
