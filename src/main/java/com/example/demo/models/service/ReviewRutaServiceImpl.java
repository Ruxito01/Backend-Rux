package com.example.demo.models.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.IReviewRutaDao;
import com.example.demo.models.entity.ReviewRuta;

@Service
public class ReviewRutaServiceImpl implements IReviewRutaService {

    @Autowired
    private IReviewRutaDao reviewRutaDao;

    @Override
    @Transactional(readOnly = true)
    public List<ReviewRuta> findAll() {
        return (List<ReviewRuta>) reviewRutaDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewRuta findById(Long id) {
        return reviewRutaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public ReviewRuta save(ReviewRuta reviewRuta) {
        return reviewRutaDao.save(reviewRuta);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        reviewRutaDao.deleteById(id);
    }
}
