package cn.zhuguoqing.operationLog.bean.annotation;

import cn.zhuguoqing.operationLog.bean.enums.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author:guoqing.zhu
 * @Date：2021/11/26 15:40
 * @Desription: TODO
 *
 * @Version: 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLog {
    /**
     * 业务名
     */
    String name();

    /**
     * 操作成功描述,后期统一转成action
     */
    String success();

    /**
     * 操作失败描述,后期统一转成action
     */
    String fail() default "";

    /**
     * 详细信息
     */
    String detail() default "";

    /**
     * 表名,注意:如果是复杂更新操作的话必填
     */
    String table() default "";

    /**
     * id 在函数的字段的名,默认为id
     */
    String idName() default "id";

    /**
     * id 在函数的字段的值,注意:如果是更新操作的话必填
     */
    String idRef() default "";

    /**
     * 需要记录的字段,如果不填写那么采用数据库的字段,
     * 关于createTime,updateTime更新字段已经做了过滤
     */
    String[] includeColumn() default {};

    /**
     * 不需要记录的字段,如果不填写那么采用数据库的字段
     * @return
     */
    String[] excludeColumn() default {};

    /**
     * 操作类型
     */
    OperationType type();

    /**
     * 日志记录的先决条件
     * @return
     */
    String condition() default "";

    /**
     * 自定义函数操作,用于复杂的更新操作
     * @return
     */
    String customMethod() default "";






}
