package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends PersoanaFizica {
    private boolean bonus;

    public CIMColaborator() {
        super(TipColaborator.CIM);
    }

    @Override
    public void citeste(Scanner in) {
        nume = in.next();
        prenume = in.next();
        venitBrutLunar = in.nextDouble();
        bonus = in.hasNext() && in.next().equalsIgnoreCase("DA");
    }

    @Override
    public boolean areBonus() { return bonus; }

    @Override
    public double calculeazaVenitNetAnual() {
        double net = venitBrutLunar * 12 * 0.55;
        if (bonus) net *= 1.10;
        return net;
    }
}
