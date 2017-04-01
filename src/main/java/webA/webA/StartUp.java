package webA.webA;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;

import webA.webA.filter.Myfilter;

@EnableAutoConfiguration
@ComponentScan("webA.webA")
public class StartUp {

	@Autowired
	private StringRedisTemplate template;
	
	@Value("${SSO.url}")
	public String SSOUrl;
	
	public static void main(String[] args) throws Exception {
		
		SpringApplication.run(StartUp.class, args);
	}
	
	@EventListener({ContextStartedEvent.class, ContextRefreshedEvent.class})
	public void handleContextStart() {
		System.out.println(SSOUrl);
		System.setProperty("SSO.url",SSOUrl);
	}
	
	@Bean  
    public FilterRegistrationBean filterDemo3Registration() {  
        FilterRegistrationBean registration = new FilterRegistrationBean();  
        registration.setFilter(new Myfilter(template));  
        registration.addUrlPatterns("/*");  
//        registration.addInitParameter("paramName", "paramValue");  
        registration.setName("Myfilter");  
        registration.setOrder(1);  
        return registration;  
    }


	
	
}