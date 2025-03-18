package epam.gymcrm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public MappingJackson2HttpMessageConverter jsonMessageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }

    @Override
    public void configureMessageConverters(List converters) {
        converters.add(jsonMessageConverter());
    }
}
