package br.com.leunardomaia.Guitar.Catalog.controller.service;

import br.com.leunardomaia.Guitar.Catalog.controller.dto.GuitarDto;
import br.com.leunardomaia.Guitar.Catalog.controller.form.GuitarForm;
import br.com.leunardomaia.Guitar.Catalog.model.Guitar;
import br.com.leunardomaia.Guitar.Catalog.repository.BrandRepository;
import br.com.leunardomaia.Guitar.Catalog.repository.GuitarRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@Service
public class GuitarServiceImp implements GuitarService {

    GuitarRepository guitarRepository;
    BrandRepository brandRepository;

    GuitarServiceImp(GuitarRepository guitarRepository, BrandRepository brandRepository){
        this.guitarRepository = guitarRepository;
        this.brandRepository = brandRepository;
    }

    @Override
    public ResponseEntity<Page<GuitarDto>> list(Pageable pageable) {
        var guitars = guitarRepository.findAll(pageable);
        return ResponseEntity.ok(GuitarDto.convertPage(guitars));
    }

    @Override
    public ResponseEntity<List<GuitarDto>> search(BigDecimal min_price, BigDecimal max_price, String q) {
       var guitars = guitarRepository.findByParams(min_price, max_price, q);
       return ResponseEntity.ok(GuitarDto.convertList(guitars));
    }


    @Override
    public ResponseEntity<GuitarDto> save(GuitarForm form, UriComponentsBuilder builder) {
        Guitar guitar = guitarRepository.save(form.toEntity(brandRepository));
        URI uri = builder.path("/guitar/{id}").buildAndExpand(guitar.getId()).toUri();
        return ResponseEntity.created(uri).body(GuitarDto.convert(guitar));
    }

    @Override
    public ResponseEntity<GuitarDto> getById(Long id) {
        var guitar = guitarRepository.getReferenceById(id);
        return ResponseEntity.ok(GuitarDto.convert(guitar));
    }

    @Override
    public ResponseEntity<GuitarDto> update(Long id, GuitarForm form) {
        Guitar guitar = form.update(guitarRepository, id);
        return ResponseEntity.ok(GuitarDto.convert(guitar));
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        guitarRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
