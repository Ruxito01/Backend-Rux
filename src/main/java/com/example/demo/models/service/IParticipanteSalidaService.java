package com.example.demo.models.service;

import java.util.List;
import com.example.demo.models.entity.ParticipanteSalida;

public interface IParticipanteSalidaService {
    public List<ParticipanteSalida> findAll();

    public ParticipanteSalida findById(Long id);

    public ParticipanteSalida save(ParticipanteSalida participanteSalida);

    public void delete(Long id);
}
