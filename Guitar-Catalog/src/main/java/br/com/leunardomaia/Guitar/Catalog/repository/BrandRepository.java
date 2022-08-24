package br.com.leunardomaia.Guitar.Catalog.repository;

import br.com.leunardomaia.Guitar.Catalog.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findByName(String brandName);
}
