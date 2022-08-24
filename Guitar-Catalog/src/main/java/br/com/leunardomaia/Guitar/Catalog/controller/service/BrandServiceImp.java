package br.com.leunardomaia.Guitar.Catalog.controller.service;

import br.com.leunardomaia.Guitar.Catalog.controller.dto.BrandDto;
import br.com.leunardomaia.Guitar.Catalog.controller.form.BrandForm;
import br.com.leunardomaia.Guitar.Catalog.model.Brand;
import br.com.leunardomaia.Guitar.Catalog.repository.BrandRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class BrandServiceImp implements BrandService {

    BrandRepository repository;

    public BrandServiceImp(BrandRepository repository){
        this.repository = repository;
    }

    @Override
    public ResponseEntity<Page<BrandDto>> list(Pageable pageable) {
        var brands = repository.findAll(pageable);
        return ResponseEntity.ok(BrandDto.convertPage(brands));
    }

    @Override
    public ResponseEntity<BrandDto> save(BrandForm form, UriComponentsBuilder builder) {
        Brand brand = repository.save(form.toEntity());
        URI uri = builder.path("/brand/{id}").buildAndExpand(brand.getId()).toUri();
        return ResponseEntity.created(uri).body(BrandDto.convert(brand));
    }

    @Override
    public ResponseEntity<BrandDto> getById(Long id) {
        Brand brand = repository.getReferenceById(id);
        return new ResponseEntity<>(BrandDto.convert(brand), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BrandDto> update(Long id, BrandForm form) {
        Brand brand = form.update(repository, id);
        return ResponseEntity.ok(BrandDto.convert(brand));
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        repository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
