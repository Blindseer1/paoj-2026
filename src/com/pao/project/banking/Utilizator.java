package com.pao.project.banking;

import java.util.TreeSet;

public class Utilizator {

    private final String id;
    private String nume;
    private String email;
    private final String cnp;

    private final TreeSet<Cont> conturi;

    public Utilizator(String id, String nume, String email, String cnp) {
        this.id      = id;
        this.nume    = nume;
        this.email   = email;
        this.cnp     = cnp;
        this.conturi = new TreeSet<>();
    }

    public String getId()           { return id; }
    public String getNume()         { return nume; }
    public String getEmail()        { return email; }
    public String getCnp()          { return cnp; }
    public TreeSet<Cont> getConturi() { return conturi; }

    public void setNume(String nume)   { this.nume  = nume; }
    public void setEmail(String email) { this.email = email; }

    public void adaugaCont(Cont cont) { conturi.add(cont); }
    public void stergeCont(Cont cont) { conturi.remove(cont); }

    @Override
    public String toString() {
        return String.format("Utilizator{id='%s', nume='%s', email='%s', nrConturi=%d}",
                id, nume, email, conturi.size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator)) return false;
        return id.equals(((Utilizator) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
