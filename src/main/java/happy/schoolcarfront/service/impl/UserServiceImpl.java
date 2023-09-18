package happy.schoolcarfront.service.impl;

import happy.schoolcarfront.entity.User;
import happy.schoolcarfront.mapper.UserMapper;
import happy.schoolcarfront.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 木月丶
 * @since 2023-03-16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
