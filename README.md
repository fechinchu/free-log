# free-log

* [1.Prepare](#1prepare)
     * [1.1.Maven依赖](#11maven依赖)
     * [1.2.SQL](#12sql)
     * [1.3.配置你的持久化方案](#13配置你的持久化方案)
     * [1.4.@EnableOperationLog](#14enableoperationlog)
* [2.Just Do it](#2just-do-it)
  * [2.1.基础使用](#21基础使用)
  * [2.2.复杂修改记录前后对比值](#22复杂修改记录前后对比值)
  * [2.3.上传文件记录日志](#23上传文件记录日志)
* [3.How can I do better?](#3how-can-i-do-better)
  * [3.1.如何能记录我只想记录部分字段(COMPLEX_UPDATE)](#31如何能记录我只想记录部分字段complex_update)
  * [3.2.如何修改我需要修正字段的注释或值(COMPLEX_UPDATE)](#32如何修改我需要修正字段的注释或值complex_update)
  * [3.3.如何在记录日志的时候记录操作人的信息](#33如何在记录日志的时候记录操作人的信息)
  * [3.4.如何修正日志记录的信息](#34如何修正日志记录的信息)
    * [3.4.1.IParseFunction与IModifyColCommentValueService区别](#341iparsefunction与imodifycolcommentvalueservice区别)
  * [3.5.如何根据方法执行的情况来动态的描述日志记录](#35如何根据方法执行的情况来动态的描述日志记录)
    * [3.5.1.基于SpEL表达式](#351基于spel表达式)
    * [3.5.2.基于LogRecordContext上下文对象](#352基于logrecordcontext上下文对象)
  * [3.6.如何能设定条件,条件满足才会记录日志](#36如何能设定条件条件满足才会记录日志)
* [4.How to customize?](#4how-to-customize)
  * [4.1.如何将日志和上传的文件进行集中管理](#41如何将日志和上传的文件进行集中管理)
  * [4.2.如何快速记录和定位日志问题](#42如何快速记录和定位日志问题)
  * [4.3.如何将Debug日志也进行集中管理](#43如何将debug日志也进行集中管理)
  * [4.4.假如项目中在并没有抛出异常而是封装成Result返回,我如何得到错误信息并记录?](#44假如项目中在并没有抛出异常而是封装成result返回我如何得到错误信息并记录)
* [5.Advanced features](#5advanced-features)
  * [5.1.I want do it by myself](#51i-want-do-it-by-myself)

**Customize your operation log in almost any form**

**定制你的业务操作日志**

free-log是一款可扩展性很高的日志框架:它能实现记录`某个人`在`某时刻`做了`什么事`,如果是修改,能够记录`修改前的值`,以及`修改后的值`.该操作`成功或者失败`,如果失败那么`失败原因是什么`;

Github源码地址:https://github.com/fechinchu/free-log

## 1.Prepare

### 1.1.Maven依赖

```xml
<dependency>
    <groupId>cn.zhuguoqing</groupId>
    <artifactId>free-log-sdk</artifactId>
    <version>1.0.3-RELEASE</version>
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

### 1.3.配置你的持久化方案

* 默认:如果你只是需要将日志记录在你所在服务的SQL数据库的数据源中.那么直接继承`AbstractLogInsertAndImportService`并且不用重写任何方法,直接注册到Spring容器即可.

```java
/**
 * 2021/12/28 15:43
 *
 * @author guoqing.zhu
 *     <p>description:告诉free-log日志记录以及日志详情该记录在什么地方
 *     <p>案例中选择MySQL,直接继承抽象类,并使用默认,不做修改
 */
@Service
public class GoodsLogInsertAndImportServiceImpl extends AbstractLogInsertAndImportService {}
```

* 定制:如果你需要记录在其他的数据源中,你需要实现`ILogInsertAndImportService`或者继承`AbstractLogInsertAndImportService`并重写方法来配置你的持久化方案.甚至是自己用来打印在控制台

### 1.4.`@EnableOperationLog`

如果你所使用的项目是SpringBoot项目,给你的项目加上`@EnableOperationLog`注解,如果是Spring的项目,通过`@Import({OperationAutoConfiguration.class})`即可;

```java
@SpringBootApplication
@MapperScan("cn.zhuguoqing.demo.mapper")
@EnableOperationLog
public class FreeLogDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreeLogDemoApplication.class);
    }
}
```

准备工作就全部结束了.下面开始编写你的日志把.

## 2.Just Do it

### 2.1.基础使用

给你想要打印日志的方法加上`@OperationLog`注解.

```java
@RequestMapping("addGoods")
  @OperationLog(
      name = "商品管理",
      success = "新增名称为:[{{#goodsDomain.name}}]的商品",
      fail = "新增名称为:[{{#goodsDomain.name}}]的商品失败,失败原因:{{#_errorMsg}}",
      type = OperationType.ADD)
  public BasicResult addGoods(@RequestBody @NotNull GoodsDomain goodsDomain) {
    goodsService.addGoods(goodsDomain);
    return BasicResult.success();
  }
```

上述案例是添加商品的简单案列.案列详情见https://github.com/fechinchu/free-log/tree/main/free-log-demo

* `success`是你该方法执行成功时记录的日志;
* `fail`是你该方法执行失败记录的日志;**如果你并不像记录失败日志,那不编写该属性即可**
* `{{#goodsDomain.name}}`可以自动替换成参数中`goodsDomain`中的`name`属性(在后面讲);
* `{{#_errorMsg}}`,可以自动替换成该方法所抛出的异常;
* `type`目前分为六种,这里使用的是`ADD`,表示的是新增操作.

注解编写完成,日志就会被记录下来:

![image-20211229154722478](https://fechin-picgo.oss-cn-shanghai.aliyuncs.com/PicGo/image-20211229154722478.png)

### 2.2.复杂修改记录前后对比值

```java
@RequestMapping("updateGoods")
  @OperationLog(
      type = OperationType.COMPLEX_UPDATE,
      name = "商品管理",
      success = "修改名称为{{#goodsDomain.name}}的商品",
      fail = "修改名称为:[{{#goodsDomain.name}}]的商品失败,失败原因:{{#_errorMsg}}",
      table = "mydb01.goods_spu",
      idRef = "{{#goodsDomain.id}}")
  public BasicResult updateGoods(@RequestBody @NotNull GoodsDomain goodsDomain) {
    goodsService.updateGoods(goodsDomain);
    return BasicResult.success();
  }
```

同样加上`@OperationLog`注解,将`type`改成`OperationType.COMPLEX_UPDATE`,这里比基础使用需要多设置几个地方

* 如果是根据id进行修改:`idRef`设置成你的id;
  * 如果不是根据id进行修改,例如根据code进行修改:那么需要加上`idName = "code"`;
* 加上你的库表名:格式是`schemaName.tableName`;

注解编写完成,日志就会被记录:

日志主表:

![image-20211229163712406](https://fechin-picgo.oss-cn-shanghai.aliyuncs.com/PicGo/image-20211229163712406.png)

日志详细表:

![image-20211229163724807](https://fechin-picgo.oss-cn-shanghai.aliyuncs.com/PicGo/image-20211229163724807.png)

**此时修改的描述,以及修改了什么,包括旧值和新值就全部记录完成了**;

这里的`clm_comment`是根据数据库字段注释映射过来的,不需要人为干预;

### 2.3.上传文件记录日志

1. 告诉系统里需要将文件存在哪里?在配置文件中编写存储路径:

![image-20211229200828781](https://fechin-picgo.oss-cn-shanghai.aliyuncs.com/PicGo/image-20211229200828781.png)

2. 给你的方法加上注解

```java
@RequestMapping("importSomething")
  @OperationLog(
      type = OperationType.IMPORT,
      name = "商品管理",
      success = "上传文件喽,文件名为:{{#importFile.getOriginalFilename()}}")
  public BasicResult importSomething(@NotNull(message = "请上传文件") MultipartFile importFile) {
    return BasicResult.success();
  }
```

3. 上传得到日志:

![image-20211229201020645](https://fechin-picgo.oss-cn-shanghai.aliyuncs.com/PicGo/image-20211229201020645.png)

4. 文件就会在你设置的地方备份一份,文件名称是日志的id,当然文件名称在数据库中也有记录,后期查阅下载就会很方便;

![image-20211229201045880](https://fechin-picgo.oss-cn-shanghai.aliyuncs.com/PicGo/image-20211229201045880.png)

## 3.How can I do better?

### 3.1.如何能记录我只想记录部分字段(COMPLEX_UPDATE)

* 假如我只想记录部分字段,给你的注解加上`includeColumn = {"name"}`,就可以只记录name的值;
* 假如我不想记录部分字段,给你的注解加上`excludeColumn = {"name"}`;
* 假如我的项目中有一些通用的字段我不想要,比如`is_deleted`,`create_time`,`update_time`,我直接实现`IExcludeColumnGetService`接口并将其注入到Spring即可.

```java
/**
 * @author guoqing.zhu
 *     <p>description:设置全局的需要忽略的数据库字段,需要自己实现,如不实现,则不忽略
 * @see cn.zhuguoqing.operationLog.service.impl.DefaultExcludeColumnGetServiceImpl
 */
public interface IExcludeColumnGetService {

  /**
   * 获取排除的字段
   *
   * @return 返回需要排除的字段集合
   */
  List<String> getExcludeColumn();
}
```

### 3.2.如何修改我需要修正字段的注释或值(COMPLEX_UPDATE)

很多时候我们部分数据存的是数字字典中的数据.我们不能直接把这些数据记录日志,我们需要的是他们的名字而不是id.我们可以实现`IModifyColCommentValueService`接口即可

```java
@Component
public class MotifyColValue implements IModifyColCommentValueService {
  @Override
  public CustomFunctionType getType() {
    return CustomFunctionType.VALUE;
  }

  @Override
  public List<String> getName() {
    return Lists.newArrayList("mydb01.goods_spu.name");
  }

  @Override
  public String modify(String param) {
    //加入是数字字典的code,我们在这边调用数字字典的接口转化成数字字典的值;
    //这个demo做简单更改;
    return "商品名称:" + param + "!!!";
  }
}
```

得到**修改后的结果**:

![image-20211229164238696](https://fechin-picgo.oss-cn-shanghai.aliyuncs.com/PicGo/image-20211229164238696.png)

* `getType()`分为两种:
  * `KEY`:修改的是字段的注释;
  * `VALUE`:修改的是字段的值;
* `getName()`:`SchemaName.tableName.colName`即库名.表名.字段名;
* `modify()`中可以直接设置转换规则.

从而实现通用的修改的值;

### 3.3.如何在记录日志的时候记录操作人的信息

实现`IOperatorGetService`接口即可

```java
@Component
public class OperatorGet implements IOperatorGetService {
  @Override
  public Operator getUser() {
    //在这里根据自己项目,用户所在地方进行获取封装
    Operator operator = new Operator();
    operator.setOperatorId("10086");
    operator.setOperatorName("帅的朱");
    operator.setOperatorIp("192.168.155.155");
    return operator;
  }
}
```

### 3.4.如何修正日志记录的信息

```java
@RequestMapping("updateGood01")
  @OperationLog(
          type = OperationType.DELETE,
          name = "商品管理",
          success = "删除了名为{getGoodsNameById{#id}}",
          fail = "删除了名为{getGoodsNameById{#id}}失败,失败原因:{{#_errorMsg}}")
  public BasicResult deleteGoodsById(@RequestParam @NotNull String id) {
    goodsService.deleteGoodsById(id);
    return BasicResult.success();
  }
```

这个场景中我们需要在action中记录获取删除的商品名称,我们需要实现`IParseFunction`接口,并自定义函数

```java
@Component
public class GetGoodsNameByIdParser implements IParseFunction {

  @Autowired private GoodsMapper goodsMapper;

  @Override
  public boolean executeBefore() {
    return true;
  }

  @Override
  public String functionName() {
    return "getGoodsNameById";
  }

  @Override
  public String apply(String value) {
    GoodsDomain goodsDomain = goodsMapper.selectByPrimaryKey(value);
    if(goodsDomain!=null){
      return goodsDomain.getName();
    }
    return value;
  }
}
```

* `executeBefore()`:是否在切点之前执行?默认`false`即在切点执行之后执行;这个需要谨慎编写,因为切点执行前后有可能是完全不一样的结果;
  * 这里的删除根据id进行查询我们就使用`before`;
* `functionName()`:方法名称;
* `apply()`自定义的方法;

#### 3.4.1.`IParseFunction`与`IModifyColCommentValueService`区别

两种接口的存在可以帮我们实现很多自定义的功能,比如`修正记录的值`,`修正记录的注释`,`修正记录的描述`;两者有一定区别:

* `IParseFunction`:主要用于`@OperationLog`注解中的`success`和`fail`的属性中,可以根据去修正日志记录的描述;
  * 案列:上述根据id获取名称(最简单的)
* `IModifyColCommentValueService`:主要用于复杂修改的全局的数据库中的字段的值和字段的注释的修正;以后复杂日志牵涉到该字段都会应用到该修正;
  * 案列:数据字典的值的获取;

### 3.5.如何根据方法执行的情况来动态的描述日志记录

#### 3.5.1.基于SpEL表达式

```java
@RequestMapping("commonOperation02")
  @OperationLog(
      type = OperationType.UPDATE,
      name = "商品管理",
      success =
          "{{#goodsDomain.shelve==1?'上架':#goodsDomain.shelve == 2?'下架':'删除'}}名称为:{{#goodsDomain.name}}的商品")
  public BasicResult commonOperation02(@RequestBody @NotNull GoodsCommonDomain goodsDomain) {
    return BasicResult.success();
  }
```

在SpEL中使用三元运算符来动态的描述日志.当我的`goodsDomain`中的`shelve`值是1那么就是上架名称为什么什么的商品;日志存储如下:

![image-20211229192053152](https://fechin-picgo.oss-cn-shanghai.aliyuncs.com/PicGo/image-20211229192053152.png)

当然SpEL表达式可不仅仅是三元运算符这么简单,正常的SpEL比如**判等**,**判空**,**计算**等等功能都是可以支持的;

甚至包括一些简单的方法调用如:`{{#goodsDomain.name.equals("小家伙")}}`都是可以解析的;返回结果是true或者是false;

#### 3.5.2.基于`LogRecordContext`上下文对象

```java
@RequestMapping("commonOperation02")
  @OperationLog(
      type = OperationType.UPDATE,
      name = "商品管理",
      success =
          "{{#goodsDomain.shelve==1?'上架':#goodsDomain.shelve == 2?'下架':'删除'}},{{#goodsDomain.name.equals('小家伙')}},名称为:{{#goodsDomain.name}}的商品,上下架结果为:{{#result}}")
  public BasicResult commonOperation02(@RequestBody @NotNull GoodsCommonDomain goodsDomain) {
    String result = doSomething(goodsDomain);
    LogRecordContext.putVariable("result",result);
    return BasicResult.success();
  }

  private String doSomething(GoodsCommonDomain goodsDomain) {
    return "得不到的永远在躁动";
  }
```

我们将代码中的`result`值放入到`LogRecordContext`的上下文对象中,便可以在日志中通过SpEL表达式`{{#result}}`来获取该执行结果;

![image-20211229193428908](https://fechin-picgo.oss-cn-shanghai.aliyuncs.com/PicGo/image-20211229193428908.png)

假如我代码是异步执行的?

没关系,也能搞定.案例如下:

```java
@RequestMapping("commonOperation02")
@OperationLog(
    type = OperationType.UPDATE,
    name = "商品管理",
    success =
        "{{#goodsDomain.shelve==1?'上架':#goodsDomain.shelve == 2?'下架':'删除'}},{{#goodsDomain.name.equals('小家伙')}}," +
                "名称为:{{#goodsDomain.name}}的商品,上下架结果为:{{#result}},看看异步执行能不能获取到:{{#result01}}")
public BasicResult commonOperation02(@RequestBody @NotNull GoodsCommonDomain goodsDomain) {
  String result = doSomething(goodsDomain);
  LogRecordContext.putVariable("result", result);
  new Thread(() -> LogRecordContext.putVariable("result01", "难受马飞")).run();
  return BasicResult.success();
}

private String doSomething(GoodsCommonDomain goodsDomain) {
  return "得不到的永远在躁动";
}
```

![image-20211229194551163](https://fechin-picgo.oss-cn-shanghai.aliyuncs.com/PicGo/image-20211229194551163.png)

目前还不能实现进程之间的通信.

### 3.6.如何能设定条件,条件满足才会记录日志

`condition`属性便能满足你的需求

```java
@RequestMapping("commonOperation")
  @OperationLog(
      type = OperationType.UPDATE,
      name = "商品管理",
      success = "我是日志我最大",
      condition = "{{#id.equals('1')}}")
  public BasicResult commonOperation(@RequestParam @NotNull String id) {
    return BasicResult.success();
  }
```

如果id为1就记录,如果不为1就不记录;

## 4.How to customize?

free-log提供了非常多的拓展点,来满足不一样的定制需求.上述的案例都是基于单个服务,不怎么复杂的小修改小记录.

如下将介绍真实的微服务环境下的使用;

### 4.1.如何将日志和上传的文件进行集中管理

实现`ILogInsertAndImportService`接口.

```java
/**
 * @author guoqing.zhu
 *     <p>description:告诉free-log日志该记录在什么地方,以及文件存放的位置
 * @see cn.zhuguoqing.operationLog.service.impl.DefaultLogInsertAndImportServiceImpl
 */
public interface ILogInsertAndImportService {

  /**
   * 插入主要日志
   *
   * @param op 主要日志
   * @return
   */
  void insertOperationLog(OperationLogDomain op);

  /**
   * 插入详细日志
   *
   * @param opds 详细日志集合
   */
  void insertOperationLogDetail(List<OperationLogDetailDomain> opds);

  /**
   * 插入文件 如果项目中有需要导入的功能,可以实现,如果没有就不用实现
   *
   * @param importFileDTO 文件信息,包括文件名称,文件id,以及文件的byte数组
   * 文件id主要用于区别各个文件,用的是日志主表的主键;
   */
  void importFile(ImportFileDTO importFileDTO);
}
```

微服务去实现该接口后使用RPC或者HTTP调用将日志信息,日志详细信息,文件信息放在你想要放置的地方进行统一管理;

其中`importFile()`方法的参数`importFileDTO`中包含了文件信息,文件id(日志id),字节数组等.方便远程调用与存储;

统一管理后便于日志和文件的查阅;

### 4.2.如何快速记录和定位日志问题

实现`AbstractDebugService`,什么都不用做,直接放入Spring容器中即可实现Error信息的记录;

```java
/**
 * <p>2021/12/29 00:18</p>
 *
 * @author guoqing.zhu
 * <P>description:</p>
 */
@Service
public class GoodsDebugServiceImpl extends AbstractDebugService {
}
```

在表`operation_log_debug`表中,如果出现问题,看到记录下来的问题信息.

* `operation_log_id`:是日志的主键;
* `log_position_id`:是生成的用于定位的Id.出现问题需要查看详细信息.拿着该id去服务器日志寻找可以快速的定位到当前接口出现的问题.

### 4.3.如何将Debug日志也进行集中管理

```java
/**
 * @author guoqing.zhu
 *     <p>description:用于记录错误日志的debugger接口
 */
public interface IDebugService {

  /**
   * 记录错误信息
   *
   * @param operationDebugDTO 封装的错误信息
   */
  void saveErrorInfo(OperationDebugDTO operationDebugDTO);
}
```

实现上述`IDebugService`接口,封装的是异常信息,你想通过RPC传递还是想记录成文件任君选择;

### 4.4.假如项目中在并没有抛出异常而是封装成Result返回,我如何得到错误信息并记录?

实现`ICheckResultService`接口.

```java
/**
 * 2021/12/28 16:06
 *
 * <p>description:该类用于适配代码中封装的返回结果;
 *
 * <p>一些项目中:在控制层没有抛出异常,而是对结果进行封装;这时候实现该类,可以对异常或正常结果进行记录
 *
 * @author guoqing.zhu
 */
@Component
public class GoodsCheckResult implements ICheckResultService {
  @Override
  public void checkResult(
      AbstractOperationLogStrategy.MethodExecuteResult methodExecuteResult, Object proceed) {

    if (proceed != null) {
      if (proceed instanceof BasicResult) {
        BasicResult result = (BasicResult) proceed;
        if (!result.isSuccess()) {
          String message = result.getMessage();
          methodExecuteResult.setErrorMsg(message);
          methodExecuteResult.setSuccess(false);
        }
      }
    }
  }
}
```

我们去设置`MethodExecuteResult`,来让free-log知道我是在报错而不是正常返回.

在代码中我们可以判断切点响应的结果是不是返回的项目中设定的`Result`,如果是那么它一定是可以判断是否执行成功,如果失败了的话,异常信息是什么,我们只需要将其拿出来设置到`MethodExecuteResult`中即可.

案列如下:

```java
@RequestMapping("ifSuccess")
  @OperationLog(
      type = OperationType.UPDATE,
      name = "商品管理",
      success = "操作成功了呀!{{#_ret}}",
      fail = "操作失败了哟,{{#_errorMsg}}")
  public BasicResult<String> ifSuccess() {
    // return BasicResult.success("冲啊!!!");
    return BasicResult.fail("10087", "还是失败了");
  }
```

记录的信息如下:

![image-20211229205732404](https://fechin-picgo.oss-cn-shanghai.aliyuncs.com/PicGo/image-20211229205732404.png)

## 5.Advanced features

**一般的更新操作在2.2介绍的注解方式就能得到解决.如下涉及到的复杂更新记录值出现的频次并不是很高,如果出现Diff包下的几个类或许能够给你提供一些帮助!**

> Diff包下用来记录比较新旧值的日志方案对代码本身有一定的侵入性.
>
> 但复杂更新不可能完全只使用AOP和MyBatis Plugin就能实现对无入侵式的记录复杂更新的新旧值;
>
> 目前思考下来通用性比较高的就是Diff包下的方案.后期可能会更新使用更好的方案;

<img src="https://fechin-picgo.oss-cn-shanghai.aliyuncs.com/PicGo/image-20211229212031195.png" alt="image-20211229212031195" style="zoom:50%;" />

使用的实体是`DiffDTO`,代码如下:

```java
package cn.zhuguoqing.operationLog.bean.dto;

import cn.zhuguoqing.operationLog.bean.enums.CustomFunctionType;
import cn.zhuguoqing.operationLog.bean.enums.DiffType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;

/**
 * @author guoqing.zhu
 *     <p>description:建造者模式的DiffDTO.
 */
@Getter
@Slf4j
public class DiffDTO {

  /** exmple: product.g_prod_spu,|必传| */
  private String schemaTableName;

  /** schema名称 */
  private String schema;

  /** 表名 */
  private String table;

  /** 键 example where {键} = 123; */
  private String keyName;

  /** 值 example:where id = {值} */
  private List<String> keyValue;

  /** SQL查询后拼接的内容,|可以不传|,example:where id = 1 and {is_deleted = 0}; */
  private String appendSQLAfterWhere;

  /** 记录的字段名,|可以不传| */
  private String diffName;

  /** 关于什么的信息,|可以不传|, */
  private String informationAboutWhat;

  /** 需要记录的字段,|可以不传|,直接使用数据库的字段 */
  private List<String> includeRecordClms;

  /** 不需要记录的字段,|可以不传|,直接使用数据库的字段 */
  private List<String> excludeRecordClms;

  /** 自定义关于字段的名称的函数,|可以不传| */
  private Map<String, Function<String, String>> customCommentFunctionMap;

  /** 自定义关于字段的值的函数,|可以不传| */
  private Map<String, Function<String, String>> customValueFunctionMap;

  private DiffDTO(Builder builder) {
    this.schemaTableName = builder.schemaTableName;
    this.schema = builder.schema;
    this.table = builder.table;
    this.keyName = builder.keyName;
    this.keyValue = builder.keyValue;
    this.appendSQLAfterWhere = builder.appendSQLAfterWhere;
    this.diffName = builder.diffName;
    this.includeRecordClms = builder.includeRecordClms;
    this.excludeRecordClms = builder.excludeRecordClms;
    this.informationAboutWhat = builder.informationAboutWhat;
    this.customCommentFunctionMap = builder.customCommentFunctionMap;
    this.customValueFunctionMap = builder.customValueFunctionMap;
  }

  public static class Builder {

    private static final String KEY_NAME = "id";

    private DiffType diffType;

    private String schemaTableName;

    private String schema;

    private String table;

    private String keyName = KEY_NAME;

    private List<String> keyValue;

    private String appendSQLAfterWhere;

    private String diffName;

    private List<String> includeRecordClms;

    private List<String> excludeRecordClms;

    private String informationAboutWhat;

    private final Map<String, Function<String, String>> customCommentFunctionMap = new HashMap<>(8);

    private final Map<String, Function<String, String>> customValueFunctionMap = new HashMap<>(8);

    public DiffDTO build() {
      try {
        // 校验逻辑放到这里来做，包括必填项校验、依赖关系校验、约束条件校验等
        if (Objects.isNull(diffType)) {
          diffType = DiffType.SINGLE_UPDATE;
        }
        if (StringUtils.isEmpty(schema)) {
          throw new IllegalArgumentException("schemaTableName cannot be null");
        }
        if (StringUtils.isEmpty(table)) {
          throw new IllegalArgumentException("schemaTableName cannot be null");
        }
        if (StringUtils.isEmpty(appendSQLAfterWhere)) {
          appendSQLAfterWhere = " ";
        } else {
          if (!appendSQLAfterWhere.trim().startsWith("and")) {
            appendSQLAfterWhere = " and " + appendSQLAfterWhere;
          }
        }
        if (diffType.getType().equals(DiffType.LIST_ADD_DELETE.getType())
            && StringUtils.isEmpty(diffName)) {
          throw new IllegalArgumentException("当使用list_add_delete的diff时候 diffName不能为空");
        }
        if (!CollectionUtils.isEmpty(includeRecordClms)
            && !CollectionUtils.isEmpty(excludeRecordClms)) {
          throw new IllegalArgumentException("includeRecordClms与excludeRecordClms不能同时存在");
        }
      } catch (Exception e) {
        log.error("--OperationLog--DiffDTO build error", e);
      }
      return new DiffDTO(this);
    }

    /**
     * DiffType |必传|
     *
     * @param diffType diffType
     * @return 建造者
     */
    public Builder setDiffType(DiffType diffType) {
      try {
        if (Objects.isNull(diffType)) {
          throw new IllegalArgumentException("diffType cannot be null");
        }
        this.diffType = diffType;
      } catch (IllegalArgumentException e) {
        log.error("--OperationLog-- setDiffType error", e);
      }
      return this;
    }

    /**
     * exmple: product.g_prod_spu,|必传|
     *
     * @param schemaTableName 库名.表名
     * @return 建造者
     */
    public Builder setSchemaTableName(String schemaTableName) {
      try {
        if (StringUtils.isEmpty(schemaTableName)) {
          throw new IllegalArgumentException("schemaTableName cannot be null");
        }
        if (!schemaTableName.contains(".")) {
          throw new IllegalArgumentException("schemaTableName的格式需要设置为:schemaA.tableB");
        }
        String[] split = schemaTableName.split("\\.");
        if (split.length != 2) {
          throw new IllegalArgumentException("schemaTableName的格式需要设置为:schemaA.tableB");
        }
        this.schemaTableName = schemaTableName;
        schema = split[0];
        table = split[1];
      } catch (IllegalArgumentException e) {
        log.error("--OperationLog-- setSchemaTableName error", e);
      }
      return this;
    }

    /**
     * 键 example:where {键} = 123
     *
     * @param keyName 键的名称
     * @return 建造者
     */
    public Builder setKeyName(String keyName) {
      try {
        if (StringUtils.isEmpty(keyName)) {
          throw new IllegalArgumentException("keyName cannot be null");
        }
        this.keyName = keyName;
      } catch (IllegalArgumentException e) {
        log.error("--OperationLog-- setKeyName error", e);
      }
      return this;
    }

    /**
     * 值 example:where id = {值}
     *
     * @param keyValue 键的值
     * @return 建造者
     */
    public Builder setKeyValue(String... keyValue) {
      try {
        if (Objects.isNull(keyValue)) {
          throw new IllegalArgumentException("keyValue cannot be null");
        }
        if (keyValue.length == 0) {
          throw new IllegalArgumentException("keyValue size cannot be 0");
        }
        this.keyValue = new ArrayList<>(Arrays.asList(keyValue));
      } catch (IllegalArgumentException e) {
        log.error("--OperationLog-- setKeyValue error", e);
      }
      return this;
    }

    /**
     * SQL查询后拼接的内容,|可以不传|,example:where id = 1 and {is_deleted = 0};
     *
     * @param appendSQLAfterWhere SQL拼接后的内容
     * @return 建造者
     */
    public Builder setAppendSQLAfterWhere(String appendSQLAfterWhere) {
      try {
        if (StringUtils.isEmpty(appendSQLAfterWhere)) {
          throw new IllegalArgumentException("appendSQLAfterWhere cannot be null");
        }
        this.appendSQLAfterWhere = appendSQLAfterWhere;
      } catch (IllegalArgumentException e) {
        log.error("--OperationLog-- setAppendSQLAfterWhere error", e);
      }
      return this;
    }

    /**
     * 日志表述用的字段名,|可以不传|
     *
     * @param diffName 日志表述用的字段名
     * @return 建造者模式
     */
    public Builder setDiffName(String diffName) {
      try {
        if (StringUtils.isEmpty(diffName)) {
          throw new IllegalArgumentException("diffName cannot be null");
        }
        this.diffName = diffName;
      } catch (IllegalArgumentException e) {
        log.error("--OperationLog-- setDiffName error", e);
      }
      return this;
    }

    /**
     * 需要记录的字段,|可以不传|,直接使用数据库的字段
     *
     * @param includeRecordClm 需要记录的字段
     * @return 建造者
     */
    public Builder setIncludeRecordClms(String... includeRecordClm) {
      try {
        if (includeRecordClm == null) {
          throw new IllegalArgumentException("includeClms cannot be null");
        }
        if (includeRecordClm.length == 0) {
          throw new IllegalArgumentException("includeClms size cannot be 0");
        }
        this.includeRecordClms = new ArrayList<>(Arrays.asList(includeRecordClm));
      } catch (IllegalArgumentException e) {
        log.error("--OperationLog-- setIncludeRecordClms error", e);
      }
      return this;
    }

    /**
     * 不需要记录的字段,|可以不传|,直接使用数据库的字段
     *
     * @param excludeRecordClm 不需要记录的字段
     * @return 建造者
     */
    public Builder setExcludeRecordClms(String... excludeRecordClm) {
      try {
        if (excludeRecordClm == null) {
          throw new IllegalArgumentException("excludeClms cannot be null");
        }
        if (excludeRecordClm.length == 0) {
          throw new IllegalArgumentException("excludeClms size cannot be 0");
        }
        this.excludeRecordClms = new ArrayList<>(Arrays.asList(excludeRecordClm));
      } catch (IllegalArgumentException e) {
        log.error("--OperationLog-- setExcludeRecordClms error", e);
      }
      return this;
    }

    /**
     * 关于什么的信息,|可以不传|, example:关于[规格编码为:194276537401347]的限价
     *
     * @param informationAboutWhat 关于什么的信息
     * @return 建造者
     */
    public Builder setInformationAboutWhat(String informationAboutWhat) {
      try {
        if (StringUtils.isEmpty(informationAboutWhat)) {
          throw new IllegalArgumentException("informationAboutWhat cannot be null");
        }
        this.informationAboutWhat = informationAboutWhat;
      } catch (IllegalArgumentException e) {
        log.error("--OperationLog-- setInformationAboutWhat error", e);
      }
      return this;
    }

    /**
     * 自定义的函数,|可以不传|
     *
     * @param columnName
     * @param function
     * @return
     */
    public Builder addCustomFunction(
        CustomFunctionType type, Function<String, String> function, String... columnName) {
      try {
        if (Objects.isNull(columnName)) {
          throw new IllegalArgumentException("columnName cannot be null");
        }
        if (Objects.isNull(function)) {
          throw new IllegalArgumentException("function cannot be null");
        }
        if (Objects.isNull(type)) {
          type = CustomFunctionType.VALUE;
        }
        if (type.getType().equals(CustomFunctionType.KEY)) {
          for (String s : columnName) {
            customCommentFunctionMap.put(s, function);
          }

        } else {
          for (String s : columnName) {
            customValueFunctionMap.put(s, function);
          }
        }
      } catch (IllegalArgumentException e) {
        log.error("--OperationLog-- addCustomFunction error", e);
      }
      return this;
    }
  }
}

```

目前有几种比较策略针对不同的Diff场景:

* `ListAddAndDeleteDiffProcessor`:列表的增删改;
* `SingleAddDomainDiffProcessor`:添加数据;
* `SingleUpdateDomainDiffProcessor`:更新数据;

### 5.1.I want do it by myself

如果上述功能还是不能满足你复杂的业务需求.还有一个完全给你定制化的解决方案

* 使用`@OperationLog`注解中的`customMethod`属性;
  * 例如:`customMethod = "{updateFreightTemplate{#inputDTO.id}}"`;

* 将你想要从自定义方法中获取的内容在源方法中放入上下文对象中;
* 实现`IParseFunction`接口,编写自定义方法;
* 在`apply()`中你可以做任何你想要做的事情.当然就比较新旧值这方面来说`DiffUtil`也会提供一些方法供你使用;
