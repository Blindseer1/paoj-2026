package com.pao.project.banking;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExtrasCont {

    private final String iban;
    private final LocalDate dataStart;
    private final LocalDate dataFinal;
    private final double soldInitial;
    private double soldFinal;
    private final List<Tranzactie> tranzactii;

    public ExtrasCont(String iban, LocalDate dataStart, LocalDate dataFinal, double soldInitial) {
        this.iban        = iban;
        this.dataStart   = dataStart;
        this.dataFinal   = dataFinal;
        this.soldInitial = soldInitial;
        this.tranzactii  = new ArrayList<>();
    }

    public String getIban()                 { return iban; }
    public LocalDate getDataStart()         { return dataStart; }
    public LocalDate getDataFinal()         { return dataFinal; }
    public double getSoldInitial()          { return soldInitial; }
    public double getSoldFinal()            { return soldFinal; }
    public List<Tranzactie> getTranzactii() { return tranzactii; }

    public void setSoldFinal(double soldFinal)      { this.soldFinal = soldFinal; }
    public void adaugaTranzactie(Tranzactie t)      { tranzactii.add(t); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("  IBAN      : %s%n", iban));
        sb.append(String.format("  Perioada  : %s → %s%n", dataStart, dataFinal));
        sb.append(String.format("  Sold initial : %10.2f RON%n", soldInitial));
        sb.append(String.format("  Sold final   : %10.2f RON%n", soldFinal));
        sb.append(String.format("  Tranzactii   : %d%n", tranzactii.size()));
        for (Tranzactie t : tranzactii) {
            sb.append("  ").append(t).append("\n");
        }
        return sb.toString();
    }
}
