package br.com.leunardomaia.Guitar.Catalog.config.swagger;

import br.com.leunardomaia.Guitar.Catalog.model.Brand;
import br.com.leunardomaia.Guitar.Catalog.model.Guitar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig {

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.leunardomaia.Guitar.Catalog"))
                .paths(PathSelectors.ant("/**"))
                .build()
                .ignoredParameterTypes(Guitar.class, Brand.class, Sort.class)
                .directModelSubstitute(Pageable.class, SwaggerPageable.class)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Guitar Catalog")
                .description("A REST API Spring Boot application example")
                .version("1.0")
                .contact(new Contact("Leonardo Ferreira Maia",
                        "https://github.com/leunardomaia/",
                        "leof.maia88@gmail.com"))
                .build();
    }
}
