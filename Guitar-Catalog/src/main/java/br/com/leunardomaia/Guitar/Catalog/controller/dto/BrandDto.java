package br.com.leunardomaia.Guitar.Catalog.controller.dto;

import br.com.leunardomaia.Guitar.Catalog.model.Brand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class BrandDto {

    private Long id;
    private String name;

    public static Page<BrandDto> convertPage(Page<Brand> brands) {
        return brands.map(brand -> new BrandDto(brand.getId(), brand.getName()));
    }

    public static BrandDto convert(Brand brand) {
        return new BrandDto(brand.getId(), brand.getName());
    }
}
