package com.trading.brokergateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BrokergatewayApplication {

	public static void main(String[] args) {
		Runtime runtime = Runtime.getRuntime();
		try {
			System.out.println( System.getProperty("user.dir"));
			runtime.exec("./redis/redis-server.exe ./redis/redis.windows.conf");
		}
		catch (Exception e){
			System.out.println(e.toString());
		}
		SpringApplication.run(BrokergatewayApplication.class, args);
	}

}
