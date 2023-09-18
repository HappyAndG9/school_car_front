package happy.schoolcarfront.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author 木月丶
 * @Description
 */
@Data
public class OrdersVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单号")
    private String orderNumber;

    @ApiModelProperty("所用充电桩")
    private String serialNumber;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("服务时长")
    private String duration;

    @ApiModelProperty("费用")
    private BigDecimal amount;

    @ApiModelProperty("订单评论")
    private String comment;

    @ApiModelProperty("订单状态")
    private Long state;

}
