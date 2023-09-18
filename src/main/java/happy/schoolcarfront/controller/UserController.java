package happy.schoolcarfront.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import happy.schoolcarfront.Result.Result;
import happy.schoolcarfront.entity.User;
import happy.schoolcarfront.mapper.UserMapper;
import happy.schoolcarfront.vo.UserVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 木月丶
 * @since 2023-03-16
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserMapper mapper;

    @GetMapping("/userInfo")
    @ApiOperation("查看个人基本信息")
    public Result userInfo(String openId){
        if (openId == null){
            return Result.error("暂无信息，请先登录");
        }
        UserVo userInfo = mapper.queryBasicInfo(openId);
        return Result.successWithData(userInfo);

    }
    @PutMapping("/bindCarInfo")
    @ApiOperation("/绑定车辆信息")
    public Result bindCarInfo(String openId,String name,String phoneNumber,String licenseNumber,String studentNumber) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("open_id", openId);
        User user = mapper.selectOne(wrapper);

        user.setPhoneNumber(phoneNumber);
        user.setLicenseNumber(licenseNumber);
        user.setName(name);
        user.setStudentNumber(studentNumber);

        return mapper.updateById(user) != 0 ? Result.success("操作成功！") : Result.error("出错啦,请稍候重试！");
    }

    @PostMapping("/closeAnAccount")
    @ApiOperation("注销账号")
    public Result closeAnAccount(Long userId){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        int delete = mapper.delete(wrapper);
        return delete != 0 ? Result.success("操作成功！") : Result.error("出错啦,请稍候重试！");
    }


    //用户登录
    @GetMapping("/firstLogin")
    @ApiOperation("用户首次登录记录其openId和昵称")
    public Result firstLogin(String openId,String userNickName){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("open_id", openId);
        User user = mapper.selectOne(userQueryWrapper);
        if (user != null){
            return Result.success("该用户已存在");
        }
        User firstLoginUser = new User(openId, userNickName);
        firstLoginUser.setUserOrderQuantity(0);
        int insert = mapper.insert(firstLoginUser);
        return insert != 0 ? Result.success() : Result.error();
    }
}
