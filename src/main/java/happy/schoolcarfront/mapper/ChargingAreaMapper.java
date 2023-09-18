package happy.schoolcarfront.mapper;

import happy.schoolcarfront.entity.ChargingArea;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 木月丶
 * @since 2023-03-25
 */
@Mapper
public interface ChargingAreaMapper extends BaseMapper<ChargingArea> {
    //获取指定充电区域的空闲充电桩数量
    @Select("SELECT COUNT(*) " +
            "FROM charging_area ca LEFT JOIN charging_pile cp " +
            "ON ca.area_id = cp.location_id " +
            "WHERE ca.area_id = #{locationId} AND cp.current_state = 0")
    int getVacancyChargingPileCount(@Param("locationId") Long locationId);

    @Select("SELECT area_id,\n" +
            "(SELECT COUNT(*) FROM `charging_pile` cp WHERE cp.location_id = ca.`area_id` AND cp.current_state = 0) AS vacancy,\n" +
            "(SELECT COUNT(*) FROM `charging_pile` cp WHERE cp.location_id = ca.`area_id`) AS `count`\n" +
            "FROM `charging_area` ca")
    List<HashMap<String,Integer>> getVacancyAndTotal();
}
