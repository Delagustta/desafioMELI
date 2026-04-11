package com.hackerrank.sample.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.sample.dto.ProductResponse;
import com.hackerrank.sample.service.ProductService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("GET /products delegates to service and returns JSON array")
    void getAllProducts() throws Exception {
        List<ProductResponse> body = List.of(
                new ProductResponse(1L, "A", "u", "d", new BigDecimal("1"), 4.0, Map.of())
        );
        when(productService.getAllProducts()).thenReturn(body);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("A"));

        verify(productService).getAllProducts();
    }

    @Test
    @DisplayName("GET /products/{id} returns single product")
    void getProductById() throws Exception {
        ProductResponse one = new ProductResponse(10L, "X", "img", "desc", new BigDecimal("99.99"), 4.2, Map.of("k", "v"));
        when(productService.getProductById(10L)).thenReturn(one);

        mockMvc.perform(get("/products/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("X"));

        verify(productService).getProductById(10L);
    }

    @Test
    @DisplayName("POST /products/compare forwards ids to service")
    void compareProducts() throws Exception {
        List<Long> ids = List.of(101L, 102L);
        List<ProductResponse> responses = List.of(
                new ProductResponse(101L, "P1", "i1", "d1", BigDecimal.ONE, 5.0, Map.of()),
                new ProductResponse(102L, "P2", "i2", "d2", BigDecimal.TEN, 4.0, Map.of())
        );
        when(productService.compareProducts(ids)).thenReturn(responses);

        String json = objectMapper.writeValueAsString(Map.of("ids", ids));

        mockMvc.perform(post("/products/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(101))
                .andExpect(jsonPath("$[1].id").value(102));

        verify(productService).compareProducts(eq(ids));
    }
}
