package br.com.leunardomaia.Guitar.Catalog.controller;

import br.com.leunardomaia.Guitar.Catalog.controller.dto.GuitarDto;
import br.com.leunardomaia.Guitar.Catalog.controller.form.GuitarForm;
import br.com.leunardomaia.Guitar.Catalog.controller.service.GuitarService;
import br.com.leunardomaia.Guitar.Catalog.model.Brand;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GuitarController.class)
public class GuitarControllerTest {

    @MockBean
    private GuitarService guitarService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void list_ShouldReturn200AndPage() throws Exception {
        Mockito.when(guitarService.list(any()))
                .thenReturn(ResponseEntity.ok(Page.empty()));

        mockMvc.perform(get("/guitar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content").hasJsonPath());
    }

    @Test
    public void list_ShouldReturn200AndPageableShouldContainTheParams() throws Exception {
        mockMvc.perform(get("/guitar")
                        .param("page", "5")
                        .param("size", "10")
                        .param("sort", "id,asc")
                        .param("sort", "model,desc"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        Mockito.verify(guitarService).list(pageableArgumentCaptor.capture());
        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(5, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
        assertEquals(Sort.by("id").ascending().and(Sort.by("model").descending()),
                pageable.getSort());

    }


    @Test
    public void search_ShouldReturn200AndList_WithParams() throws Exception {
        Mockito.when(guitarService.search(new BigDecimal(5), new BigDecimal(10), "q"))
                .thenReturn(ResponseEntity.ok(List.of()));

        mockMvc.perform(get("/guitar/search")
                        .param("min_price", "5")
                        .param("max_price", "10")
                        .param("q", "q"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void search_ShouldReturn200AndList_WithoutParams() throws Exception {
        Mockito.when(guitarService.search(null, null, null))
                .thenReturn(ResponseEntity.ok(List.of()));

        mockMvc.perform(get("/guitar/search"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }


    @Test
    public void save_ShouldReturn200AndTheGuitarAndUri() throws Exception {
        Mockito.when(guitarService.save(any(), any()))
                .thenReturn(ResponseEntity.created(new URI("/" + createDto().getId())).body(createDto()));

        mockMvc.perform(post("/guitar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createForm())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("model").value(createForm().getModel()))
                .andExpect(header().string("location", "/" + createDto().getId()));
    }

    @Test
    public void save_ShouldReturn400AndTheExceptionResponse_WithoutBody() throws Exception {
        mockMvc.perform(post("/guitar"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code").value("400"));
    }

    @Test
    public void save_ShouldReturn400AndTheExceptionResponse_WithEmptyField() throws Exception {
        mockMvc.perform(post("/guitar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmptyForm())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code").value("400"));
    }

    @Test
    public void save_ShouldReturn400AndTheExceptionResponse_WithBlankField() throws Exception {
        mockMvc.perform(post("/guitar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBlankForm())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code").value("400"));
    }

    @Test
    public void save_ShouldReturn400AndTheExceptionResponse_WithNullField() throws Exception {
        mockMvc.perform(post("/guitar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createNullForm())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code").value("400"));
    }


    @Test
    public void getById_ShouldReturn200AndTheGuitar() throws Exception {
        Mockito.when(guitarService.getById(createDto().getId()))
                .thenReturn(ResponseEntity.ok(createDto()));

        mockMvc.perform(get("/guitar/{id}", createDto().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("model").value(createDto().getModel()));
    }

    @Test
    public void getById_ShouldReturn404AndTheExceptionResponse() throws Exception {
        Mockito.when(guitarService.getById(createDto().getId()))
                .thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/guitar/{id}", createDto().getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("status_code").value("404"));
    }


    @Test
    public void update_ShouldReturn200AndTheGuitar() throws Exception {
        Mockito.when(guitarService.update(eq(createDto().getId()), any()))
                .thenReturn(ResponseEntity.ok(createDto()));

        mockMvc.perform(put("/guitar/{id}", createDto().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createForm())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("model").value(createForm().getModel()));
    }

    @Test
    public void update_ShouldReturn404AndTheExceptionResponse() throws Exception {
        Mockito.when(guitarService.update(eq(createDto().getId()), any()))
                .thenThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/guitar/{id}", createDto().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createForm())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("status_code").value("404"));
    }

    @Test
    public void update_ShouldReturn400AndTheExceptionResponse_WithoutBody() throws Exception {
        mockMvc.perform(put("/guitar/{id}", createDto().getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code").value("400"));
    }

    @Test
    public void update_ShouldReturn400AndTheExceptionResponse_WithEmptyField() throws Exception {
        mockMvc.perform(put("/guitar/{id}", createDto().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmptyForm())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code").value("400"));
    }

    @Test
    public void update_ShouldReturn400AndTheExceptionResponse_WithBlankField() throws Exception {
        mockMvc.perform(put("/guitar/{id}", createDto().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBlankForm())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code").value("400"));
    }

    @Test
    public void update_ShouldReturn400AndTheExceptionResponse_WithNullField() throws Exception {
        mockMvc.perform(put("/guitar/{id}", createDto().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createNullForm())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code").value("400"));
    }


    @Test
    public void delete_ShouldReturn200() throws Exception {
        Mockito.when(guitarService.delete(createDto().getId()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/guitar/{id}", createDto().getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void delete_ShouldReturn404AndTheExceptionResponse() throws Exception {
        Mockito.when(guitarService.delete(createDto().getId()))
                .thenThrow(EmptyResultDataAccessException.class);

        mockMvc.perform(delete("/guitar/{id}", createDto().getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("status_code").value("404"));
    }


    private GuitarDto createDto() {
        return new GuitarDto(1L, "P3DC", new BigDecimal("8499"), new Brand(1L, "Takamine"));
    }

    private GuitarForm createForm() {
        GuitarDto dto = createDto();
        return new GuitarForm(dto.getModel(), dto.getPrice(), dto.getBrand().getName());
    }

    private GuitarForm createEmptyForm() {
        GuitarForm form = createForm();
        form.setModel("");
        return form;
    }

    private GuitarForm createBlankForm() {
        GuitarForm form = createForm();
        form.setModel(" ");
        return form;
    }

    private GuitarForm createNullForm() {
        GuitarForm form = createForm();
        form.setModel(null);
        return form;
    }
}
