package com.desafio.reserva.service;

import com.desafio.reserva.model.Reserva;
import com.desafio.reserva.repository.InMemoryReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReservationServiceTest {

    private static final Clock FIXED_CLOCK = Clock.fixed(Instant.parse("2026-04-27T10:00:00Z"), ZoneOffset.UTC);

    private ReservationService service;

    @BeforeEach
    void setUp() {
        service = new ReservationService(new InMemoryReservationRepository(), FIXED_CLOCK);
    }

    @Test
    void deveReservarHorarioLivreComSucesso() {
        Reserva reserva = new Reserva(
                "sala-1",
                "user-1",
                LocalDateTime.of(2026, 4, 27, 12, 0),
                LocalDateTime.of(2026, 4, 27, 13, 0)
        );

        boolean reservou = service.reservar(reserva);

        assertTrue(reservou);
        assertEquals(1, service.listarReservasPorSala("sala-1").size());
    }

    @Test
    void deveFalharQuandoHaConflitoDeHorario() {
        Reserva primeira = new Reserva(
                "sala-1",
                "user-1",
                LocalDateTime.of(2026, 4, 27, 14, 0),
                LocalDateTime.of(2026, 4, 27, 15, 0)
        );

        Reserva conflito = new Reserva(
                "sala-1",
                "user-2",
                LocalDateTime.of(2026, 4, 27, 14, 30),
                LocalDateTime.of(2026, 4, 27, 15, 30)
        );

        assertTrue(service.reservar(primeira));

        boolean reservou = service.reservar(conflito);

        assertFalse(reservou);
        assertEquals(ReservationError.CONFLITO_HORARIO, service.ultimoErro().orElseThrow());
    }

    @Test
    void deveFalharQuandoFimEhAntesDoInicio() {
        Reserva invalida = new Reserva(
                "sala-1",
                "user-1",
                LocalDateTime.of(2026, 4, 27, 16, 0),
                LocalDateTime.of(2026, 4, 27, 15, 0)
        );

        boolean reservou = service.reservar(invalida);

        assertFalse(reservou);
        assertEquals(ReservationError.INTERVALO_INVALIDO, service.ultimoErro().orElseThrow());
    }

    @Test
    void deveFalharQuandoInicioEhNoPassado() {
        Reserva invalida = new Reserva(
                "sala-1",
                "user-1",
                LocalDateTime.of(2026, 4, 27, 9, 0),
                LocalDateTime.of(2026, 4, 27, 11, 0)
        );

        boolean reservou = service.reservar(invalida);

        assertFalse(reservou);
        assertEquals(ReservationError.INICIO_NO_PASSADO, service.ultimoErro().orElseThrow());
    }

    @Test
    void devePermitirReservaEmSequenciaSemSobreposicao() {
        Reserva primeira = new Reserva(
                "sala-1",
                "user-1",
                LocalDateTime.of(2026, 4, 27, 17, 0),
                LocalDateTime.of(2026, 4, 27, 18, 0)
        );

        Reserva sequencia = new Reserva(
                "sala-1",
                "user-2",
                LocalDateTime.of(2026, 4, 27, 18, 0),
                LocalDateTime.of(2026, 4, 27, 19, 0)
        );

        assertTrue(service.reservar(primeira));
        assertTrue(service.reservar(sequencia));
        assertEquals(2, service.listarReservasPorSala("sala-1").size());
    }

    @Test
    void estaDisponivelDeveRetornarFalseParaIntervaloInvalido() {
        boolean disponivel = service.estaDisponivel(
                "sala-1",
                LocalDateTime.of(2026, 4, 27, 19, 0),
                LocalDateTime.of(2026, 4, 27, 18, 0)
        );

        assertFalse(disponivel);
        assertEquals(ReservationError.INTERVALO_INVALIDO, service.ultimoErro().orElseThrow());
    }
}
