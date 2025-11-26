package com.example.demo.models.service;

import java.util.List;
import com.example.demo.models.entity.Avatar;

public interface IAvatarService {
    public List<Avatar> findAll();

    public Avatar findById(Long id);

    public Avatar save(Avatar avatar);

    public void delete(Long id);
}
