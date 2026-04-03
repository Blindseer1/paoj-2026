package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class SRLColaborator extends PersoanaJuridica {
    private double venitLunar;
    private double cheltuieliLunare;

    public SRLColaborator() {
        super(TipColaborator.SRL);
    }

    @Override
    public void citeste(Scanner in) {
        nume = in.next();
        prenume = in.next();
        venitLunar = in.nextDouble();
        cheltuieliLunare = in.nextDouble();
        venitBrutLunar = venitLunar;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        return (venitLunar - cheltuieliLunare) * 12 * 0.84;
    }
}
