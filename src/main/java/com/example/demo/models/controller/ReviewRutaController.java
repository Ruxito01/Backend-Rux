package com.example.demo.models.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.entity.ReviewRuta;
import com.example.demo.models.service.IReviewRutaService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ReviewRutaController {

    @Autowired
    private IReviewRutaService reviewRutaService;

    @GetMapping("/review-rutas")
    public List<ReviewRuta> index() {
        return reviewRutaService.findAll();
    }

    @GetMapping("/review-rutas/{id}")
    public ReviewRuta show(@PathVariable Long id) {
        return reviewRutaService.findById(id);
    }

    @PostMapping("/review-rutas")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewRuta create(@RequestBody ReviewRuta reviewRuta) {
        return reviewRutaService.save(reviewRuta);
    }

    @PutMapping("/review-rutas/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewRuta update(@RequestBody ReviewRuta reviewRuta, @PathVariable Long id) {
        ReviewRuta reviewRutaActual = reviewRutaService.findById(id);

        reviewRutaActual.setUsuario(reviewRuta.getUsuario());
        reviewRutaActual.setRuta(reviewRuta.getRuta());
        reviewRutaActual.setCalificacion(reviewRuta.getCalificacion());

        return reviewRutaService.save(reviewRutaActual);
    }

    @DeleteMapping("/review-rutas/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reviewRutaService.delete(id);
    }
}
