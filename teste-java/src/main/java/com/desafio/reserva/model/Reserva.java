package com.desafio.reserva.model;

import java.time.LocalDateTime;
import java.util.Objects;

public final class Reserva {
    private final String salaId;
    private final String usuarioId;
    private final LocalDateTime inicio;
    private final LocalDateTime fim;

    public Reserva(String salaId, String usuarioId, LocalDateTime inicio, LocalDateTime fim) {
        this.salaId = salaId;
        this.usuarioId = usuarioId;
        this.inicio = inicio;
        this.fim = fim;
    }

    public String getSalaId() {
        return salaId;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public boolean sobrepoe(Reserva outra) {
        return inicio.isBefore(outra.fim) && outra.inicio.isBefore(fim);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reserva reserva)) {
            return false;
        }
        return Objects.equals(salaId, reserva.salaId)
                && Objects.equals(usuarioId, reserva.usuarioId)
                && Objects.equals(inicio, reserva.inicio)
                && Objects.equals(fim, reserva.fim);
    }

    @Override
    public int hashCode() {
        return Objects.hash(salaId, usuarioId, inicio, fim);
    }
}
