package com.hackerrank.sample.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hackerrank.sample.dto.ProductResponse;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product productA;
    private Product productB;
    private Product productC;

    @BeforeEach
    void setUp() {
        productA = new Product(1L, "A", "http://a", "desc A", new BigDecimal("10.00"), 4.0, Map.of("k", "v"));
        productB = new Product(2L, "B", "http://b", "desc B", new BigDecimal("20.00"), 4.5, Map.of());
        productC = new Product(3L, "C", "http://c", "desc C", new BigDecimal("30.00"), 5.0, Map.of("x", "y"));
    }

    @Nested
    @DisplayName("getAllProducts")
    class GetAllProducts {

        @Test
        void returnsEmptyListWhenCatalogIsEmpty() {
            when(productRepository.findAll()).thenReturn(List.of());

            assertThat(productService.getAllProducts()).isEmpty();
        }

        @Test
        void mapsAllProductsToResponses() {
            when(productRepository.findAll()).thenReturn(List.of(productA, productB));

            List<ProductResponse> result = productService.getAllProducts();

            assertThat(result).hasSize(2);
            assertThat(result.getFirst().id()).isEqualTo(1L);
            assertThat(result.getFirst().name()).isEqualTo("A");
            assertThat(result.get(1).id()).isEqualTo(2L);
        }
    }

    @Nested
    @DisplayName("getProductById")
    class GetProductById {

        @Test
        void returnsProductWhenFound() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(productA));

            ProductResponse response = productService.getProductById(1L);

            assertThat(response.id()).isEqualTo(1L);
            assertThat(response.name()).isEqualTo("A");
            assertThat(response.price()).isEqualByComparingTo(new BigDecimal("10.00"));
        }

        @Test
        void throwsWhenNotFound() {
            when(productRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.getProductById(99L))
                    .isInstanceOf(NoSuchResourceFoundException.class)
                    .hasMessageContaining("not found");
        }
    }

    @Nested
    @DisplayName("compareProducts")
    class CompareProducts {

        @Test
        void throwsWhenIdsNull() {
            assertThatThrownBy(() -> productService.compareProducts(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("null or empty");
        }

        @Test
        void throwsWhenIdsEmpty() {
            assertThatThrownBy(() -> productService.compareProducts(List.of()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("null or empty");
        }

        @Test
        void throwsWhenAnyIdMissing() {
            when(productRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(productA));

            assertThatThrownBy(() -> productService.compareProducts(List.of(1L, 2L)))
                    .isInstanceOf(NoSuchResourceFoundException.class)
                    .hasMessageContaining("not found");
        }

        @Test
        void returnsResultsInSameOrderAsRequestEvenWhenRepositoryOrderDiffers() {
            List<Long> requestedOrder = List.of(3L, 1L, 2L);
            when(productRepository.findAllById(requestedOrder))
                    .thenReturn(List.of(productA, productB, productC));

            List<ProductResponse> result = productService.compareProducts(requestedOrder);

            assertThat(result).extracting(ProductResponse::id).containsExactly(3L, 1L, 2L);
            assertThat(result.get(0).name()).isEqualTo("C");
            assertThat(result.get(1).name()).isEqualTo("A");
            assertThat(result.get(2).name()).isEqualTo("B");
            verify(productRepository).findAllById(requestedOrder);
        }

        @Test
        void deduplicatesIdsForRepositoryLookupButPreservesRequestOrderAndLength() {
            List<Long> ids = List.of(1L, 1L, 2L);
            when(productRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(productA, productB));

            List<ProductResponse> result = productService.compareProducts(ids);

            assertThat(result).extracting(ProductResponse::id).containsExactly(1L, 1L, 2L);
            assertThat(result.get(0).name()).isEqualTo("A");
            assertThat(result.get(1).name()).isEqualTo("A");
            assertThat(result.get(2).name()).isEqualTo("B");
            verify(productRepository).findAllById(List.of(1L, 2L));
        }
    }
}
