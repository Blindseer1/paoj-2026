package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = citesteStudenti();

        Scanner scanner = new Scanner(System.in);
        String linie = scanner.nextLine().trim();
        String[] parti = linie.split(" ", 2);
        String comanda = parti[0];

        switch (comanda) {
            case "PRINT" -> {
                for (Student s : studenti) {
                    System.out.println(s);
                }
            }
            case "SHALLOW" -> {
                String nume = parti[1].trim();
                Student original = gaseste(studenti, nume);
                Student clona = original.shallowClone();
                clona.getAdresa().setOras("MODIFICAT");
                System.out.println("Original: " + original);
                System.out.println("Clona: " + clona);
            }
            case "DEEP" -> {
                String nume = parti[1].trim();
                Student original = gaseste(studenti, nume);
                Student clona = original.deepClone();
                clona.getAdresa().setOras("MODIFICAT");
                System.out.println("Original: " + original);
                System.out.println("Clona: " + clona);
            }
            default -> System.out.println("Comanda necunoscuta: " + comanda);
        }
    }

    private static List<Student> citesteStudenti() throws IOException {
        List<Student> studenti = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linie;
            while ((linie = br.readLine()) != null) {
                linie = linie.trim();
                if (linie.isEmpty()) continue;
                String[] parti = linie.split(",");
                String nume  = parti[0].trim();
                int varsta   = Integer.parseInt(parti[1].trim());
                String oras  = parti[2].trim();
                String strada = parti[3].trim();
                studenti.add(new Student(nume, varsta, new Adresa(oras, strada)));
            }
        }
        return studenti;
    }

    private static Student gaseste(List<Student> studenti, String nume) {
        return studenti.stream()
                .filter(s -> s.getNume().equals(nume))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Student negasit: " + nume));
    }
}
