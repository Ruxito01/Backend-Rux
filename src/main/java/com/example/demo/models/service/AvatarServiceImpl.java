package com.example.demo.models.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.IAvatarDao;
import com.example.demo.models.entity.Avatar;

@Service
public class AvatarServiceImpl implements IAvatarService {

    @Autowired
    private IAvatarDao avatarDao;

    @Override
    @Transactional(readOnly = true)
    public List<Avatar> findAll() {
        return (List<Avatar>) avatarDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Avatar findById(Long id) {
        return avatarDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Avatar save(Avatar avatar) {
        return avatarDao.save(avatar);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        avatarDao.deleteById(id);
    }
}
