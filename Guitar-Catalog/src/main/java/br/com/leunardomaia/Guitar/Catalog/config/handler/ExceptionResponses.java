package br.com.leunardomaia.Guitar.Catalog.config.handler;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@Getter
@AllArgsConstructor
public class ExceptionResponses {
    private int status_code;
    private List<String> messages;

}
