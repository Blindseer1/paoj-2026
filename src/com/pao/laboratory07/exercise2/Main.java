package com.pao.laboratory07.exercise2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine().trim());

        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String[] parts = scanner.nextLine().trim().split(" ");
            String tip = parts[0];
            String nume = parts[1];

            Comanda c = switch (tip) {
                case "STANDARD"   -> new ComandaStandard(nume, Double.parseDouble(parts[2]));
                case "DISCOUNTED" -> new ComandaRedusa(nume, Double.parseDouble(parts[2]), Integer.parseInt(parts[3]));
                case "GIFT"       -> new ComandaGratuita(nume);
                default           -> throw new IllegalArgumentException("Unknown type: " + tip);
            };
            comenzi.add(c);
        }

        for (Comanda c : comenzi) {
            System.out.println(c.descriere());
        }

        double sumaStandard = 0, sumaDiscounted = 0, sumaGift = 0;
        int nrStandard = 0, nrDiscounted = 0, nrGift = 0;

        for (Comanda c : comenzi) {
            if (c instanceof ComandaStandard)   { sumaStandard   += c.pretFinal(); nrStandard++;   }
            else if (c instanceof ComandaRedusa) { sumaDiscounted += c.pretFinal(); nrDiscounted++; }
            else if (c instanceof ComandaGratuita){ sumaGift      += c.pretFinal(); nrGift++;       }
        }

        System.out.println();
        System.out.println("Statistici:");
        if (nrStandard   > 0) System.out.printf("STANDARD: suma = %.2f lei, numar = %d%n",   sumaStandard,   nrStandard);
        if (nrDiscounted > 0) System.out.printf("DISCOUNTED: suma = %.2f lei, numar = %d%n", sumaDiscounted, nrDiscounted);
        if (nrGift       > 0) System.out.printf("GIFT: suma = %.2f lei, numar = %d%n",       sumaGift,       nrGift);
        System.out.printf("Total platit: %.2f lei%n", sumaStandard + sumaDiscounted + sumaGift);
    }
}
