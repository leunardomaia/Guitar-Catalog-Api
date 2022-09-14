package br.com.leunardomaia.Guitar.Catalog.controller.form;

import br.com.leunardomaia.Guitar.Catalog.model.Brand;
import br.com.leunardomaia.Guitar.Catalog.model.Guitar;
import br.com.leunardomaia.Guitar.Catalog.repository.BrandRepository;
import br.com.leunardomaia.Guitar.Catalog.repository.GuitarRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GuitarForm {
    @NotNull
    @NotBlank
    @NotEmpty
    private String model;
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;
    @NotNull
    @NotBlank
    @NotEmpty
    private String brandName;

    public Guitar toEntity(BrandRepository repository) {
        Brand brand = repository.findByName(brandName);
        if (brand == null){
            return new Guitar(null, model, price, repository.save(new Brand(null, brandName)));
        }
        return new Guitar(null, model, price, brand);
    }

    public Guitar update(GuitarRepository repository, Long id){
        Guitar guitar = repository.getReferenceById(id);
        guitar.setModel(model);
        guitar.setPrice(price);
        return guitar;
    }
}
