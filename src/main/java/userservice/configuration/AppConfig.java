package userservice.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import userservice.component.filter.RequestLoggingFilter;

@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterRegistrationBean<RequestLoggingFilter> filterRegistrationBean() {
        FilterRegistrationBean<RequestLoggingFilter> registrationBean = new FilterRegistrationBean();
        RequestLoggingFilter requestLoggingFilter = new RequestLoggingFilter();
        registrationBean.setFilter(requestLoggingFilter);
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
