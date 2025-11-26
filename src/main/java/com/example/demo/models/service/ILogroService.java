package com.example.demo.models.service;

import java.util.List;
import com.example.demo.models.entity.Logro;

public interface ILogroService {
    public List<Logro> findAll();

    public Logro findById(Long id);

    public Logro save(Logro logro);

    public void delete(Long id);
}
