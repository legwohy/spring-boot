package com.cobra.filter.response;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean testFilterRegistration() {


        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new ResponseFilter());
        registration.addUrlPatterns("/userInfo/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("responseFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public ResponseFilter responseFilter(){
        return new ResponseFilter();
    }

    @Bean(value = "testFilterAutoBean")
    public TestFilterAutoBean get(){
        return new TestFilterAutoBean(1,"jack");
    }
}
