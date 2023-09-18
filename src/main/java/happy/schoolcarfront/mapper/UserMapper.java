package happy.schoolcarfront.mapper;

import happy.schoolcarfront.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import happy.schoolcarfront.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT u.user_nick_name, phone_number, student_number, license_number, COUNT(o.order_id) AS quantity " +
            "FROM `user` u " +
            "LEFT JOIN orders o ON u.user_id = o.user_id " +
            "WHERE u.open_id = #{openId} " +
            "GROUP BY u.user_nick_name, phone_number, student_number, license_number")
    UserVo queryBasicInfo(@Param("openId") String openId);
}
