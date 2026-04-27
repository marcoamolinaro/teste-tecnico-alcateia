package com.desafio.reserva.service;

public enum ReservationError {
    RESERVA_NULA("A reserva nao pode ser nula."),
    SALA_INVALIDA("salaId e obrigatorio."),
    USUARIO_INVALIDO("usuarioId e obrigatorio."),
    DATA_INICIO_INVALIDA("inicio e obrigatorio."),
    DATA_FIM_INVALIDA("fim e obrigatorio."),
    INTERVALO_INVALIDO("inicio deve ser menor que fim."),
    INICIO_NO_PASSADO("A reserva deve iniciar no futuro."),
    CONFLITO_HORARIO("Ja existe reserva nesse horario para a sala informada."),
    PARAMETRO_INVALIDO("Parametros invalidos para verificacao de disponibilidade.");

    private final String message;

    ReservationError(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
