package happy.schoolcarfront.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@TableName("feedback")
@ApiModel(value = "Feedback对象")
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("反馈id")
    @TableId(value = "feedback_id", type = IdType.AUTO)
    private Long feedbackId;

    @ApiModelProperty("反馈用户")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("联系电话")
    @TableField("phone_number")
    private String phoneNumber;

    @ApiModelProperty("反馈时间")
    @TableField(value = "feedback_time", fill = FieldFill.INSERT)
    private LocalDateTime feedbackTime;

    @ApiModelProperty("反馈内容")
    @TableField("feedback_content")
    private String feedbackContent;

    @ApiModelProperty("反馈对象")
    @TableField("serial_number")
    private String serialNumber;

    @ApiModelProperty("管理员的回复")
    @TableField("reply")
    private String reply;

    @ApiModelProperty("处理状态(默认未处理)")
    @TableField("state")
    private Long state;

    @ApiModelProperty("逻辑删除字段（默认不被删除）")
    @TableLogic(value = "0",delval = "1")
    @TableField("is_delete")
    private Integer isDelete;


    public Feedback(Long userId, String feedbackContent, String serialNumber) {
        this.userId = userId;
        this.feedbackContent = feedbackContent;
        this.serialNumber = serialNumber;
    }

    public Feedback(Long userId, String phoneNumber, String feedbackContent, String serialNumber) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.feedbackContent = feedbackContent;
        this.serialNumber = serialNumber;
    }


}
