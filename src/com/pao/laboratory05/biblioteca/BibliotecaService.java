package com.pao.laboratory05.biblioteca;

import java.util.Arrays;
import java.util.Comparator;

public class BibliotecaService {

    private Carte[] carti;

    private BibliotecaService() {
        carti = new Carte[0];
    }

    private static class Holder {
        private static final BibliotecaService INSTANCE = new BibliotecaService();
    }

    public static BibliotecaService getInstance() {
        return Holder.INSTANCE;
    }

    public void addCarte(Carte carte) {
        Carte[] nou = new Carte[carti.length + 1];

        for (int i = 0; i < carti.length; i++) {
            nou[i] = carti[i];
        }

        nou[carti.length] = carte;
        carti = nou;

        System.out.println("Carte adăugată: " + carte);
    }

    public void listSortedByRating() {
        Carte[] copy = carti.clone();
        Arrays.sort(copy);

        System.out.println("Sortare după rating (desc):");
        for (Carte c : copy) {
            System.out.println(c);
        }
    }

    public void listSortedBy(Comparator<Carte> comparator) {
        Carte[] copy = carti.clone();
        Arrays.sort(copy, comparator);

        System.out.println("Sortare custom:");
        for (Carte c : copy) {
            System.out.println(c);
        }
    }
}
