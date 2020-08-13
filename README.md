# cps-mall
优惠券商城后台，支持淘宝、拼多多、京东、唯品会、蘑菇街，预留接口，可以很方便的扩展其他优惠券平台。
精力有限前端仅适配了微信小程序，可根据接口自行适配小程序、PC、H5、App等平台。



##### 微信扫码预览：
![微信扫码预览](https://mall-share.oss-cn-shanghai.aliyuncs.com/share/untitled.png?x-oss-process=image/resize,h_200,w_200 "微信扫码预览")


------------

#### 开发环境、用到的框架和类库：
- 开发环境：
  - IDEA
  - JDK-1.8
  - maven-3.6.1
  - MySql-5.7
  - Redis-3.2.100
  - RabbitMq-3.7.14（需安装rabbitmq_delayed_message_exchange延时消息插件）
- 用到的框架：
  - SpringCloud-Greenwich.SR2
  - SpringBoot-2.1.6
  - MyBatis-3.4.6
  - Shiro
  - [Seata](https://github.com/seata/seata "Seata")
  - [通用Mapper](https://github.com/abel533/Mapper "通用Mapper")
- 用到的类库：
  - druid
  - RedisLockRegistry：Spring分布式锁
  - [hutool](https://github.com/looly/hutool "hutool")
  - [ip2region：IP地址解析](https://github.com/lionsoul2014/ip2region "ip2region：IP地址解析")
  - [easyexcel：阿里POI](https://github.com/alibaba/easyexcel "easyexcel：阿里POI")
  - [HanLP：分词工具](https://github.com/hankcs/HanLP "HanLP：分词工具")
  - [PageHelper：MyBatis分页插件](https://github.com/pagehelper/Mybatis-PageHelper "PageHelper：MyBatis分页插件")
  - [weixin-java-miniapp：微信开发工具包](https://github.com/Wechat-Group/WxJava "weixin-java-miniapp：微信开发工具包")
#### 模块功能简介：
- api_mall: 商城模块，整合淘宝、拼多多、唯品会、蘑菇街平台的SDK，抹除各个平台间的差异，提供了统一的API
- api_user：用户模块，包含的具体内容如下：
  - 订单查询：包含自购订单、分享订单的查询。
  - 账单查询：包含用户自购、分享、锁定产生的收益账单。
  - 三级分销：提供各级用户查询、相关收益，各级佣金比例可调。
  - 信用体系：用户购买习惯良好、使用频繁、分享好友等操作则信用提升，反之信用下降，信用越高自动返现额度越大、速度越快，增强用户体验。
  - 浏览历史、收藏记录、意见反馈等功能的支持。
- api_pay: 支付/付款模块，为项目提供支付、付款的能力。
- api_activite: 活动模块，可按规则配置相关营销活动，如：高温补贴、看视频领红包、分享返现等。
- api_lottery: 道具商城，可配置添加一些功能道具，如：VIP、收益翻倍卡等营销道具。
- api_mini: 小程序功能模块，提供微信小程序平台独有的功能，小程序登录、消息推送、服务认证、生成分享海报、生成二维码等。
- cron_service: 定时任务模块，主要服务于用户订单，定时从cps平台拉取订单。
- wind_control: 风控模块，防止用户恶意操作对平台造成损失

#### 架构简介：
- cloud_eureka：服务发现组件
- cloud_gateway：zuul网关
  - 整合Shiro实现权限管理。
  - 配合Feign ErrorDecoder实现微服务下的异常处理。[[设计原则]](https://zhuanlan.zhihu.com/p/68730428 "[设计原则]")
- comment_web：基础模块，提供web访问能力公共基础模块，该模块下的配置全局生效
  - 表单验证Aspect
  - 异常传递Aspect
  - 请求记时Aspect
  - JSON解析器Config
  - Redis锁、Redis缓存Config
  - DataSource详细配置、Redis连接池、RabbitMq、Zipkin等配置文件
  - Mybatis分页插件、通用Mapper等配置文件
- comment_api: 封装第三方cps平台api接口，按需引用
- comment_feign: FeignClient客户端存访位置，内部接口调用需引入此模块
- comment_seata: 阿里分布式事务框架Seata相关配置 [[项目地址]](https://github.com/seata/seata "[项目地址]")
- comment_mq: 引入RabbitMq
  - 自定义一套消息总线，支持分发的消息类型位于UserActionEnum
- comment_serialize: 存访各个模块vo、bo、entity、form、constant等bean，按需引用
- comment_utils: 全局工具包


------------

#### 使用前的准备
- 导入sql文件，生成表。
- 注册淘宝、拼多多、蘑菇街、唯品会任一平台联盟开发者账号，获取appId、secret等信息,
- 申请微信支付，并开通企业付款（不需要返现，则不需要）
- 浏览各个模块下resources目录下包含“demo”配置文件，酌情修改，改好后删除文件名中的“-demo”
- 下载Seata源码修改server模块配置文件并编译，具体操作如下：（Seata版本：1.1.0）

1. 修改seata Server模块配置文件file.conf
```
store {
  ## store mode: file、db
  mode = "db"
  ## file store property
  file {
    ## store location dir
    dir = "sessionStore"
    # branch session size , if exceeded first try compress lockkey, still exceeded throws exceptions
    maxBranchSessionSize = 16384
    # globe session size , if exceeded throws exceptions
    maxGlobalSessionSize = 512
    # file buffer size , if exceeded allocate new buffer
    fileWriteBufferCacheSize = 16384
    # when recover batch read size
    sessionReloadReadSize = 100
    # async, sync
    flushDiskMode = async
  }
  ## database store property
  db {
    ## the implement of javax.sql.DataSource, such as DruidDataSource(druid)/BasicDataSource(dbcp) etc.
    datasource = "druid"
    ## mysql/oracle/h2/oceanbase etc.
    dbType = "mysql"
    driverClassName = "com.mysql.jdbc.Driver"
    url = "jdbc:mysql://数据库连接/wechat_seata"
    user = "账号"
    password = "密码"
    minConn = 1
    maxConn = 10
    globalTable = "global_table"
    branchTable = "branch_table"
    lockTable = "lock_table"
    queryLimit = 100
  }
}
```
2. 修改seata Server模块配置文件registry.conf

```
registry {
  # file 、nacos 、eureka、redis、zk、consul、etcd3、sofa
  type = "eureka"
#  eureka {
#    serviceUrl = "http://eureka-service:8101/eureka/"
#    application = "seata-server"
#    weight = "1"
#  }
  eureka {
    serviceUrl = "http://你的eureka服务器地址/eureka/"
    application = "seata-server"
    weight = "1"
  }
}
config {
  # file、nacos 、apollo、zk、consul、etcd3
  type = "file"
  nacos {
    serverAddr = "localhost"
    namespace = ""
    group = "SEATA_GROUP"
  }
  consul {
    serverAddr = "127.0.0.1:8500"
  }
  apollo {
    app.id = "seata-server"
    apollo.meta = "http://192.168.1.204:8801"
    namespace = "application"
  }
  zk {
    serverAddr = "127.0.0.1:2181"
    session.timeout = 6000
    connect.timeout = 2000
  }
  etcd3 {
    serverAddr = "http://localhost:2379"
  }
  file {
    name = "file.conf"
  }
}
```

3. 修改完成后在seata项目根目录运行 `mvn clean package`，打包完成后项目根目录会生成distribution\bin\seata-server，启动seata-server后会注册到配置的eureka服务，所以在此之前应启动eureak服务，如果一切正常eureka控制台会出现seata-server服务（如需帮助请联系）

#### 启动服务
以上配置修改完成后可按如下顺序启动服务
1. cloud_eureka
2. seata-server
3. cloud_gateway
4. api_mall
5. api_user
6. api_activite
7. api_pay
8. api_mini
9. cron_service
10. wind_control

```
    <!--淘宝sdk-->
    <!-- ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓-->
    <dependency>
        <groupId>com.xm.tb</groupId>
        <artifactId>taobao-sdk</artifactId>
        <version>1.0.0</version>
    </dependency>
    <!--唯品会sdk-->
    <!-- ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓-->
    <dependency>
        <groupId>com.xm.wph</groupId>
        <artifactId>osp-sdk</artifactId>
        <version>1.0.0</version>
    </dependency>
    <dependency>
        <groupId>com.xm.wph</groupId>
        <artifactId>vop-sdk</artifactId>
        <version>1.0.0</version>
    </dependency>

    <!--蘑菇街sdk-->
    <!-- ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓-->
    <dependency>
        <groupId>com.xm.mgj</groupId>
        <artifactId>openapi-sdk</artifactId>
        <version>1.0.0</version>
    </dependency>

    <!--拼多多sdk-->
    <!-- ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓-->
    <dependency>
        <groupId>com.xm.pdd</groupId>
        <artifactId>pop-sdk</artifactId>
        <classifier>all</classifier>
        <version>1.6.1</version>
    </dependency>
```

以上依赖打包：

`链接：https://pan.baidu.com/s/1VDY0n9SEIK3T0VY_hsFYyw 提取码：ug5y`

也可自行去官网下载。
       

------------
联系我获取前端代码：

![微信扫码](https://mall-share.oss-cn-shanghai.aliyuncs.com/share/my.jpg?x-oss-process=image/resize,h_200,w_200 "微信扫码")
