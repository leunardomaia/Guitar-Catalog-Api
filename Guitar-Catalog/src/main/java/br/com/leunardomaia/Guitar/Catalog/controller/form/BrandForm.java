package br.com.leunardomaia.Guitar.Catalog.controller.form;

import br.com.leunardomaia.Guitar.Catalog.model.Brand;
import br.com.leunardomaia.Guitar.Catalog.repository.BrandRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandForm {

    @NotNull
    @NotBlank
    @NotEmpty
    private String name;

    public Brand toEntity() {
        return new Brand(null, name);
    }

    public Brand update(BrandRepository repository, Long id) {
        Brand brand = repository.getReferenceById(id);
        brand.setName(name);
        return brand;
    }
}
