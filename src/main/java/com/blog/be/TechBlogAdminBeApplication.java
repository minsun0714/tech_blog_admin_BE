package com.blog.be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TechBlogAdminBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechBlogAdminBeApplication.class, args);
    }
}
