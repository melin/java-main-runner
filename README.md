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

使用方法
-------------------

新创建maven工程：
`mvn archetype:generate -DgroupId=com.mycompany.helloworld -DartifactId=helloworld -Dpackage=com.mycompany.helloworld -Dversion=1.0-SNAPSHOT`

添加java-main-runner依赖：

```xml
<dependency>
       	<groupId>com.github.runner</groupId>
	<artifactId>java-main-runner</artifactId>
	<version>1.0.0-SNAPSHOT</version>
</dependency>
```

添加maven plguin 插件：

```xml
       		<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<configuration>
					<source>1.6</source>
					<target>1.6</target>
				</configuration>
			</plugin>

			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-surefire-plugin</artifactId>
				<configuration>
					<skipTests>false</skipTests>
					<argLine>-Dfile.encoding=UTF-8</argLine>
				</configuration>
			</plugin>
			
			<plugin>
      			<groupId>org.codehaus.mojo</groupId>
      			<artifactId>exec-maven-plugin</artifactId>
      			<configuration>
        			<executable>java</executable>
        			<mainClass>com.github.runner.ServerStartup</mainClass>
      			</configuration>
    		</plugin>
    		
    		<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-jar-plugin</artifactId>
				<configuration>
					<excludes>
						<exclude>*.xml</exclude>
					</excludes>
				</configuration>
			</plugin>

			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-assembly-plugin</artifactId>
				<configuration>
					<descriptors>
						<descriptor>src/main/assembly/assembly.xml</descriptor>
					</descriptors>
				</configuration>
			</plugin>
```

项目分为三种profile，分别为：develipment、test、production，在pom.xml文件中添加配置：

```xml
	<profiles>
		<profile>
			<id>development</id>
			<activation>
				<os>
					<family>windows</family>
				</os>
			</activation>
			<properties>
				<profiles.active>development</profiles.active>
			</properties>
		</profile>
		<profile>
			<id>test</id>
			<activation>
				<os>
					<family>unix</family>
				</os>
			</activation>
			<properties>
				<profiles.active>test</profiles.active>
			</properties>
		</profile>
		<profile>
			<id>production</id>
			<properties>
				<profiles.active>production</profiles.active>
			</properties>
		</profile>
	</profiles>
```

在src/main 目录下添加文件，文件从java-main-runner-test中获取。

assembly/assembly.xml
scripts/env.sh
scripts/server.sh

以上配置信息，请具体参考java-main-runner-test工程配置。


创建AplicationServer类，继承com.github.runner.support.AbstractAplicationServer。

```java
public class AplicationServer extends AbstractAplicationServer {
	private static final Logger LOGGER = LoggerFactory.getLogger(AplicationServer.class);

	@Override
	public void stop() {
		LOGGER.info("stop======");
	}
	
	@Override
	public void start() {
		LOGGER.info("start======");
	}
}
```

在src/main/resources/META-INF/main-class文件添加AplicationServer类的全路径。

本地开发环境执行以下命令，启动应用程序：

`mvn exec:java`



java-web-runner-test
-------------------

java-main-runner 添加tomcat嵌入运行功能，请参考java-web-runner-test实例，
注意assembly.xml中配置：`<exclude>com.github.web.demo:java-web-runner-test</exclude>`
等于当前工程：groupId:artifactId


注意
-------------------
1：如果通过java代码调用./bin/server.sh start命令，需要把server.sh中start_server function最后三行代码替换为
`exit 0;`

2：META-INF/logback-*.xml文件中 `<contextName>logback-4001</contextName>`配置，4001与env.sh中JMX_PORT值是一致的，
这样可以保证统一机器中可以部署多个应用，只要保证应用之间端口是不一样。
