package com.pao.laboratory05.angajati;

import java.util.Arrays;

public class AngajatService {

    private Angajat[] angajati;

    private AngajatService() {
        angajati = new Angajat[0];
    }

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    public void addAngajat(Angajat a) {
        Angajat[] nou = new Angajat[angajati.length + 1];

        for (int i = 0; i < angajati.length; i++) {
            nou[i] = angajati[i];
        }

        nou[angajati.length] = a;
        angajati = nou;

        System.out.println("Angajat adăugat: " + a);
    }

    public void printAll() {
        System.out.println("Lista angajați:");
        for (Angajat a : angajati) {
            System.out.println(a);
        }
    }

    public void listBySalary() {
        Angajat[] copy = angajati.clone();
        Arrays.sort(copy);

        System.out.println("Sortare după salariu (desc):");
        for (Angajat a : copy) {
            System.out.println(a);
        }
    }

    public void findByDepartament(String numeDept) {
        boolean gasit = false;

        for (Angajat a : angajati) {
            if (a.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                System.out.println(a);
                gasit = true;
            }
        }

        if (!gasit) {
            System.out.println("Niciun angajat în departamentul: " + numeDept);
        }
    }
}
