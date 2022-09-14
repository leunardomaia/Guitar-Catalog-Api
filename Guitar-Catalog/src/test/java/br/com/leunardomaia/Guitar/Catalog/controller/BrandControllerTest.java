package br.com.leunardomaia.Guitar.Catalog.controller;

import br.com.leunardomaia.Guitar.Catalog.controller.dto.BrandDto;
import br.com.leunardomaia.Guitar.Catalog.controller.form.BrandForm;
import br.com.leunardomaia.Guitar.Catalog.controller.service.BrandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BrandController.class)
public class BrandControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private BrandService brandService;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    public void list_ShouldReturn200AndPage() throws Exception {
        when(brandService.list(any()))
                .thenReturn(ResponseEntity.ok(Page.empty()));

        mockMvc.perform(get("/brand"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content").hasJsonPath());
    }

    @Test
    public void list_ShouldReturn200AndPageableShouldContainTheParams() throws Exception {

        mockMvc.perform(get("/brand")
                        .param("page", "5")
                        .param("size", "10")
                        .param("sort", "id,desc")
                        .param("sort", "name,asc"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(brandService).list(pageableArgumentCaptor.capture());
        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(5, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
        assertEquals("name: ASC", pageable.getSort().getOrderFor("name").toString());
        assertEquals("id: DESC", pageable.getSort().getOrderFor("id").toString());

    }


    @Test
    public void save_ShouldReturn201AndTheBrandAndUri() throws Exception {
        when(brandService.save(any(BrandForm.class), any(UriComponentsBuilder.class)))
                .thenReturn(ResponseEntity.created(new URI("/1")).body(createDto()));

        mockMvc.perform(post("/brand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createForm())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name").value(createDto().getName()))
                .andExpect(header().string("location", "/1"));
    }

    @Test
    public void save_ShouldReturn400AndTheExceptionResponse_WithoutBody() throws Exception {
        mockMvc.perform(post("/brand"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code").value(400));
    }

    @Test
    public void save_ShouldReturn400AndTheExceptionResponse_WithEmptyField() throws Exception {
        mockMvc.perform(post("/brand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmptyFieldForm())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code").value(400));
    }

    @Test
    public void save_ShouldReturn400AndTheExceptionResponse_WithoutBlankField() throws Exception {
        mockMvc.perform(post("/brand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBlankFieldForm())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code").value(400));
    }

    @Test
    public void save_ShouldReturn400AndTheExceptionResponse_WithoutNullField() throws Exception {
        mockMvc.perform(post("/brand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createNullFieldForm())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code").value(400));
    }


    @Test
    public void getById_ShouldReturn200AndTheBrand() throws Exception {
        when(brandService.getById(1L))
                .thenReturn(new ResponseEntity<>(createDto(), HttpStatus.OK));

        mockMvc.perform(get("/brand/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(createDto().getName()));
    }

    @Test
    public void getById_ShouldReturn404AndTheExceptionResponse() throws Exception {
        when(brandService.getById(123L))
                .thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/brand/{id}", 123))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("status_code").value(404));
    }


    @Test
    public void update_ShouldReturn200AndTheBrand() throws Exception {
        when(brandService.update(eq(1L), any()))
                .thenReturn(ResponseEntity.ok(createDto()));

        mockMvc.perform(put("/brand/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createForm())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(createDto().getName()));
    }

    @Test
    public void update_ShouldReturn404AndTheExceptionResponse() throws Exception {
        when(brandService.update(eq(123L), any()))
                .thenThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/brand/{id}", 123)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createForm())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("status_code").value(404));
    }

    @Test
    public void update_ShouldReturn400AndTheExceptionResponse_WithoutBody() throws Exception {
        mockMvc.perform(put("/brand/{id}", 1L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code").value(400));
    }

    @Test
    public void update_ShouldReturn400AndTheExceptionResponse_WithEmptyField() throws Exception {
        mockMvc.perform(put("/brand/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmptyFieldForm())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code").value(400));
    }

    @Test
    public void update_ShouldReturn400AndTheExceptionResponse_WithBlankField() throws Exception {
        mockMvc.perform(put("/brand/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBlankFieldForm())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code").value(400));
    }

    @Test
    public void update_ShouldReturn400AndTheExceptionResponse_WithNullField() throws Exception {
        mockMvc.perform(put("/brand/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createNullFieldForm())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code").value(400));
    }


    @Test
    public void delete_ShouldReturn200() throws Exception {
        when(brandService.delete(1L))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/brand/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void delete_ShouldReturn404AndExceptionResponse() throws Exception {
        when(brandService.delete(123L))
                .thenThrow(EmptyResultDataAccessException.class);

        mockMvc.perform(delete("/brand/{id}", 123L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("status_code").value(404));
    }


    private BrandDto createDto() {
        return new BrandDto(1L, "Takamine");
    }

    private BrandForm createForm() {
        BrandDto dto = createDto();
        return new BrandForm(dto.getName());
    }

    private BrandForm createEmptyFieldForm() {
        return new BrandForm("");
    }

    private BrandForm createBlankFieldForm() {
        return new BrandForm("  ");
    }

    private BrandForm createNullFieldForm() {
        return new BrandForm(null);
    }
}
