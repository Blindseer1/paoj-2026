package com.pao.project.banking;

import java.time.LocalDate;

public abstract class Card {

    private final String numarCard;
    private final String cvv;
    private final LocalDate dataExpirare;
    private final String titularCard;
    private boolean activ;

    public Card(String numarCard, String cvv, LocalDate dataExpirare, String titularCard) {
        this.numarCard    = numarCard;
        this.cvv          = cvv;
        this.dataExpirare = dataExpirare;
        this.titularCard  = titularCard;
        this.activ        = true;
    }

    public abstract String getTipCard();

    public String getNumarCard()        { return numarCard; }
    public String getCvv()              { return cvv; }
    public LocalDate getDataExpirare()  { return dataExpirare; }
    public String getTitularCard()      { return titularCard; }
    public boolean isActiv()            { return activ; }
    public void setActiv(boolean activ) { this.activ = activ; }

    public String getNumarMascat() {
        return  numarCard.substring(numarCard.length() - 4);
    }

    @Override
    public String toString() {
        return String.format("Card{numar='%s', titular='%s', tip=%s, expira=%s, activ=%b}",
                getNumarMascat(), titularCard, getTipCard(), dataExpirare, activ);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        return numarCard.equals(((Card) o).numarCard);
    }

    @Override
    public int hashCode() {
        return numarCard.hashCode();
    }
}
