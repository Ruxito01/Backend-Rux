package com.example.demo.models.ServiceImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.ILogroDao;
import com.example.demo.models.entity.Logro;
import com.example.demo.models.service.ILogroService;

@Service
public class LogroServiceImpl implements ILogroService {

    @Autowired
    private ILogroDao logroDao;

    @Override
    @Transactional(readOnly = true)
    public List<Logro> findAll() {
        return (List<Logro>) logroDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Logro findById(Long id) {
        return logroDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Logro save(Logro logro) {
        return logroDao.save(logro);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        logroDao.deleteById(id);
    }
}
