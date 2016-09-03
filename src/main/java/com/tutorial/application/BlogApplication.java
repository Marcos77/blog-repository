package com.tutorial.application;

import javax.inject.Named;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
//@EnableJpaRepositories(basePackageClasses = BlogRepository.class)
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}
        

        @Named
        static class JerseyConfig extends ResourceConfig {
            public JerseyConfig() {
               this.packages("com.tutorial.application.services");
               register(MultiPartFeature.class);
            }
            
        }
            
}
