package cn.zhuguoqing.demo.common.model;

import java.util.Objects;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/28 15:21
 * @Desription: TODO
 * @Version: 1.0
 */
public class BasicResult<T> {

    private final static String SUCCESS_CODE = "0";
    private final static String DEFAULT_FAIL_CODE = "-1";

    /**
     * 返回状态码
     */
    private String code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 返回结果
     */
    private T data;

    /**
     * 成功
     *
     * @return
     */
    public static <T> BasicResult<T> success() {
        BasicResult<T> basicResult = new BasicResult<>();
        basicResult.setCode(SUCCESS_CODE);
        return basicResult;
    }


    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BasicResult<T> success(T data) {
        BasicResult<T> basicResult = new BasicResult<>();
        basicResult.setCode(SUCCESS_CODE);
        basicResult.setData(data);
        return basicResult;
    }

    /**
     * 失败
     *
     * @param code
     * @param <T>
     * @return
     */
    public static <T> BasicResult<T> fail(String code, String message) {
        BasicResult<T> basicResult = new BasicResult<>();
        basicResult.setCode(code);
        basicResult.setMessage(message);
        return basicResult;
    }

    public boolean isSuccess() {
        return Objects.equals(BasicResult.SUCCESS_CODE, this.code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
