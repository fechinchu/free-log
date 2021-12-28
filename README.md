# free-log
**Customize your operation log in almost any form**

**定制你的业务操作日志**

free-log是一款可扩展性比较高的日志框架:它能实现记录`某个人`在`某时刻`做了`什么事`,如果是修改,能够记录`修改前的值`,以及`修改后的值`.该操作`成功或者失败`,如果失败那么`失败原因是什么`;

## 1.Prepare

### 1.1.Maven依赖

```xml
<dependency>
    <groupId>cn.zhuguoqing</groupId>
    <artifactId>free-log-sdk</artifactId>
    <version>1.0.1-RELEASE</version>
</dependency>
```

### 1.2.SQL

1. 用户操作日志记录主表;

```sql
create table operation_log
(
    id              bigint           not null comment '主键id',
    name            varchar(128)     null comment '操作业务名',
    table_name      varchar(100)     null comment '操作表名',
    table_id        varchar(100)     null comment '操作表id',
    type            varchar(100)     null comment '操作类型,(添加ADD,删除DELETE,修改UPDATE)',
    if_success      int(2) default 1 null comment '是否操作成功,0:失败,1:成功',
    action          text             null comment '操作内容',
    detail          longtext         null comment '操作详情',
    import_filename varchar(100)     null comment '导入的文件名称',
    operator_name   varchar(100)     null comment '操作人名',
    operation_time  timestamp        null comment '操作时间',
    operator_id     varchar(100)     null comment '操作人id',
    operator_ip     varchar(100)     null comment '操作人ip'
)
    comment '用户操作日志记录表';
```

2. 操作日志记录详情表;

```SQL
create table operation_log_detail
(
    id               bigint auto_increment comment '主键id'
        primary key,
    operation_log_id bigint       not null comment '操作日志id',
    clm_name         varchar(255) null comment '字段名',
    clm_comment      varchar(255) null comment '字段描述',
    old_string       longtext     null comment '旧值',
    new_string       longtext     null comment '新值'
)
    comment '操作日志详情表';
```

3. 操作日志Debugger期间错误记录表,主要用在开发测试阶段,快速定位问题(不是必要);

```SQL
create table operation_log_debug
(
    id                 bigint auto_increment comment '主键id'
        primary key,
    operation_log_id   bigint    null comment '操作日志id',
    log_positioning_id bigint    null comment '日志定位id,用来去服务器中进行定位',
    error_info         longtext  null comment '错误信息',
    create_time        timestamp null
)
    comment '用户操作日志框架错误记录表';
```

## 2.Do



