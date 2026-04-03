package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends PersoanaFizica {
    // Salariu minim brut 2026: 4050 lei/luna x 12 = 48600 lei/an
    private static final double SALARIU_MINIM_BRUT = 48600.0;

    private double venitLunar;
    private double cheltuieliLunare;

    public PFAColaborator() {
        super(TipColaborator.PFA);
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
        double venitNet = (venitLunar - cheltuieliLunare) * 12;

        double impozit = 0.10 * venitNet;

        double prag6  = 6  * SALARIU_MINIM_BRUT;
        double prag72 = 72 * SALARIU_MINIM_BRUT;
        double cass;
        if (venitNet < prag6) {
            cass = 0.10 * prag6;
        } else if (venitNet <= prag72) {
            cass = 0.10 * venitNet;
        } else {
            cass = 0.10 * prag72;
        }

        double prag12 = 12 * SALARIU_MINIM_BRUT;
        double prag24 = 24 * SALARIU_MINIM_BRUT;
        double cas;
        if (venitNet < prag12) {
            cas = 0;
        } else if (venitNet <= prag24) {
            cas = 0.25 * prag12;
        } else {
            cas = 0.25 * prag24;
        }

        return venitNet - impozit - cass - cas;
    }
}
