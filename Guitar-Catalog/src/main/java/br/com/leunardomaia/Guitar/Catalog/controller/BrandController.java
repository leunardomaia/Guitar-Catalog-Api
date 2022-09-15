package br.com.leunardomaia.Guitar.Catalog.controller;

import br.com.leunardomaia.Guitar.Catalog.controller.dto.BrandDto;
import br.com.leunardomaia.Guitar.Catalog.controller.form.BrandForm;
import br.com.leunardomaia.Guitar.Catalog.controller.service.BrandService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
@Api(tags = "Brand")
@AllArgsConstructor
@RestController
@RequestMapping("/brand")
public class BrandController {

    private BrandService service;

    @GetMapping
    @Cacheable(value = "brands")
    public ResponseEntity<Page<BrandDto>> list(Pageable pageable) {
        return service.list(pageable);
    }

    @Transactional
    @PostMapping
    @CacheEvict(value = "brands", allEntries = true)
    public ResponseEntity<BrandDto> save(@RequestBody @Valid BrandForm form, UriComponentsBuilder builder) {
        return service.save(form, builder);
    }

    @GetMapping("/{id}")
    @Cacheable(value = "brands")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @Transactional
    @PutMapping("/{id}")
    @CacheEvict(value = "brands", allEntries = true)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid BrandForm form) {
        return service.update(id, form);
    }

    @Transactional
    @DeleteMapping("/{id}")
    @CacheEvict(value = "brands", allEntries = true)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return service.delete(id);
    }

}
