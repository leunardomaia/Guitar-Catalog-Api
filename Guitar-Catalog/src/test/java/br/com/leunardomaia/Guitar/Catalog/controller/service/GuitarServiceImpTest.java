package br.com.leunardomaia.Guitar.Catalog.controller.service;

import br.com.leunardomaia.Guitar.Catalog.controller.dto.GuitarDto;
import br.com.leunardomaia.Guitar.Catalog.controller.form.GuitarForm;
import br.com.leunardomaia.Guitar.Catalog.model.Brand;
import br.com.leunardomaia.Guitar.Catalog.model.Guitar;
import br.com.leunardomaia.Guitar.Catalog.repository.BrandRepository;
import br.com.leunardomaia.Guitar.Catalog.repository.GuitarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class GuitarServiceImpTest {

    private GuitarRepository guitarRepository;
    private BrandRepository brandRepository;
    private GuitarService guitarService;

    @BeforeEach
    void setup(){
        guitarRepository = Mockito.mock(GuitarRepository.class);
        brandRepository = Mockito.mock(BrandRepository.class);
        guitarService = new GuitarServiceImp(guitarRepository, brandRepository);
    }


    //list
    @Test
     public void list_ShouldReturn200AndPage(){
        when(guitarRepository.findAll(Pageable.unpaged()))
                .thenReturn(Page.empty());

        ResponseEntity<Page<GuitarDto>> response =
                guitarService.list(Pageable.unpaged());

        assertEquals(200, response.getStatusCode().value());
        assertEquals(Page.empty().getClass(), response.getBody().getClass());
    }

    //search
    @Test
    public void search_ShouldReturn200AndList(){
        BigDecimal min_price = new BigDecimal("30");
        BigDecimal max_price = new BigDecimal("60");
        String q = "q";

        when(guitarRepository.findByParams(min_price, max_price, q))
                .thenReturn(List.of());

        ResponseEntity<List<GuitarDto>> response =
                guitarService.search(min_price, max_price, q);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("[]", response.getBody().toString());
    }
    @Test
    public void search_ShouldReturn200AndList_WithNullParams(){
        when(guitarRepository.findByParams(null, null, null))
                .thenReturn(List.of());

        ResponseEntity<List<GuitarDto>> response =
                guitarService.search(null, null, null);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("[]", response.getBody().toString());
    }


    //save
    @Test
    public void save_ShouldReturn201AndUriAndTheGuitar() {
        Brand brand = createBrand();
        Guitar guitar = createGuitar();
        GuitarForm form = createForm();
        Guitar guitarWithoutId = createGuitarWithoutId();

        when(brandRepository.findByName(form.getBrandName()))
                .thenReturn(brand);

        when(guitarRepository.save(guitarWithoutId))
                .thenReturn(guitar);

        ResponseEntity<GuitarDto> response =
                guitarService.save(form, UriComponentsBuilder.newInstance());

        assertEquals(201, response.getStatusCode().value());
        assertEquals("/guitar/" + guitar.getId(), response.getHeaders().getLocation().toString());
        assertEquals(guitar.getModel(), response.getBody().getModel());

    }

    @Test
    public void save_ShouldReturn201AndUriAndTheGuitar_withoutSavedBrand(){
        Brand brand = createBrand();
        Guitar guitar = createGuitar();
        GuitarForm form = createForm();
        Guitar guitarWithoutId = createGuitarWithoutId();
        Brand brandWithoutId = createBrandWithoutId();

        when(brandRepository.findByName(form.getBrandName()))
                .thenReturn(null);

        when(brandRepository.save(brandWithoutId))
                .thenReturn(brand);

        when(guitarRepository.save(guitarWithoutId))
                .thenReturn(guitar);

        ResponseEntity<GuitarDto> response =
                guitarService.save(form, UriComponentsBuilder.newInstance());

        assertEquals(201, response.getStatusCode().value());
        assertEquals("/guitar/" + guitar.getId(), response.getHeaders().getLocation().toString());
        assertEquals(guitar.getModel(), response.getBody().getModel());
    }


    //getById
    @Test
    public void getById_ShouldReturn200AndTheGuitar(){
        Guitar guitar = createGuitar();
        Long id = guitar.getId();

        when(guitarRepository.getReferenceById(id))
                .thenReturn(guitar);

        ResponseEntity<GuitarDto> response = guitarService.getById(id);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(guitar.getModel(), response.getBody().getModel());
    }

    @Test
    public void getById_ShouldThrowEntityNotFoundException(){
        Long id = createGuitar().getId();

        when(guitarRepository.getReferenceById(id))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> guitarService.getById(id));
    }

    //update
    @Test
    public void update_ShouldReturn200AndTheGuitarUpdated(){
        Guitar guitar = createGuitar();
        Long id = guitar.getId();
        GuitarForm updateForm = createUpdateForm();

        when(guitarRepository.getReferenceById(id))
                .thenReturn(guitar);

        ResponseEntity<GuitarDto> response = guitarService.update(id, updateForm);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(updateForm.getModel(), response.getBody().getModel());
    }

    @Test
    public void update_ShouldThrowEntityNotFoundException(){
        Long id = createGuitar().getId();
        GuitarForm updateForm = createUpdateForm();

        when(guitarRepository.getReferenceById(id))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> guitarService.update(id, updateForm));
    }


    //delete
    @Test
    public void delete_ShouldReturn200(){

    }

    @Test
    public void delete_ShouldReturnSome404Exception(){
        //change method name
    }

    //private methods
    private Brand createBrand() {
        return new Brand(1L, "Takamine");
    }

    private Brand createBrandWithoutId() {
        Brand brand = createBrand();
        brand.setId(null);
        return brand;
    }
    private Guitar createGuitar() {
        Brand brand = createBrand();
        return new Guitar(1L, "E220L", new BigDecimal("1233"), brand);
    }
    private Guitar createGuitarWithoutId() {
        Guitar guitar = createGuitar();
        guitar.setId(null);
        return guitar;
    }
     private GuitarForm createForm() {
         Guitar guitar = createGuitar();
         return new GuitarForm(guitar.getModel(), guitar.getPrice(),guitar.getBrand().getName());
    }

    private GuitarForm createUpdateForm() {
        GuitarForm form = createForm();
        form.setModel("updated");
        return form;
    }
}
