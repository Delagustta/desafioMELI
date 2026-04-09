package com.hackerrank.sample.controller;

import com.hackerrank.sample.model.Model;
import com.hackerrank.sample.service.ModelService;
import io.swagger.v3.oas.annotations.Hidden;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@Hidden
public class ModelController {
    @Autowired
    private ModelService modelService;

    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "Default Java 21 Project Home Page";
    }

    @PostMapping(value = "/model", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewModel(@RequestBody @Valid Model model) {
        modelService.createModel(model);
    }

    @DeleteMapping("/erase")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllModels() {
        modelService.deleteAllModels();
    }

    @DeleteMapping("/model/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteModelById(@PathVariable Long id) {
        modelService.deleteModelById(id);
    }

    @GetMapping("/model")
    @ResponseStatus(HttpStatus.OK)
    public List<Model> getAllModels() {
        return modelService.getAllModels();
    }

    @GetMapping("/model/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Model getModelById(@PathVariable Long id) {
        return modelService.getModelById(id);
    }
}
