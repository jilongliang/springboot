FROM frolvlad/alpine-oraclejdk8:slim

MAINTAINER jilongliang@sina.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /home/jenkins/workspace/springboot_dev

WORKDIR /home/jenkins/workspace/springboot_dev

EXPOSE 7011

ADD ./target/springboot.jar ./

CMD java ${JAVA_OPTS_DEFAULT} -Djava.security.egd=file:/dev/./urandom -jar springboot.jar

