package happy.schoolcarfront.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import happy.schoolcarfront.Result.Result;
import happy.schoolcarfront.entity.Feedback;
import happy.schoolcarfront.entity.User;
import happy.schoolcarfront.mapper.FeedbackMapper;
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
@RequestMapping("/feedback")
public class FeedbackController {
    @Resource
    FeedbackMapper feedbackMapper;

    @Resource
    UserMapper userMapper;




    @PostMapping("/giveFeedback")
    @ApiOperation("提出反馈")
    public Result giveFeedback(@RequestParam(required = false) String phoneNumber,
                               String openId,
                               String feedbackContent,
                               String serialNumber){

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("open_id", openId);
        User user = userMapper.selectOne(wrapper);

        Feedback feedback;

        if (phoneNumber == null){
             feedback = new Feedback(user.getUserId(), feedbackContent, serialNumber);
        }else {
             feedback = new Feedback(user.getUserId(), phoneNumber,feedbackContent, serialNumber);
        }

        int insert = feedbackMapper.insert(feedback);

        return insert != 0 ? Result.success("提交成功") : Result.error("提交失败");


    }

    @GetMapping("/queryMyFeedbacks")
    @ApiOperation("查看我全部未删除的反馈")
    public Result queryMyFeedbacks(String openId ){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("open_id", openId);
        User user = userMapper.selectOne(userQueryWrapper);

        QueryWrapper<Feedback> feedbackQueryWrapper = new QueryWrapper<>();
        feedbackQueryWrapper.eq("user_id", user.getUserId());
        feedbackQueryWrapper.orderByDesc("feedback_time");
        List<Feedback> feedbacks = feedbackMapper.selectList(feedbackQueryWrapper);
        if (feedbacks == null || feedbacks.size() == 0){
            return Result.error("暂无反馈");
        }
        return Result.successWithData(feedbacks);
    }


    @DeleteMapping("/deleteFeedback")
    @ApiOperation("删除我的一条反馈（逻辑删除）")
    public Result deleteMyFeedback(Long feedbackId){
        QueryWrapper<Feedback> wrapper = new QueryWrapper<>();
        wrapper.eq("feedback_id", feedbackId);
        int delete = feedbackMapper.delete(wrapper);
        return delete != 0 ? Result.success("删除成功") : Result.error("删除失败，请重试！");
    }

}
