package br.com.leunardomaia.Guitar.Catalog.controller.service;

import br.com.leunardomaia.Guitar.Catalog.controller.dto.GuitarDto;
import br.com.leunardomaia.Guitar.Catalog.controller.form.GuitarForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;

public interface GuitarService {

    ResponseEntity<Page<GuitarDto>> list(Pageable pageable);

    ResponseEntity<List<GuitarDto>> search(BigDecimal min_price, BigDecimal max_price, String q);
    ResponseEntity<GuitarDto> save(GuitarForm form, UriComponentsBuilder builder);
    ResponseEntity<GuitarDto> getById(Long id);
    ResponseEntity<GuitarDto> update(Long id, GuitarForm form);
    ResponseEntity<?> delete(Long id);
}
