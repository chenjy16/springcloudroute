package com.meiduimall.mzfrouter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;


@EnableZuulProxy
@SpringBootApplication
public class RouterApplication {

	public static void main(String[] args) {
		//System.setProperty("spring.config.name", "ms-route");
		SpringApplication.run(RouterApplication.class, args);
	}
}
