package com.example.demo.models.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.IParticipanteSalidaDao;
import com.example.demo.models.entity.ParticipanteSalida;

@Service
public class ParticipanteSalidaServiceImpl implements IParticipanteSalidaService {

    @Autowired
    private IParticipanteSalidaDao participanteSalidaDao;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipanteSalida> findAll() {
        return (List<ParticipanteSalida>) participanteSalidaDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public ParticipanteSalida findById(Long id) {
        return participanteSalidaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public ParticipanteSalida save(ParticipanteSalida participanteSalida) {
        return participanteSalidaDao.save(participanteSalida);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        participanteSalidaDao.deleteById(id);
    }
}
