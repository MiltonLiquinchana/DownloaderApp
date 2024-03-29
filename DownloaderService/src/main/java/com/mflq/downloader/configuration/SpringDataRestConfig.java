package com.mflq.downloader.configuration;

import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;



@Component
public class SpringDataRestConfig implements RepositoryRestConfigurer {

  @Override
  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

    cors.addMapping("/api/**")
//      .allowedOrigins("http://localhost:5173")
      .allowedOrigins("*")
      .allowedMethods("*")
      .allowCredentials(false).maxAge(3600);
  }
}
