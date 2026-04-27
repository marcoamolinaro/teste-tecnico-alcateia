package com.desafio.reserva.service;

import com.desafio.reserva.model.Reserva;
import com.desafio.reserva.repository.InMemoryReservationRepository;
import com.desafio.reserva.repository.ReservationRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ReservationService {
    private final ReservationRepository repository;
    private final ReservationValidator validator;
    private ReservationResult lastResult = ReservationResult.success();

    public ReservationService() {
        this(new InMemoryReservationRepository(), Clock.systemDefaultZone());
    }

    public ReservationService(ReservationRepository repository, Clock clock) {
        this(repository, new ReservationValidator(clock));
    }

    public ReservationService(ReservationRepository repository, ReservationValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public boolean reservar(Reserva reserva) {
        ReservationResult result = reservarComResultado(reserva);
        return result.isSuccess();
    }

    public ReservationResult reservarComResultado(Reserva reserva) {
        ReservationResult validation = validator.validateReserva(reserva);
        if (!validation.isSuccess()) {
            lastResult = validation;
            return validation;
        }

        boolean disponivel = estaDisponivel(reserva.getSalaId(), reserva.getInicio(), reserva.getFim());
        if (!disponivel) {
            ReservationResult conflito = ReservationResult.failure(ReservationError.CONFLITO_HORARIO);
            lastResult = conflito;
            return conflito;
        }

        repository.save(reserva);
        lastResult = ReservationResult.success();
        return lastResult;
    }

    public boolean estaDisponivel(String salaId, LocalDateTime inicio, LocalDateTime fim) {
        if (salaId == null || salaId.isBlank()) {
            lastResult = ReservationResult.failure(ReservationError.SALA_INVALIDA);
            return false;
        }

        ReservationResult intervalValidation = validator.validateInterval(inicio, fim);
        if (!intervalValidation.isSuccess()) {
            lastResult = intervalValidation;
            return false;
        }

        Reserva nova = new Reserva(salaId, "_probe_", inicio, fim);
        boolean conflito = listarReservasPorSala(salaId)
                .stream()
                .anyMatch(existente -> existente.sobrepoe(nova));

        if (conflito) {
            lastResult = ReservationResult.failure(ReservationError.CONFLITO_HORARIO);
            return false;
        }

        lastResult = ReservationResult.success();
        return true;
    }

    public List<Reserva> listarReservasPorSala(String salaId) {
        return repository.findBySalaId(salaId)
                .stream()
                .sorted(Comparator.comparing(Reserva::getInicio))
                .toList();
    }

    public Optional<String> ultimoMotivoFalha() {
        return lastResult.getMessage();
    }

    public Optional<ReservationError> ultimoErro() {
        return lastResult.getError();
    }
}
