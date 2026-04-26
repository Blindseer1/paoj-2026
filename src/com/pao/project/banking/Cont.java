package com.pao.project.banking;

import java.util.ArrayList;
import java.util.List;

public abstract class Cont implements Comparable<Cont> {

    private final String iban;
    private double sold;
    private final String titular;
    private final List<Card> carduri;
    private final List<Tranzactie> tranzactii;

    public Cont(String iban, String titular, double soldInitial) {
        this.iban        = iban;
        this.titular     = titular;
        this.sold        = soldInitial;
        this.carduri     = new ArrayList<>();
        this.tranzactii  = new ArrayList<>();
    }

    public abstract String getTipCont();

    public String getIban()                 { return iban; }
    public double getSold()                 { return sold; }
    public String getTitular()              { return titular; }
    public List<Card> getCarduri()          { return carduri; }
    public List<Tranzactie> getTranzactii() { return tranzactii; }

    public void setSold(double sold)        { this.sold = sold; }

    public void adaugaCard(Card card)           { carduri.add(card); }
    public void adaugaTranzactie(Tranzactie t)  { tranzactii.add(t); }

    @Override
    public int compareTo(Cont other) {
        return this.iban.compareTo(other.iban);
    }

    @Override
    public String toString() {
        return String.format("Cont{IBAN='%s', titular='%s', sold=%10.2f RON, tip=%s}",
                iban, titular, sold, getTipCont());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cont)) return false;
        return iban.equals(((Cont) o).iban);
    }

    @Override
    public int hashCode() {
        return iban.hashCode();
    }
}
