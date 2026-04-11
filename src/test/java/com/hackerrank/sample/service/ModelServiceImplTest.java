package com.hackerrank.sample.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hackerrank.sample.exception.BadResourceRequestException;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.model.Model;
import com.hackerrank.sample.repository.ModelRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ModelServiceImplTest {

    @Mock
    private ModelRepository modelRepository;

    @InjectMocks
    private ModelServiceImpl modelService;

    @Nested
    @DisplayName("createModel")
    class CreateModel {

        @Test
        void savesWhenIdDoesNotExist() {
            Model model = new Model(1L, "first");
            when(modelRepository.findById(1L)).thenReturn(Optional.empty());

            modelService.createModel(model);

            verify(modelRepository).save(model);
        }

        @Test
        void throwsWhenDuplicateId() {
            Model model = new Model(1L, "duplicate");
            when(modelRepository.findById(1L)).thenReturn(Optional.of(new Model(1L, "existing")));

            assertThatThrownBy(() -> modelService.createModel(model))
                    .isInstanceOf(BadResourceRequestException.class)
                    .hasMessageContaining("same id");

            verify(modelRepository, never()).save(model);
        }
    }

    @Nested
    @DisplayName("getModelById")
    class GetModelById {

        @Test
        void returnsModelWhenFound() {
            Model expected = new Model(5L, "found");
            when(modelRepository.findById(5L)).thenReturn(Optional.of(expected));

            assertThat(modelService.getModelById(5L)).isEqualTo(expected);
        }

        @Test
        void throwsWhenMissing() {
            when(modelRepository.findById(9L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> modelService.getModelById(9L))
                    .isInstanceOf(NoSuchResourceFoundException.class)
                    .hasMessageContaining("No model");
        }
    }

    @Nested
    @DisplayName("getAllModels")
    class GetAllModels {

        @Test
        void delegatesToRepository() {
            List<Model> models = List.of(new Model(1L, "a"), new Model(2L, "b"));
            when(modelRepository.findAll()).thenReturn(models);

            assertThat(modelService.getAllModels()).isEqualTo(models);
        }
    }

    @Nested
    @DisplayName("deleteAllModels")
    class DeleteAll {

        @Test
        void callsBatchDelete() {
            modelService.deleteAllModels();
            verify(modelRepository).deleteAllInBatch();
        }
    }

    @Nested
    @DisplayName("deleteModelById")
    class DeleteById {

        @Test
        void delegatesToRepository() {
            modelService.deleteModelById(3L);
            verify(modelRepository).deleteById(3L);
        }
    }
}
