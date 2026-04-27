package com.desafio.reserva.service;

import java.util.Optional;

public final class ReservationResult {
    private final boolean success;
    private final ReservationError error;

    private ReservationResult(boolean success, ReservationError error) {
        this.success = success;
        this.error = error;
    }

    public static ReservationResult success() {
        return new ReservationResult(true, null);
    }

    public static ReservationResult failure(ReservationError error) {
        return new ReservationResult(false, error);
    }

    public boolean isSuccess() {
        return success;
    }

    public Optional<ReservationError> getError() {
        return Optional.ofNullable(error);
    }

    public Optional<String> getMessage() {
        return getError().map(ReservationError::message);
    }
}
