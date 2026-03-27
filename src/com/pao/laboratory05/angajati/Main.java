package com.pao.laboratory05.angajati;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AngajatService service = AngajatService.getInstance();

        while (true) {
            System.out.println("\n===== Gestionare Angajați =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("4. Afișează toți angajații");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");

            int opt = Integer.parseInt(scanner.nextLine());

            switch (opt) {
                case 1:
                    System.out.print("Nume angajat: ");
                    String nume = scanner.nextLine();

                    System.out.print("Departament: ");
                    String dept = scanner.nextLine();

                    System.out.print("Locație departament: ");
                    String locatie = scanner.nextLine();

                    System.out.print("Salariu: ");
                    double salariu = Double.parseDouble(scanner.nextLine());

                    Departament d = new Departament(dept, locatie);
                    Angajat a = new Angajat(nume, d, salariu);

                    service.addAngajat(a);
                    break;

                case 2:
                    service.listBySalary();
                    break;

                case 3:
                    System.out.print("Nume departament: ");
                    String cautare = scanner.nextLine();
                    service.findByDepartament(cautare);
                    break;

                case 4:
                    service.printAll();
                    break;

                case 0:
                    System.out.println("Ieșire...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Opțiune invalidă!");
            }
        }
    }
}
