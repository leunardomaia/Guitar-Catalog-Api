package br.com.leunardomaia.Guitar.Catalog.controller.service;

import br.com.leunardomaia.Guitar.Catalog.controller.dto.GuitarDto;
import br.com.leunardomaia.Guitar.Catalog.model.Guitar;
import br.com.leunardomaia.Guitar.Catalog.repository.BrandRepository;
import br.com.leunardomaia.Guitar.Catalog.repository.GuitarRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.Context;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GuitarServiceImpTest {

    GuitarRepository guitarRepository;
    BrandRepository brandRepository;
    GuitarService guitarService;

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
        when(guitarRepository.findByParams(any(), any(), any()))
                .thenReturn(List.of());

        //assertEquals
    }

    //save
    @Test
    public void save_ShouldReturn200AndUriAndTheGuitar(){
        //One repository returns an instance to the other one
    }


    //getById
    @Test
    public void getById_ShouldReturn200AndTheGuitar(){

    }

    @Test
    public void getById_ShouldThrowSome404Exception(){
        //change method name
    }

    //update
    @Test
    public void update_ShouldReturn200AndTheGuitarUpdated(){

    }

    @Test
    public void update_ShouldReturnSome404Exception(){
        //change method name
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
}
