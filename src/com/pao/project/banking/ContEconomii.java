package com.pao.project.banking;

public class ContEconomii extends Cont {

    private double rataDobanda;   
    private final double soldMinim;

    public ContEconomii(String iban, String titular, double soldInitial,
                        double rataDobanda, double soldMinim) {
        super(iban, titular, soldInitial);
        this.rataDobanda = rataDobanda;
        this.soldMinim   = soldMinim;
    }

    @Override
    public String getTipCont() { return "Cont Economii"; }

    public double getRataDobanda()                 { return rataDobanda; }
    public double getSoldMinim()                   { return soldMinim; }
    public void setRataDobanda(double rataDobanda) { this.rataDobanda = rataDobanda; }

    public double calculeazaDobandaAnuala() {
        return getSold() * rataDobanda / 100.0;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(
                ", dobanda=%.2f%%, soldMinim=%.2f RON, dobandaAnuala=%.2f RON",
                rataDobanda, soldMinim, calculeazaDobandaAnuala());
    }
}
