#!/usr/bin/env bash
# 动态变量的【等号】不能有空格和tab键置位,否则获取不了值，而且在shell脚本代码里面不支持空格格式化，支持tab置位格式化。
# 在终端(ssh软件端)或Jenkins客户端shell命令,参数以空格隔开。如：sh build.sh 192.168.1.235 springboot 0.0.1 7011 /home/jenkins/workspace/springboot_dev 
IMG_SERVER="$1"
IMG_NAME="$2"
IMG_VERSION="$3"
IMG_PORT="$4"
RUN_EVN="$5"
IMG_PATH="$6"

echo "服务地址：$IMG_SERVER"
echo "工程镜像名称：$IMG_NAME"
echo "工程版本号：$IMG_VERSION"
echo "工程端口：$IMG_PORT"
echo "服务环境：$RUN_EVN"

echo "create $IMG_PATH"
mkdir -p $IMG_PATH

#私服访问url路径和编译之后镜像文件存放到指定路径固定,不动态参数进行处理传值.
REGISTRY_URL="192.168.1.235:5000"
IMG_TAR_GZ_PATH="/home/img_tar_gz_path/"

# 判断动态参数不为空字符串的时候才执行下面操作
if [ "$IMG_SERVER" != "" ] && [ "$IMG_NAME" != "" ] && [ "$IMG_VERSION" != "" ] && [ "$IMG_PORT" != "" ]; then
	
	echo " .......进入删除  Container & Images 操作 ......."
	docker rm -f $(docker ps -a | grep $IMG_NAME | awk '{ print $1 }')
	docker rmi $(docker images | grep $IMG_NAME | awk '{ print $3 }')
	
	# $IMG_NAME:$IMG_VERSION 这个IMG_VERSION版本(tag)参数不指定默认latest,通过不同参数执行不同环境文件
	# -f 表示强制指定Dockerfile文件进行编译
	
	echo " .......进入Building & Images 操作 ....... "
	
	#方法1、指定不同文件存放默认的Dockerfile，使用-f进行强制编译
	#docker build -t $IMG_NAME:$IMG_VERSION -f $IMG_PATH"env/"$RUN_EVN/Dockerfile $IMG_PATH
	
	#方法2、跟据不同Dockerfile文件的后缀进行编译不同环境的文件
	docker build -t $IMG_NAME:$IMG_VERSION -f $IMG_PATH"env/"Dockerfile_$RUN_EVN $IMG_PATH
	
	
	# 将镜像打一下标签，然后安照标签进行推送到私服里面，标签名就以服务名即可
	docker tag $IMG_NAME:$IMG_VERSION $REGISTRY_URL/$IMG_NAME:$IMG_VERSION
	
	# 推镜像到私服里面
	docker push $REGISTRY_URL/$IMG_NAME:$IMG_VERSION
	
	# 创建目录
	mkdir -p $IMG_TAR_GZ_PATH
	# 保存编译之后镜像文件存放到指定路径
	docker save $IMG_NAME -o $IMG_TAR_GZ_PATH/$IMG_NAME.tar.gz
	
	echo " .......进入Runing操作 ....."
	docker run -d --network default_network --restart=always --env-file=./.env  -e spring.profiles.active=$RUN_EVN --expose=$IMG_PORT --name=$IMG_NAME  -p $IMG_PORT:$IMG_PORT $IMG_NAME:$IMG_VERSION

	echo " .......Build & Run Finish Success~...."
else
	echo " .......Illegal Command Operation ......."
fi

