package br.com.leunardomaia.Guitar.Catalog.config.handler;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponse {
    private int status_code;
    private String message;

}
