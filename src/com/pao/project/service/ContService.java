package com.pao.project.service;

import com.pao.project.exception.ContNegasitException;
import com.pao.project.exception.FonduriInsuficienteException;
import com.pao.project.banking.*;
import com.pao.project.repository.ContRepository;
import com.pao.project.repository.TranzactieRepository;
import com.pao.project.repository.CardRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public class ContService {

    private static ContService instance;

    // Stage II Repositories 
    private final ContRepository contRepository;
    private final TranzactieRepository tranzactieRepository;
    private final CardRepository cardRepository;

    private ContService() {
        this.contRepository = new ContRepository();
        this.tranzactieRepository = new TranzactieRepository();
        this.cardRepository = new CardRepository();
    }

    public static synchronized ContService getInstance() {
        if (instance == null) {
            instance = new ContService();
        }
        return instance;
    }

    // Generates a unique secure transaction ID instead of an unstable local counter
    private String genereazaIdTranzactie() {
        return "TRX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // ── Actiunea 2 / 3: Deschide Cont ────────────────────────────────────────
    public void adaugaCont(Cont cont, String utilizatorId) {
        if (cont == null) throw new IllegalArgumentException("Contul nu poate fi null.");
        
        // Pushes data straight to MySQL
        contRepository.save(cont, utilizatorId);

        if (cont instanceof ContCurent) {
            AuditService.getInstance().log(AuditService.DESCHIDE_CONT_CURENT);
        } else if (cont instanceof ContEconomii) {
            AuditService.getInstance().log(AuditService.DESCHIDE_CONT_ECONOMII);
        }

        System.out.println("[ContService] Cont salvat in baza de date: " + cont);
    }

    public void stergeCont(String iban) throws ContNegasitException {
        cautaDupaIban(iban); // Validates presence first to throw exception if missing
        contRepository.delete(iban);
        System.out.println("[ContService] Cont sters din baza de date: " + iban);
    }

    public Cont cautaDupaIban(String iban) throws ContNegasitException {
        return contRepository.findById(iban)
                .orElseThrow(() -> new ContNegasitException(iban));
    }

    public Collection<Cont> listeazaToateConturile() {
        return contRepository.findAll();
    }

    // ── Actiunea 6: Depune Bani ──────────────────────────────────────────────
    public void depune(String iban, double suma) throws ContNegasitException {
        if (suma <= 0) throw new IllegalArgumentException("Suma de depus trebuie sa fie pozitiva.");
        Cont cont = cautaDupaIban(iban);
        
        cont.setSold(cont.getSold() + suma);
        contRepository.update(cont);

        Tranzactie t = new Tranzactie(
                genereazaIdTranzactie(), iban, null, suma,
                "Depunere numerar", Tranzactie.TipTranzactie.DEPUNERE);
        tranzactieRepository.save(t);

        AuditService.getInstance().log(AuditService.DEPUNE_BANI);

        System.out.printf("[ContService] Depunere: +%.2f RON in %s. Sold nou: %.2f RON%n",
                suma, iban, cont.getSold());
    }

    // ── Actiunea 7: Retrage Bani ─────────────────────────────────────────────
    public void retrage(String iban, double suma)
            throws ContNegasitException, FonduriInsuficienteException {
        if (suma <= 0) throw new IllegalArgumentException("Suma de retras trebuie sa fie pozitiva.");
        Cont cont = cautaDupaIban(iban);

        double disponibil = cont.getSold();
        if (cont instanceof ContCurent cc) {
            disponibil = cc.getSoldDisponibil();
        } else if (cont instanceof ContEconomii ce) {
            disponibil = cont.getSold() - ce.getSoldMinim();
        }

        if (suma > disponibil) {
            throw new FonduriInsuficienteException(disponibil, suma);
        }

        cont.setSold(cont.getSold() - suma);
        contRepository.update(cont);

        Tranzactie t = new Tranzactie(
                genereazaIdTranzactie(), iban, null, suma,
                "Retragere numerar", Tranzactie.TipTranzactie.RETRAGERE);
        tranzactieRepository.save(t);

        AuditService.getInstance().log(AuditService.RETRAGE_BANI);

        System.out.printf("[ContService] Retragere: -%.2f RON din %s. Sold nou: %.2f RON%n",
                suma, iban, cont.getSold());
    }

    // ── Actiunea 8: Explicit JDBC Transaction (Transfer) ─────────────────────
    public void transfer(String ibanSursa, String ibanDestinatie, double suma)
            throws ContNegasitException, FonduriInsuficienteException {
        if (suma <= 0) throw new IllegalArgumentException("Suma de transferat trebuie sa fie pozitiva.");
        
        Cont sursa = cautaDupaIban(ibanSursa);
        Cont destinatie = cautaDupaIban(ibanDestinatie);

        if (sursa.getSold() < suma) {
            throw new FonduriInsuficienteException(sursa.getSold(), suma);
        }

        String txIdSursa = genereazaIdTranzactie();
        String txIdDest = genereazaIdTranzactie();

        Tranzactie tSursa = new Tranzactie(
                txIdSursa, ibanSursa, ibanDestinatie, suma,
                "Transfer catre " + ibanDestinatie, Tranzactie.TipTranzactie.TRANSFER);
        Tranzactie tDest = new Tranzactie(
                txIdDest, ibanSursa, ibanDestinatie, suma,
                "Transfer primit de la " + ibanSursa, Tranzactie.TipTranzactie.TRANSFER);

        try {
            // Triggers your custom explicit multi-table database transaction block
            tranzactieRepository.executeazaTransfer(tSursa, tDest, suma);
            
            AuditService.getInstance().log(AuditService.TRANSFER);

            System.out.printf("[ContService] Transfer realizat JDBC: %.2f RON de la %s la %s%n",
                    suma, ibanSursa, ibanDestinatie);
        } catch (SQLException e) {
            throw new RuntimeException("Eroare fatala intampinata la transfer. Rollback DB executat: " + e.getMessage(), e);
        }
    }

    // ── Actiunea 4 / 5: Emite Card ───────────────────────────────────────────
    public void emiteCard(String iban, Card card) throws ContNegasitException {
        // Validation check ensures account exists to satisfy the "Fara NullPointerException" rule
        cautaDupaIban(iban); 
        
        cardRepository.save(card, iban);

        if (card instanceof CardDebit) {
            AuditService.getInstance().log(AuditService.EMITE_CARD_DEBIT);
        } else if (card instanceof CardCredit) {
            AuditService.getInstance().log(AuditService.EMITE_CARD_CREDIT);
        }

        System.out.println("[ContService] Card salvat in baza de date pentru " + iban + ": " + card);
    }

    // ── Actiunea 9: Genereaza Extras Cont ────────────────────────────────────
    public ExtrasCont genereazaExtras(String iban, LocalDate dataStart, LocalDate dataFinal)
            throws ContNegasitException {
        Cont cont = cautaDupaIban(iban); 
        
        ExtrasCont extras = new ExtrasCont(iban, dataStart, dataFinal, cont.getSold());

        // Queries the cold database storage dynamically to gather operational logs
        for (Tranzactie t : tranzactieRepository.findAll()) {
            if (iban.equals(t.getIbanSursa()) || iban.equals(t.getIbanDestinatie())) {
                LocalDate dataTranzactie = t.getData().toLocalDate();
                
                if (!dataTranzactie.isBefore(dataStart) && !dataTranzactie.isAfter(dataFinal)) {
                    extras.adaugaTranzactie(t);
                }
            }
        }
        extras.setSoldFinal(cont.getSold());

        AuditService.getInstance().log(AuditService.GENEREAZA_EXTRAS);
        System.out.println("[ContService] Extras generat folosind datele din tabele pentru " + iban);
        return extras;
    }
}
