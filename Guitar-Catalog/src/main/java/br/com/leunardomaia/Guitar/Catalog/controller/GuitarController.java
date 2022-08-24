package br.com.leunardomaia.Guitar.Catalog.controller;

import br.com.leunardomaia.Guitar.Catalog.controller.dto.GuitarDto;
import br.com.leunardomaia.Guitar.Catalog.controller.form.GuitarForm;
import br.com.leunardomaia.Guitar.Catalog.controller.service.GuitarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
@Api(tags = "Guitar")
@RestController
@RequestMapping("/guitar")
public class GuitarController {

    GuitarService service;

    GuitarController(GuitarService service) {
        this.service = service;
    }

    @GetMapping
    @Cacheable(value = "guitars")
    public ResponseEntity<Page<GuitarDto>> list(Pageable pageable) {
        return service.list(pageable);
    }

    @GetMapping("/search")
    @Cacheable(value = "guitars")
    public ResponseEntity<List<GuitarDto>> search(@RequestParam(required = false) @ApiParam(value = "Minimum price") BigDecimal min_price,
                                                  @RequestParam(required = false) @ApiParam(value = "Maximum price") BigDecimal max_price,
                                                  @RequestParam(required = false) @ApiParam(value = "Type your search here") String q) {
        return service.search(min_price, max_price, q);
    }

    @Transactional
    @PostMapping
    @CacheEvict(value = "guitars", allEntries = true)
    public ResponseEntity<GuitarDto> save(@RequestBody @Valid GuitarForm form, UriComponentsBuilder builder) {
        return service.save(form, builder);
    }

    @GetMapping("/{id}")
    @Cacheable(value = "guitars")
    public ResponseEntity<GuitarDto> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @Transactional
    @PutMapping("/{id}")
    @CacheEvict(value = "guitars", allEntries = true)
    public ResponseEntity<GuitarDto> update(@PathVariable Long id, @RequestBody @Valid GuitarForm form) {
        return service.update(id, form);
    }

    @Transactional
    @DeleteMapping("/{id}")
    @CacheEvict(value = "guitars", allEntries = true)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
