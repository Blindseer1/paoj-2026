package com.pao.project.exception;

public class ContNegasitException extends Exception {

    private final String iban;

    public ContNegasitException(String iban) {
        super("Contul cu IBAN-ul '" + iban + "' nu a fost gasit in sistem.");
        this.iban = iban;
    }

    public String getIban() { return iban; }
}
