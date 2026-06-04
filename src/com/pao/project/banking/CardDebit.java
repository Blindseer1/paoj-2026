package com.pao.project.banking;

import java.time.LocalDate;

public class CardDebit extends Card {

    private double limitaZilnica;

    public CardDebit(String numarCard, String cvv, LocalDate dataExpirare,
                     String titularCard, double limitaZilnica) {
        super(numarCard, cvv, dataExpirare, titularCard);
        this.limitaZilnica = limitaZilnica;
    }

    @Override
    public String getTipCard() { return "Debit"; }

    public double getLimitaZilnica()                   { return limitaZilnica; }
    public void setLimitaZilnica(double limitaZilnica) { this.limitaZilnica = limitaZilnica; }

    @Override
    public String toString() {
        return super.toString() + String.format(", limitaZilnica=%.2f RON", limitaZilnica);
    }
}
