package com.desafio.reserva.repository;

import com.desafio.reserva.model.Reserva;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryReservationRepository implements ReservationRepository {
    private final Map<String, List<Reserva>> reservasPorSala = new ConcurrentHashMap<>();

    @Override
    public void save(Reserva reserva) {
        reservasPorSala.computeIfAbsent(reserva.getSalaId(), ignored -> Collections.synchronizedList(new ArrayList<>()))
                .add(reserva);
    }

    @Override
    public List<Reserva> findBySalaId(String salaId) {
        List<Reserva> reservas = reservasPorSala.getOrDefault(salaId, Collections.emptyList());
        synchronized (reservas) {
            return new ArrayList<>(reservas);
        }
    }
}
