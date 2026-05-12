package com.pao.project.banking;

import com.pao.project.exception.ContNegasitException;
import com.pao.project.exception.FonduriInsuficienteException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ContService {

    private static ContService instance;

    private final Map<String, Cont> conturi;
    private int contorTranzactii;

    private ContService() {
        this.conturi            = new HashMap<>();
        this.contorTranzactii   = 0;
    }

    public static ContService getInstance() {
        if (instance == null) {
            instance = new ContService();
        }
        return instance;
    }

    private String genereazaIdTranzactie() {
        return String.format("TRX%06d", ++contorTranzactii);
    }

    public void adaugaCont(Cont cont) {
        if (cont == null) throw new IllegalArgumentException("Contul nu poate fi null.");
        conturi.put(cont.getIban(), cont);
        System.out.println("[ContService] Cont adaugat: " + cont);
    }

    public void stergeCont(String iban) throws ContNegasitException {
        cautaDupaIban(iban); // arunca exceptie daca nu exista
        conturi.remove(iban);
        System.out.println("[ContService] Cont sters: " + iban);
    }

    /** Cauta un cont dupa IBAN; arunca ContNegasitException daca nu exista. */
    public Cont cautaDupaIban(String iban) throws ContNegasitException {
        Cont cont = conturi.get(iban);
        if (cont == null) throw new ContNegasitException(iban);
        return cont;
    }

    /** Returneaza toate conturile din sistem (read-only). */
    public Collection<Cont> listeazaToateConturile() {
        return Collections.unmodifiableCollection(conturi.values());
    }

    // ── Operatiuni financiare ─────────────────────────────────────────────────

    /** Depune o suma intr-un cont. */
    public void depune(String iban, double suma) throws ContNegasitException {
        if (suma <= 0) throw new IllegalArgumentException("Suma de depus trebuie sa fie pozitiva.");
        Cont cont = cautaDupaIban(iban);
        cont.setSold(cont.getSold() + suma);
        Tranzactie t = new Tranzactie(
                genereazaIdTranzactie(), iban, null, suma,
                "Depunere numerar", Tranzactie.TipTranzactie.DEPUNERE);
        cont.adaugaTranzactie(t);
        System.out.printf("[ContService] Depunere: +%.2f RON in %s. Sold nou: %.2f RON%n",
                suma, iban, cont.getSold());
    }

    /** Retrage o suma dintr-un cont, respectand regulile fiecarui tip de cont. */
    public void retrage(String iban, double suma)
            throws ContNegasitException, FonduriInsuficienteException {
        if (suma <= 0) throw new IllegalArgumentException("Suma de retras trebuie sa fie pozitiva.");
        Cont cont = cautaDupaIban(iban);

        // Calculeaza soldul disponibil in functie de tipul contului
        double disponibil;
        if (cont instanceof ContCurent) {
            disponibil = ((ContCurent) cont).getSoldDisponibil(); // sold + overdraft
        } else if (cont instanceof ContEconomii) {
            double soldMinim = ((ContEconomii) cont).getSoldMinim();
            disponibil = cont.getSold() - soldMinim;              // nu se coboara sub sold minim
        } else {
            disponibil = cont.getSold();
        }

        if (suma > disponibil) {
            throw new FonduriInsuficienteException(disponibil, suma);
        }

        cont.setSold(cont.getSold() - suma);
        Tranzactie t = new Tranzactie(
                genereazaIdTranzactie(), iban, null, suma,
                "Retragere numerar", Tranzactie.TipTranzactie.RETRAGERE);
        cont.adaugaTranzactie(t);
        System.out.printf("[ContService] Retragere: -%.2f RON din %s. Sold nou: %.2f RON%n",
                suma, iban, cont.getSold());
    }

    /** Transfera o suma intre doua conturi. */
    public void transfer(String ibanSursa, String ibanDestinatie, double suma)
            throws ContNegasitException, FonduriInsuficienteException {
        if (suma <= 0) throw new IllegalArgumentException("Suma de transferat trebuie sa fie pozitiva.");
        Cont sursa      = cautaDupaIban(ibanSursa);
        Cont destinatie = cautaDupaIban(ibanDestinatie);

        if (sursa.getSold() < suma) {
            throw new FonduriInsuficienteException(sursa.getSold(), suma);
        }

        sursa.setSold(sursa.getSold() - suma);
        destinatie.setSold(destinatie.getSold() + suma);

        Tranzactie tSursa = new Tranzactie(
                genereazaIdTranzactie(), ibanSursa, ibanDestinatie, suma,
                "Transfer catre " + ibanDestinatie, Tranzactie.TipTranzactie.TRANSFER);
        Tranzactie tDest = new Tranzactie(
                genereazaIdTranzactie(), ibanSursa, ibanDestinatie, suma,
                "Transfer primit de la " + ibanSursa, Tranzactie.TipTranzactie.TRANSFER);

        sursa.adaugaTranzactie(tSursa);
        destinatie.adaugaTranzactie(tDest);

        System.out.printf("[ContService] Transfer: %.2f RON de la %s la %s%n",
                suma, ibanSursa, ibanDestinatie);
    }

    // ── Card ─────────────────────────────────────────────────────────────────

    /** Emite un card pentru un cont dat. */
    public void emiteCard(String iban, Card card) throws ContNegasitException {
        Cont cont = cautaDupaIban(iban);
        cont.adaugaCard(card);
        System.out.println("[ContService] Card emis pentru " + iban + ": " + card);
    }

    // ── Extras de cont ────────────────────────────────────────────────────────

    /**
     * Genereaza un extras de cont pentru intervalul [dataStart, dataFinal].
     * Filtreaza tranzactiile dupa data.
     */
    public ExtrasCont genereazaExtras(String iban, LocalDate dataStart, LocalDate dataFinal)
            throws ContNegasitException {
        Cont cont = cautaDupaIban(iban);
        ExtrasCont extras = new ExtrasCont(iban, dataStart, dataFinal, cont.getSold());

        for (Tranzactie t : cont.getTranzactii()) {
            LocalDate dataTranzactie = t.getData().toLocalDate();
            if (!dataTranzactie.isBefore(dataStart) && !dataTranzactie.isAfter(dataFinal)) {
                extras.adaugaTranzactie(t);
            }
        }
        extras.setSoldFinal(cont.getSold());
        System.out.println("[ContService] Extras generat pentru " + iban);
        return extras;
    }
}
