## 给日志添加requestId的组件

给每个请求的相应添加requestId（需结合`component-dto`组件，对应`Response#requestId`），便于追踪溯源.

基于 `MDC`，也可以通过`logback-spring.xml`日志配置文件，配置请求的`requestId`

```xml
<!--logback-spring.xml-->
<configuration>
    <include resource="org/springframework/boot/logging/logback/defaults.xml"/>
    <property name="APP_NAME" value="component-file-qiniu-controller-starter"/>

    <property name="ENCODER_PATTERN"
              value="%d{[yyyy-MM-dd HH:mm:ss]} [%level] [%X{requestId}] [%logger{1}:%L-%thread]  %msg%n"/>

    <property name="PATTERN_COLOR"
              value="%yellow(%d{yyyy-MM-dd HH:mm:ss.SSS}) [%thread] %highlight(%-5level) [%X{requestId}] %green(%logger{50}) - %highlight(%msg) %n"/>
    <property name="ENCODING" value="UTF-8"/>


    <contextName>${APP_NAME}</contextName>
    <jmxConfigurator/>

    <contextListener class="ch.qos.logback.classic.jul.LevelChangePropagator">
        <resetJUL>true</resetJUL>
    </contextListener>

    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder charset="${ENCODING}">
            <pattern>${PATTERN_COLOR}</pattern>
            <charset>${ENCODING}</charset>
        </encoder>
    </appender>

    <!--rootLogger是默认的logger-->
    <root level="INFO">
        <!--定义了两个appender，日志会通过往这两个appender里面写-->
        <appender-ref ref="CONSOLE"/>
    </root>

    <!--应用日志-->
    <!--这个logger没有指定appender，它会继承root节点中定义的那些appender-->
    <logger name="com.lyloou" level="DEBUG"/>


</configuration>

```