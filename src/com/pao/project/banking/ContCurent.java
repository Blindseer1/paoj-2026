package com.pao.project.banking;

public class ContCurent extends Cont {

    private double limitaOverdraft;

    public ContCurent(String iban, String titular, double soldInitial, double limitaOverdraft) {
        super(iban, titular, soldInitial);
        this.limitaOverdraft = limitaOverdraft;
    }

    @Override
    public String getTipCont() { return "Cont Curent"; }

    public double getLimitaOverdraft()                     { return limitaOverdraft; }
    public void setLimitaOverdraft(double limitaOverdraft) { this.limitaOverdraft = limitaOverdraft; }

    public double getSoldDisponibil() { return getSold() + limitaOverdraft; }

    @Override
    public String toString() {
        return super.toString() + String.format(", overdraft=%.2f RON", limitaOverdraft);
    }
}
