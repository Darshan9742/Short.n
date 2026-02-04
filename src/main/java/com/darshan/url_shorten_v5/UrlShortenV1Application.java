package com.darshan.url_shorten_v5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAspectJAutoProxy
@SpringBootApplication
public class UrlShortenV1Application {

	public static void main(String[] args) {
		SpringApplication.run(UrlShortenV1Application.class, args);
	}

}
