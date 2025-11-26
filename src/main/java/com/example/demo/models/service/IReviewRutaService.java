package com.example.demo.models.service;

import java.util.List;
import com.example.demo.models.entity.ReviewRuta;

public interface IReviewRutaService {
    public List<ReviewRuta> findAll();

    public ReviewRuta findById(Long id);

    public ReviewRuta save(ReviewRuta reviewRuta);

    public void delete(Long id);
}
