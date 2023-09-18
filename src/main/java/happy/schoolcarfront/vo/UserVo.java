package happy.schoolcarfront.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author 木月丶
 * @Description
 */
@Data
public class UserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户昵称")
    private String userNickName;

    @ApiModelProperty("电话号码")
    private String phoneNumber;

    @ApiModelProperty("学号")
    private String studentNumber;

    @ApiModelProperty("车牌号")
    private String licenseNumber;

    @ApiModelProperty("充电订单总数量")
    private Integer quantity;

}