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
 * @since 2023-03-16
 */
@Getter
@Setter
@TableName("announcement")
@ApiModel(value = "Announcement对象")
public class Announcement implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("公告id")
    @TableId(value = "announcement_id", type = IdType.AUTO)
    private Long announcementId;

    @ApiModelProperty("发布者")
    @TableField("publisher_id")
    private String publisherId;

    @ApiModelProperty("公告标题")
    @TableField("publish_title")
    private String announcementTitle;

    @ApiModelProperty("公告分类")
    @TableField("publish_classification")
    private String announcementClassification;

    @ApiModelProperty("发布时间")
    @TableField(value = "publish_time", fill = FieldFill.INSERT)
    private LocalDateTime publishTime;

    @ApiModelProperty("公告内容")
    @TableField("publish_content")
    private String publishContent;


}
