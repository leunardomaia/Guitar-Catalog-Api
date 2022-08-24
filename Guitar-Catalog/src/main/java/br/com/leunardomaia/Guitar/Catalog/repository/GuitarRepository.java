package br.com.leunardomaia.Guitar.Catalog.repository;

import br.com.leunardomaia.Guitar.Catalog.model.Guitar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface GuitarRepository extends JpaRepository<Guitar, Long> {

    @Query(value = "SELECT * FROM Guitar JOIN Brand ON Guitar.brand_id = Brand.id "
                    + "WHERE ( :q IS NULL OR UPPER(Guitar.model) LIKE UPPER(CONCAT('%', :q, '%')) "
                    + "OR UPPER(Brand.name) LIKE UPPER(CONCAT('%', :q, '%')) ) "
                    + "AND ( Guitar.price >= :min_price OR :min_price IS NULL) "
                    + "AND ( Guitar.price <= :max_price OR :max_price IS NULL) ", nativeQuery = true)
    List<Guitar> findByParams(@Param("min_price") BigDecimal min_price,
                              @Param("max_price") BigDecimal max_price,
                              @Param("q") String q);

}
