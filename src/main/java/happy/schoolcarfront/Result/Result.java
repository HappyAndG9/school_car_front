package happy.schoolcarfront.Result;

import lombok.Data;


import java.util.Map;

/**
 * @Author 木月丶
 * @Description 统一返回类
 */
@Data

public class Result<T> {
    private int code;
    private String msg;
    private Map resultMap;
    private T data;

    public Result(int code, Map resultMap) {
        this.code = code;
        this.resultMap = resultMap;
    }

    public Result(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * @author 木月丶
     * @description 请求成功
     * @return Result
     */
    public static Result success(){
        return new Result(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMsg());
    }

    /**
     * @author 木月丶
     * @description 请求成功并携带多条数据返回
     * @return Result
     */
    public static Result successWithMap(Map resultMap){
        return new Result(ResultCode.SUCCESS.getCode(),resultMap);
    }

    /**
     * @author 木月丶
     * @description 请求成功并携带一条数据返回
     * @return Result
     */
    public static Result successWithData(Object data){
        return new Result(ResultCode.SUCCESS.getCode(),data);
    }

    /**
     * @author 木月丶
     * @description 自定义返回信息的成功请求
     * @return Result
     */
    public static Result success(String msg) {
        return new Result(ResultCode.SUCCESS.getCode(), msg);
    }

    /**
     * @author 木月丶
     * @description 默认的错误返回
     * @return Result
     */
    public static Result error(){
        return new Result(ResultCode.ERROR.getCode(),ResultCode.ERROR.getMsg());
    }

    /**
     * @author 木月丶
     * @description 自定义错误信息的错误返回
     * @return Result
     */
    public static Result error(String msg){
        return new Result(ResultCode.ERROR.getCode(),msg);
    }

}
