package com.example.demo.models.entity;

public enum EstadoParticipante {
    registrado,
    ingresa,
    abandona, // Salió voluntariamente pero puede volver (15 min)
    cancela, // Definitivamente no puede volver (se unió a otro viaje o organizador canceló)
    finaliza,
    solicita_ingreso,
    rechazado
}
