package br.com.leunardomaia.Guitar.Catalog.config.swagger;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
class SwaggerPageable {

    @ApiParam(value = "Page to be loaded", example = "0")
    @Nullable
    private Integer page;

    @ApiParam(value = "Number of items per page", example = "10")
    @Nullable
    private Integer size;

    @ApiParam(value = "Sort list by parameter", example = "id")
    @Nullable
    private String sort;

}
