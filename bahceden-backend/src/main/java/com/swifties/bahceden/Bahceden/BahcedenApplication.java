package com.swifties.bahceden.Bahceden;

import com.swifties.bahceden.Bahceden.config.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Import(WebConfig.class)
public class BahcedenApplication {
	public static void main(String[] args) {
		SpringApplication.run(BahcedenApplication.class, args);
	}
}
