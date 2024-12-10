package tn.enova.Configures;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import tn.enova.Filters.UserIdFilter;

@Configuration
public class WebConfig {
    @Bean
    public FilterRegistrationBean<UserIdFilter> getBeanFilterRegistration(){
        FilterRegistrationBean<UserIdFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new UserIdFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
