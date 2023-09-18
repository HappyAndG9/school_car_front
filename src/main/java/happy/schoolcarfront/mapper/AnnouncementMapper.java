package happy.schoolcarfront.mapper;

import happy.schoolcarfront.entity.Announcement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import happy.schoolcarfront.vo.AnnouncementVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 木月丶
 * @since 2023-03-16
 */
@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {

    @Select("SELECT * FROM `announcement`\n" +
            "WHERE publish_time = (SELECT MAX(publish_time) FROM `announcement`)")
    AnnouncementVo getTheLatestAnnouncement();


}
