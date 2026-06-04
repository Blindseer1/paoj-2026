package com.pao.project.banking;

import java.time.LocalDate;

public class CardCredit extends Card {

    private double limitaCredit;
    private double soldUtilizat;

    public CardCredit(String numarCard, String cvv, LocalDate dataExpirare,
                      String titularCard, double limitaCredit) {
        super(numarCard, cvv, dataExpirare, titularCard);
        this.limitaCredit = limitaCredit;
        this.soldUtilizat = 0.0;
    }

    @Override
    public String getTipCard() { return "Credit"; }

    public double getLimitaCredit()                  { return limitaCredit; }
    public double getSoldUtilizat()                  { return soldUtilizat; }
    public double getSoldDisponibil()                { return limitaCredit - soldUtilizat; }
    public void setSoldUtilizat(double soldUtilizat) { this.soldUtilizat = soldUtilizat; }

    @Override
    public String toString() {
        return super.toString() + String.format(
                ", limitaCredit=%.2f RON, utilizat=%.2f RON, disponibil=%.2f RON",
                limitaCredit, soldUtilizat, getSoldDisponibil());
    }
}
