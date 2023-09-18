package happy.schoolcarfront.entity;

import com.baomidou.mybatisplus.annotation.*;

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
 * @since 2023-03-25
 */
@Getter
@Setter
@TableName("suggest")
@ApiModel(value = "Suggest对象")
public class Suggest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("建议id")
    @TableId(value = "suggest_id", type = IdType.AUTO)
    private Long suggestId;

    @ApiModelProperty("建议用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("建议时间")
    @TableField(value = "suggest_time", fill = FieldFill.INSERT)
    private LocalDateTime suggestTime;

    @ApiModelProperty("建议内容")
    @TableField("suggest_content")
    private String suggestContent;

    @ApiModelProperty("逻辑删除字段（默认不被删除）")
    @TableLogic(value = "0",delval = "1")
    @TableField("is_delete")
    private Integer isDelete;

    public Suggest(Long userId, String suggestContent) {
        this.userId = userId;
        this.suggestContent = suggestContent;
    }
}
