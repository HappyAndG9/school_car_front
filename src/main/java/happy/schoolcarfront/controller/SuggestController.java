package happy.schoolcarfront.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import happy.schoolcarfront.Result.Result;
import happy.schoolcarfront.entity.Suggest;
import happy.schoolcarfront.entity.User;
import happy.schoolcarfront.mapper.SuggestMapper;
import happy.schoolcarfront.mapper.UserMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 木月丶
 * @since 2023-03-16
 */
@RestController
@RequestMapping("/suggest")
public class SuggestController {

    @Resource
    SuggestMapper mapper;

    @Resource
    UserMapper userMapper;

    @PostMapping("giveSuggest")
    @ApiOperation("提出建议")
    public Result giveSuggest(String openId,String suggestContent){

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("open_id", openId);
        User user = userMapper.selectOne(wrapper);

        Suggest suggest = new Suggest(user.getUserId(), suggestContent);

        int insert = mapper.insert(suggest);

        return insert != 0 ? Result.success("提交成功，感谢您的宝贵建议！") : Result.error("出错啦，请稍后重试！");

    }


    //查看建议
    @GetMapping("/queryMySuggests")
    @ApiOperation("查看我全部未删除的建议")
    public Result queryMySuggests(String openId) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("open_id", openId);
        User user = userMapper.selectOne(userQueryWrapper);

        QueryWrapper<Suggest> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", user.getUserId());
        wrapper.orderByDesc("suggest_time");
        List<Suggest> suggests = mapper.selectList(wrapper);
        if (suggests == null || suggests.size() == 0){
            return Result.error("暂无提交过的建议！");
        }
        return Result.successWithData(suggests);
    }


    //删除建议（逻辑删除）
    @DeleteMapping("/deleteMySuggest")
    @ApiOperation("删除我的一条建议")
    public Result deleteMySuggest(Long suggestId){
        QueryWrapper<Suggest> wrapper = new QueryWrapper<>();
        wrapper.eq("suggest_id", suggestId);
        int delete = mapper.delete(wrapper);
        return delete != 0 ? Result.success("删除成功！") : Result.error("出错啦，请稍候重试！");
    }

}
