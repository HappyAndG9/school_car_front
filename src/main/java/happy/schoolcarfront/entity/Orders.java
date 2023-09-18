package happy.schoolcarfront.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * @since 2023-03-25
 */
@Getter
@Setter
@TableName("orders")
@ApiModel(value = "Order对象")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单号")
    @TableId(value = "order_id", type = IdType.AUTO)
    private Long orderId;

    @ApiModelProperty("用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("用户昵称")
    @TableField("user_nick_name")
    private String userNickName;

    @ApiModelProperty("所用充电桩")
    @TableField("serial_number")
    private String serialNumber;

    @ApiModelProperty("订单号")
    @TableField("order_number")
    private String orderNumber;

    @ApiModelProperty("开始时间")
    @TableField(value = "start_time", fill = FieldFill.INSERT)
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("服务时长")
    @TableField("duration")
    private String duration;

    @ApiModelProperty("费用")
    @TableField("amount")
    private BigDecimal amount;

    @ApiModelProperty("订单评论")
    @TableField("comment")
    private String comment;

    @ApiModelProperty("订单状态")
    @TableField("state")
    private Long state;

    @ApiModelProperty("逻辑删除字段（默认不被删除）")
    @TableLogic(value = "0",delval = "1")
    @TableField("is_delete")
    private Integer isDelete;


    public Orders(Long userId, String userNickName, String serialNumber, String orderNumber) {
        this.userId = userId;
        this.userNickName = userNickName;
        this.serialNumber = serialNumber;
        this.orderNumber = orderNumber;
    }
}
