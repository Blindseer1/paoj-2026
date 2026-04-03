package com.pao.laboratory06.exercise2;

import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();

        List<Colaborator> colaboratori = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine().trim();
            Scanner lineScanner = new Scanner(line);
            String tip = lineScanner.next();
            Colaborator c;
            switch (tip) {
                case "CIM": c = new CIMColaborator(); break;
                case "PFA": c = new PFAColaborator(); break;
                case "SRL": c = new SRLColaborator(); break;
                default: throw new IllegalArgumentException("Tip necunoscut: " + tip);
            }
            c.citeste(lineScanner);
            colaboratori.add(c);
        }

        // 1. Afiseaza in ordinea citirii
        for (Colaborator c : colaboratori) {
            c.afiseaza();
        }
        System.out.println();

        // 2. Colaboratorul cu venitul net anual maxim
        Colaborator max = Collections.max(colaboratori,
                Comparator.comparingDouble(Colaborator::calculeazaVenitNetAnual));
        System.out.print("Colaborator cu venit net maxim: ");
        max.afiseaza();
        System.out.println();

        // 3. Doar persoane juridice (SRL)
        System.out.println("Colaboratori persoane juridice:");
        colaboratori.stream()
                .filter(c -> c instanceof PersoanaJuridica)
                .forEach(Colaborator::afiseaza);
        System.out.println();

        // 4. Suma si numar pe tip
        System.out.println("Sume și număr colaboratori pe tip:");
        for (TipColaborator tip : TipColaborator.values()) {
            List<Colaborator> perTip = colaboratori.stream()
                    .filter(c -> c.getTip() == tip)
                    .collect(Collectors.toList());
            if (perTip.isEmpty()) {
                System.out.printf("%s: suma = nu lei, număr = null%n", tip);
            } else {
                double suma = perTip.stream()
                        .mapToDouble(Colaborator::calculeazaVenitNetAnual)
                        .sum();
                System.out.printf("%s: suma = %.2f lei, număr = %d%n",
                        tip, suma, perTip.size());
            }
        }
    }
}
