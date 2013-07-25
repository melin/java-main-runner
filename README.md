java-main-runner
================

平时写了一个Java程序，会希望把应用打包来运行，最好是提供一个脚本来运行。

这个工程做的事情：

- 配置了maven-assembly-plugin Maven插件，把工程里用到的依赖的Jar放到一起。
- 提供了运行脚本，启动、停止、重新加载lockback等命令。

打包
-------------------

执行命令
```bash
mvn install assembly:single
```

在`target`目录下生成一个`xxx-bin.tar.gz`文件，下面目录的压缩文件。

```bash
├─bin # 运行脚本
│      env.sh
│      server.sh
│
├─conf # 配置文件，运行时会把这个conf目录加载到Class Path上
│      META-INF/logback/logback-development.xml
│      META-INF/logback/logback-test.xml
│      META-INF/logback/logback-production.xml
│      META-INF/main-class
│      logback.xml
│
└─lib # 相关的依赖
       java-main-runner-1.0.0-SNAPSHOT.jar
       jakarta.commons.lang-2.5.jar
       log4j-1.2.17.jar
       main.runner-0.0.1-SNAPSHOT.jar
       slf4j-api-1.5.6.jar
       slf4j-log4j12-1.5.6.jar
       ......
```

运行
-------------------

解压`xxx-bin.tar.gz`文件，进入xxx-bin目录，执行server.sh脚本，脚本提供如下参数：

```bash
Usage: server.sh {start|status|stop|restart|logback}
       start:             start the xxx server
       stop:              stop the xxx server
       restart:           restart the xxx server
       logback:           reload logback config file
       status:            get xxx current status,running or stopped.
```
