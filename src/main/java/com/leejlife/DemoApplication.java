package com.leejlife;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    /**
	 * 启动mq
	 * docker stop rmqnamesrv
	 * docker start rmqnamesrv
	 * docker stop rmqbroker
	 * docker start rmqbroker
	 *
	 *
	 * 1、docker run -d -p 9876:9876 -v /Users/jingli/rocketmq/data/namesrv/logs:/root/logs -v /Users/jingli/rocketmq/data/namesrv/store:/root/store --name rmqnamesrv -e "MAX_POSSIBLE_HEAP=100000000" rocketmqinc/rocketmq:4.4.0 sh mqnamesrv
	 * 2、docker run -d -p 10911:10911 -p 10909:10909 -v  /Users/jingli/rocketmq/data/broker/logs:/root/logs -v  /Users/jingli/rocketmq/rocketmq/data/broker/store:/root/store -v  /Users/jingli/rocketmq/conf/broker.conf:/opt/rocketmq-4.4.0/conf/broker.conf --name rmqbroker --link rmqnamesrv:namesrv -e "NAMESRV_ADDR=namesrv:9876" -e "MAX_POSSIBLE_HEAP=200000000" rocketmqinc/rocketmq:4.4.0 sh mqbroker -c /opt/rocketmq-4.4.0/conf/broker.conf
	 * 3、docker run -e "JAVA_OPTS=-Drocketmq.namesrv.addr=10.12.172.111:9876 -Dcom.rocketmq.sendMessageWithVIPChannel=false" -p 8080:8080 -t pangliang/rocketmq-console-ng
	 * @param args
     */
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
