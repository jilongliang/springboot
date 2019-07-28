
# 1、docker常用命令使用
### 1.1 docker rm 命令强制删除容器
```
docker rm -f
```

### 1.2 docker rmi 命令强制删除镜像
```
docker rmi -f
```

### 1.3 docker build 命令 强制指定Dockerfile文件进行编译
```
docker build -t 镜像名称:镜像版本号-f Dockerfile文件目录  编译镜像的目录
```

### 1.4 docker tag 和 docker push命令结合的使用

* 将镜像打一下标签，然后安照标签和版本号进行推送到私服里面，标签名就以服务名和版本号即可      

```
docker tag  镜像名称:镜像版本号   私服URL地址/镜像名称:镜像版本号    
```

* 推镜像到私服里面

```
docker push 私服URL地址/镜像名称:镜像版本号
```
	
### 1.5  docker save 命令保存编译之后镜像文件存放到指定路径

```
docker save 镜像名称 -o 保存文件的路径/镜像名称.tar.gz
```


# 2、Dockerfile的使用
* FROM 来自哪里，一个Dockerfile必须有一个FROM关键字
* MAINTAINER 构建作者
* RUN  运行关键字
* WORKDIR 切换工作目录
* EXPOSE 暴露端口
* ADD 添加或说是拷贝
* CMD 执行命令(command)



# 3、shell脚本的使用
* ehco 输出的命令的使用
* mkdir -p 的命令的使用,-p表示父目录下面的子目录也进行创建


# 4、git常见命令
### 4.1 Git全局设置
```
git config --global user.name "admin"
git config --global user.email "admin@example.com"
```

##### 4.2 创建一个仓库
```
git clone ssh://git@192.168.1.235:7003/root/springbootx.git
cd springbootx
touch README.md
git add README.md
git commit -m "add README"
git push -u origin master
```

### 4.3 推送现有文件夹
```
cd existing_folder
git init
git remote add origin ssh://git@192.168.1.235:7003/root/springbootx.git
git add .
git commit -m "Initial commit"
git push -u origin master
```

### 4.4 推送到现有的Git存储库
```
cd existing_repo
git remote rename origin old-origin
git remote add origin ssh://git@192.168.1.235:7003/root/springbootx.git
git push -u origin --all
git push -u origin --tags
```

# 5、maven的pom.xml关键配置
```
<!--docke rmaven编译插件-->
<build>
  <finalName>springboot</finalName>
  <plugins>
	<plugin>
		<groupId>com.spotify</groupId>
		<artifactId>docker-maven-plugin</artifactId>
		<version>0.4.12</version>
		<configuration>
			<dockerDirectory>${project.basedir}</dockerDirectory>
			<resources>
				<resource>
					<targetPath>/</targetPath>
					<directory>${project.build.directory}</directory>
					<include>${project.build.finalName}.jar</include>
				</resource>
			</resources>
		</configuration>
	</plugin>
  </plugins>
</build>
```

# 6、profiles的参数
* 以开发环境进行执行运行工程

```
java -jar springboot.jar --spring.profiles.active=dev
```

* 以测试环境进行执行运行工程

```
java -jar springboot.jar --spring.profiles.active=test
```


