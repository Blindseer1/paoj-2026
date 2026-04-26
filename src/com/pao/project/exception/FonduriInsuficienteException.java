package com.pao.project.exception;

public class FonduriInsuficienteException extends Exception {

    private final double soldDisponibil;
    private final double sumaSolicitata;

    public FonduriInsuficienteException(double soldDisponibil, double sumaSolicitata) {
        super(String.format(
                "Fonduri insuficiente: sold disponibil %.2f RON, suma solicitata %.2f RON.",
                soldDisponibil, sumaSolicitata));
        this.soldDisponibil  = soldDisponibil;
        this.sumaSolicitata  = sumaSolicitata;
    }

    public double getSoldDisponibil() { return soldDisponibil; }
    public double getSumaSolicitata() { return sumaSolicitata; }
}
