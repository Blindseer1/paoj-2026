package com.pao.project.banking;
import com.pao.project.exception.*;
import com.pao.project.service.*;

import java.time.LocalDate;
import java.util.List;

public class Main {

    private static final String SEP =" ";

    public static void main(String[] args) {
        ContService       contService       = ContService.getInstance();
        UtilizatorService utilizatorService = UtilizatorService.getInstance();

       separator("1. Adaugare utilizatori");
        Utilizator u1 = new Utilizator("U001", "Ion Popescu",   "ion.popescu@email.com",   "1900101123456");
        Utilizator u2 = new Utilizator("U002", "Maria Ionescu", "maria.ionescu@email.com", "2950315654321");
        Utilizator u3 = new Utilizator("U003", "Andrei Popa",   "andrei.popa@email.com",   "1850720789012");
        utilizatorService.adaugaUtilizator(u1);
        utilizatorService.adaugaUtilizator(u2);
        utilizatorService.adaugaUtilizator(u3);

       separator("2. Deschidere conturi curente");

        ContCurent cc1 = new ContCurent("RO49AAAA1B31007593840000", "Ion Popescu",   1000.00, 500.00);
        ContCurent cc2 = new ContCurent("RO49AAAA1B31007593840001", "Maria Ionescu", 2500.00, 300.00);
        ContCurent cc3 = new ContCurent("RO49AAAA1B31007593840004", "Andrei Popa",    500.00, 200.00);
        contService.adaugaCont(cc1);
        contService.adaugaCont(cc2);
        contService.adaugaCont(cc3);
        utilizatorService.asociazaContUtilizator("U001", cc1);
        utilizatorService.asociazaContUtilizator("U002", cc2);
        utilizatorService.asociazaContUtilizator("U003", cc3);

        separator("3. Deschidere cont de economii");

        ContEconomii ce1 = new ContEconomii("RO49AAAA1B31007593840002", "Ion Popescu",   5000.00, 3.5, 100.0);
        ContEconomii ce2 = new ContEconomii("RO49AAAA1B31007593840003", "Maria Ionescu", 8000.00, 4.0, 200.0);
        contService.adaugaCont(ce1);
        contService.adaugaCont(ce2);
        utilizatorService.asociazaContUtilizator("U001", ce1);
        utilizatorService.asociazaContUtilizator("U002", ce2);
        System.out.printf("Dobanda anuala Ion Popescu (economii): %.2f RON%n",
                ce1.calculeazaDobandaAnuala());

        separator("4. Emitere card debit");

        CardDebit cd1 = new CardDebit("4111111111111111", "123",
                LocalDate.now().plusYears(3), "ION POPESCU", 2000.0);
        CardDebit cd2 = new CardDebit("4111111111112222", "456",
                LocalDate.now().plusYears(3), "MARIA IONESCU", 3000.0);
        try {
            contService.emiteCard("RO49AAAA1B31007593840000", cd1);
            contService.emiteCard("RO49AAAA1B31007593840001", cd2);
        } catch (ContNegasitException e) {
            System.out.println("Eroare emitere card debit: " + e.getMessage());
        }

        separator("5. Emitere card credit");

        CardCredit ccr1 = new CardCredit("5500005555555559", "789",
                LocalDate.now().plusYears(2), "ION POPESCU", 5000.0);
        try {
            contService.emiteCard("RO49AAAA1B31007593840000", ccr1);
        } catch (ContNegasitException e) {
            System.out.println("Eroare emitere card credit: " + e.getMessage());
        }
        separator("6. Depunere bani in cont");
        try {
            contService.depune("RO49AAAA1B31007593840000", 750.00);
            contService.depune("RO49AAAA1B31007593840001", 1200.00);
        } catch (ContNegasitException e) {
            System.out.println("Eroare depunere: " + e.getMessage());
        }
        separator("7. Retragere bani din cont");
        try {
            contService.retrage("RO49AAAA1B31007593840000", 300.00);
        } catch (ContNegasitException | FonduriInsuficienteException e) {
            System.out.println("Eroare retragere: " + e.getMessage());
        }
        System.out.println("\n  [Excep] Incercare retragere cu fonduri insuficiente:");
        try {
            contService.retrage("RO49AAAA1B31007593840004", 99999.00);
        } catch (ContNegasitException e) {
            System.out.println("  Eroare cont: " + e.getMessage());
        } catch (FonduriInsuficienteException e) {
            System.out.println("  Exceptie prinsa -> " + e.getMessage());
        }
        separator("8. Transfer intre conturi");
        try {
            contService.transfer("RO49AAAA1B31007593840000",
                                 "RO49AAAA1B31007593840001", 500.00);
            contService.transfer("RO49AAAA1B31007593840002",
                                 "RO49AAAA1B31007593840003", 200.00);
        } catch (ContNegasitException | FonduriInsuficienteException e) {
            System.out.println("Eroare transfer: " + e.getMessage());
        }
        separator("9. Generare extras de cont");
        try {
            ExtrasCont extras = contService.genereazaExtras(
                    "RO49AAAA1B31007593840000",
                    LocalDate.now().minusMonths(1),
                    LocalDate.now());
            System.out.println(extras);
        } catch (ContNegasitException e) {
            System.out.println("Eroare extras: " + e.getMessage());
        }

        separator("10. Listare conturi utilizator (sortate dupa IBAN)");

        utilizatorService.afiseazaConturiUtilizator("U001");
        System.out.println();
        utilizatorService.afiseazaConturiUtilizator("U002");


        separator("Bonus: Toti utilizatorii");
        for (Utilizator u : utilizatorService.listeazaTotiUtilizatorii()) {
            System.out.println("  " + u);
        }

        System.out.println(SEP + "   FIN" + SEP);
    }

    private static void separator(String titlu) {
        System.out.println("  " + titlu);
    }
}
