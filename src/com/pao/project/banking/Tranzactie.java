package com.pao.project.banking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Tranzactie {

    public enum TipTranzactie {
        DEPUNERE, RETRAGERE, TRANSFER
    }

    private final String id;
    private final String ibanSursa;
    private final String ibanDestinatie;
    private final double suma;
    private final String descriere;
    private final LocalDateTime data;
    private final TipTranzactie tip;

    public Tranzactie(String id, String ibanSursa, String ibanDestinatie,
                      double suma, String descriere, TipTranzactie tip) {
        this.id              = id;
        this.ibanSursa       = ibanSursa;
        this.ibanDestinatie  = (ibanDestinatie != null) ? ibanDestinatie : "-";
        this.suma            = suma;
        this.descriere       = descriere;
        this.data            = LocalDateTime.now();
        this.tip             = tip;
    }

    public String getId()              { return id; }
    public String getIbanSursa()       { return ibanSursa; }
    public String getIbanDestinatie()  { return ibanDestinatie; }
    public double getSuma()            { return suma; }
    public String getDescriere()       { return descriere; }
    public LocalDateTime getData()     { return data; }
    public TipTranzactie getTip()      { return tip; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return String.format("Tranzactie{id='%s', tip=%-10s, suma=%10.2f RON, sursa='%s', dest='%s', data=%s, desc='%s'}",
                id, tip, suma, ibanSursa, ibanDestinatie, data.format(fmt), descriere);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tranzactie)) return false;
        return id.equals(((Tranzactie) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
