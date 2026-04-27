package com.desafio.reserva.service;

import com.desafio.reserva.model.Reserva;

import java.time.Clock;
import java.time.LocalDateTime;

public class ReservationValidator {
    private final Clock clock;

    public ReservationValidator(Clock clock) {
        this.clock = clock;
    }

    public ReservationResult validateReserva(Reserva reserva) {
        if (reserva == null) {
            return ReservationResult.failure(ReservationError.RESERVA_NULA);
        }

        if (isBlank(reserva.getSalaId())) {
            return ReservationResult.failure(ReservationError.SALA_INVALIDA);
        }

        if (isBlank(reserva.getUsuarioId())) {
            return ReservationResult.failure(ReservationError.USUARIO_INVALIDO);
        }

        if (reserva.getInicio() == null) {
            return ReservationResult.failure(ReservationError.DATA_INICIO_INVALIDA);
        }

        if (reserva.getFim() == null) {
            return ReservationResult.failure(ReservationError.DATA_FIM_INVALIDA);
        }

        return validateInterval(reserva.getInicio(), reserva.getFim());
    }

    public ReservationResult validateInterval(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null) {
            return ReservationResult.failure(ReservationError.PARAMETRO_INVALIDO);
        }

        if (!inicio.isBefore(fim)) {
            return ReservationResult.failure(ReservationError.INTERVALO_INVALIDO);
        }

        LocalDateTime agora = LocalDateTime.now(clock);
        if (!inicio.isAfter(agora)) {
            return ReservationResult.failure(ReservationError.INICIO_NO_PASSADO);
        }

        return ReservationResult.success();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
