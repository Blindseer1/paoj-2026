package com.pao.project.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

public class AuditService {

    public static final String ADAUGA_UTILIZATOR        = "adauga_utilizator";
    public static final String DESCHIDE_CONT_CURENT     = "deschide_cont_curent";
    public static final String DESCHIDE_CONT_ECONOMII   = "deschide_cont_economii";
    public static final String EMITE_CARD_DEBIT         = "emite_card_debit";
    public static final String EMITE_CARD_CREDIT        = "emite_card_credit";
    public static final String DEPUNE_BANI              = "depune_bani";
    public static final String RETRAGE_BANI             = "retrage_bani";
    public static final String TRANSFER                 = "transfer";
    public static final String GENEREAZA_EXTRAS         = "genereaza_extras";
    public static final String AFISEAZA_CONTURI         = "afiseaza_conturi_utilizator";

    private static final String CSV_PATH = "audit.csv";

    private static AuditService instance;
    private final ReentrantLock lock = new ReentrantLock();

    private AuditService() {
    }

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public void log(String numeleActiunii) {
        lock.lock();
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_PATH, true))) {
            pw.printf("%s,%s%n", numeleActiunii, LocalDateTime.now());
        } catch (IOException e) {
            System.err.println("[AuditService] Nu s-a putut scrie in audit.csv: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}
