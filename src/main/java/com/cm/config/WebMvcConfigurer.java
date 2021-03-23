package com.cm.config;

import com.cm.interceptor.UserDateInterceptor;
import com.cm.interceptor.UserInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


@Configuration  //适配器
public class WebMvcConfigurer extends WebMvcConfigurationSupport {

	// redis用
	@Bean
    public UserInterceptor getUserInterceptor() {
        return new UserInterceptor();
    }
	// 数据库用
	@Bean
    public UserDateInterceptor getUserDateInterceptor() {
        return new UserDateInterceptor();
    }

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		/**
		 * 拦截器按照顺序执行
		 */
		registry.addInterceptor(getUserDateInterceptor()).excludePathPatterns("/user/login");
		super.addInterceptors(registry);
	}
}