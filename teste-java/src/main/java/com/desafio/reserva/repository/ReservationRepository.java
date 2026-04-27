package com.desafio.reserva.repository;

import com.desafio.reserva.model.Reserva;

import java.util.List;

public interface ReservationRepository {
    void save(Reserva reserva);

    List<Reserva> findBySalaId(String salaId);
}
