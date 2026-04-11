package com.hackerrank.sample.service;

import com.hackerrank.sample.exception.BadResourceRequestException;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.model.Model;
import com.hackerrank.sample.repository.ModelRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("modelService")
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelService {
    private final ModelRepository modelRepository;

    @Override
    public void deleteAllModels() {
        modelRepository.deleteAllInBatch();
        log.info("All models removed from persistence");
    }

    @Override
    public void deleteModelById(Long id) {
        modelRepository.deleteById(id);
        log.debug("Model deleted id={}", id);
    }

    @Override
    public void createModel(Model model) {
        Optional<Model> existingModel = modelRepository.findById(model.getId());

        if (existingModel.isPresent()) {
            log.debug("Rejected duplicate model id={}", model.getId());
            throw new BadResourceRequestException("Model with same id exists.");
        }

        modelRepository.save(model);
        log.debug("Model created id={}", model.getId());
    }

    @Override
    public Model getModelById(Long id) {
        Optional<Model> model = modelRepository.findById(id);

        if (model.isEmpty()) {
            log.debug("Model not found id={}", id);
            throw new NoSuchResourceFoundException("No model with given id found.");
        }

        return model.get();
    }

    @Override
    public List<Model> getAllModels() {
        List<Model> all = modelRepository.findAll();
        log.debug("Listing models count={}", all.size());
        return all;
    }
}
