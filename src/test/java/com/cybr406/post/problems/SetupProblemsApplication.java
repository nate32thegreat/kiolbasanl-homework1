package com.cybr406.post.problems;

import com.cybr406.post.PostApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Profile;

@Profile("setup")
@SpringBootApplication(exclude = { JpaRepositoriesAutoConfiguration.class, WebMvcAutoConfiguration.class })
public class SetupProblemsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostApplication.class, args);
    }

}
