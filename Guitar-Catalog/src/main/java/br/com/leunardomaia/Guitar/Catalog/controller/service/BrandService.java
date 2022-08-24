package br.com.leunardomaia.Guitar.Catalog.controller.service;

import br.com.leunardomaia.Guitar.Catalog.controller.dto.BrandDto;
import br.com.leunardomaia.Guitar.Catalog.controller.form.BrandForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

public interface BrandService {

    ResponseEntity<Page<BrandDto>> list(Pageable pageable);
    ResponseEntity<BrandDto> save(BrandForm form, UriComponentsBuilder builder);
    ResponseEntity<BrandDto> getById(Long id);
    ResponseEntity<BrandDto> update(Long id, BrandForm form);
    ResponseEntity<?> delete(Long id);

}
