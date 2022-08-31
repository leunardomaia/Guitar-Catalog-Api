package br.com.leunardomaia.Guitar.Catalog.controller.service;

import br.com.leunardomaia.Guitar.Catalog.controller.dto.BrandDto;
import br.com.leunardomaia.Guitar.Catalog.controller.form.BrandForm;
import br.com.leunardomaia.Guitar.Catalog.model.Brand;
import br.com.leunardomaia.Guitar.Catalog.repository.BrandRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class BrandServiceImpTest {

    BrandRepository brandRepository;
    BrandService brandService;

    public BrandServiceImpTest() {
        brandRepository = Mockito.mock(BrandRepository.class);
        brandService = new BrandServiceImp(brandRepository);
    }


    //list
    @Test
    public void list_ShouldReturn200AndPage() {
        when(brandRepository.findAll(pageable()))
                .thenReturn(page());

        ResponseEntity<Page<BrandDto>> response =
                brandService.list(pageable());

        assertEquals(200, response.getStatusCode().value());
        assertEquals(PageImpl.class, response.getBody().getClass());
    }


    //save
    @Test
    public void save_ShouldReturn201AndUriAndBrand() {
        when(brandRepository.save(form().toEntity()))
                .thenReturn(brand());

        ResponseEntity<BrandDto> response =
                brandService.save(form(), UriComponentsBuilder.newInstance());

        assertEquals(201, response.getStatusCode().value());
        assertEquals("/brand/" + brand().getId(),
                response.getHeaders().getLocation().getPath());
        assertEquals(form().getName(), response.getBody().getName());
    }


    //getById
    @Test
    public void getById_ShouldReturn200AndBrand() {

        when(brandRepository.getReferenceById(brand().getId()))
                .thenReturn(brand());

        ResponseEntity<BrandDto> response =
                brandService.getById(brand().getId());

        assertEquals(200, response.getStatusCode().value());
        assertEquals(brand().getName(), response.getBody().getName());
    }

    @Test
    public void getById_ShouldThrowEntityNotFoundException() {
        when(brandRepository.getReferenceById(nonexistentId()))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> brandService.getById(nonexistentId()));
    }


    //update
    @Test
    public void update_ShouldReturn200AndUpdatedBrand(){
        when(brandRepository.getReferenceById(brand().getId()))
                .thenReturn(brand());

        ResponseEntity<BrandDto> response =
                brandService.update(brand().getId(), updateForm());

        assertEquals(200, response.getStatusCode().value());
        assertEquals(updateForm().getName(), response.getBody().getName());
    }



    @Test
    public void update_ShouldThrowEntityNotFoundException() {
        when(brandRepository.getReferenceById(nonexistentId()))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> brandService.update(nonexistentId(), updateForm()));
    }

    //delete
    @Test
    public void delete_ShouldCallRepositoryAndReturn200(){
        ResponseEntity<?> response = brandService.delete(brand().getId());

        verify(brandRepository).deleteById(brand().getId());
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void delete_ShouldThrowEmptyResultDataAccessException() {
        doThrow(EmptyResultDataAccessException.class)
                .when(brandRepository).deleteById(nonexistentId());

        assertThrows(EmptyResultDataAccessException.class,
                () -> brandService.delete(nonexistentId()));
    }


    //private methods
    private Page<Brand> page() {
        return new PageImpl<>(Arrays.asList(brand(), brand(), brand()),
                pageable(), 3);
    }
    private PageRequest pageable() {
        return PageRequest.of(0, 3);
    }
    private Brand brand() {
        return new Brand(1L, "name");
    }
    private BrandForm form() {
        return new BrandForm("name");
    }
    private BrandForm updateForm() {
        return new BrandForm("updated");
    }
    private Long nonexistentId(){
        return 123L;
    }
}
