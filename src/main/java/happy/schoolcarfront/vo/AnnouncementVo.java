package happy.schoolcarfront.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author 木月丶
 * @Description
 */
@Data
public class AnnouncementVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("发布者")
    private String publisherId;

    @ApiModelProperty("公告标题")
    private String announcementTitle;

    @ApiModelProperty("公告分类")
    private String announcementClassification;

    @ApiModelProperty("发布时间")
    private LocalDateTime publishTime;

    @ApiModelProperty("公告内容")
    private String publishContent;
}
