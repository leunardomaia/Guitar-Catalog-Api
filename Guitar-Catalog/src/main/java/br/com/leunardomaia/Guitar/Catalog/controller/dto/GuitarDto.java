package br.com.leunardomaia.Guitar.Catalog.controller.dto;

import br.com.leunardomaia.Guitar.Catalog.model.Brand;
import br.com.leunardomaia.Guitar.Catalog.model.Guitar;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class GuitarDto {

    private Long id;
    private String model;
    private BigDecimal price;
    private Brand brand;

    public static Page<GuitarDto> convertPage(Page<Guitar> guitars){
        return guitars.map(guitar -> new GuitarDto(guitar.getId(), guitar.getModel(), guitar.getPrice(), guitar.getBrand()));
    }

    public static GuitarDto convert(Guitar guitar) {
        return new GuitarDto(guitar.getId(), guitar.getModel(), guitar.getPrice(), guitar.getBrand());
    }

    public static List<GuitarDto> convertList(List<Guitar> guitars) {
        return guitars.stream()
                .map(guitar -> new GuitarDto(guitar.getId(), guitar.getModel(), guitar.getPrice(), guitar.getBrand()))
                .collect(Collectors.toList());
    }
}
