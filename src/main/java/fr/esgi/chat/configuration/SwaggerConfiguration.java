package fr.esgi.chat.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket swaggerApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Chat-api")
                .apiInfo(swaggerApiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo swaggerApiInfo() {
        return new ApiInfoBuilder()
                .title("Chat")
                .description("Some nice REST services in a Spring Boot application")
                .version("1.0.0")
                .build();
    }

    @Bean
    UiConfiguration uiConfig() {
        return new UiConfiguration(
                null,
                "none",
                "alpha",
                "schema",
                UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS,
                false,
                true,
                60000L);
    }
}
