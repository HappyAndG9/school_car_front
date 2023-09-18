package happy.schoolcarfront.mapper;

import happy.schoolcarfront.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import happy.schoolcarfront.vo.OrdersVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
public interface OrdersMapper extends BaseMapper<Orders> {
    @Select("SELECT order_number,serial_number,start_time,end_time,duration,amount,comment,state " +
            "FROM orders o " +
            "LEFT JOIN `user` u " +
            "ON o.`user_id` = u.`user_id` " +
            "WHERE u.`open_id` = #{openId} " +
            "AND o.is_delete = 0 " +
            "ORDER BY o.end_time DESC")
    List<OrdersVo> getOrdersList(@Param("openId") String openId);


}
