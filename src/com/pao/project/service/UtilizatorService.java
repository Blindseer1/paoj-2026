package com.pao.project.service;
import com.pao.project.banking.*;
import java.util.*;

/**
 * Singleton service care gestioneaza utilizatorii bancii.
 * Indexeaza utilizatorii dupa ID intr-un Map<String, Utilizator>.
 */
public class UtilizatorService {

    private static UtilizatorService instance;

    /** Map<ID, Utilizator> – indexare rapida dupa id unic. */
    private final Map<String, Utilizator> utilizatori;

    private UtilizatorService() {
        this.utilizatori = new HashMap<>();
    }

    public static UtilizatorService getInstance() {
        if (instance == null) {
            instance = new UtilizatorService();
        }
        return instance;
    }

    // ── CRUD de baza ─────────────────────────────────────────────────────────

    /** Adauga un utilizator in sistem. */
    public void adaugaUtilizator(Utilizator utilizator) {
        if (utilizator == null) throw new IllegalArgumentException("Utilizatorul nu poate fi null.");
        utilizatori.put(utilizator.getId(), utilizator);
        System.out.println("[UtilizatorService] Utilizator adaugat: " + utilizator);
    }

    /** Sterge un utilizator dupa ID. */
    public void stergeUtilizator(String id) {
        if (!utilizatori.containsKey(id)) {
            System.out.println("[UtilizatorService] Utilizatorul cu id '" + id + "' nu exista.");
            return;
        }
        utilizatori.remove(id);
        System.out.println("[UtilizatorService] Utilizator sters: " + id);
    }

    /** Cauta un utilizator dupa ID. Returneaza null daca nu exista. */
    public Utilizator cautaDupaId(String id) {
        return utilizatori.get(id);
    }

    /** Cauta utilizatori al caror nume contine sirul dat (case-insensitive). */
    public List<Utilizator> cautaDupaNume(String fragment) {
        List<Utilizator> rezultate = new ArrayList<>();
        for (Utilizator u : utilizatori.values()) {
            if (u.getNume().toLowerCase().contains(fragment.toLowerCase())) {
                rezultate.add(u);
            }
        }
        return rezultate;
    }

    /** Returneaza toti utilizatorii (read-only). */
    public Collection<Utilizator> listeazaTotiUtilizatorii() {
        return Collections.unmodifiableCollection(utilizatori.values());
    }

    // ── Relatia utilizator–cont ───────────────────────────────────────────────

    /** Asociaza un cont unui utilizator. */
    public void asociazaContUtilizator(String idUtilizator, Cont cont) {
        Utilizator u = cautaDupaId(idUtilizator);
        if (u == null) {
            System.out.println("[UtilizatorService] Utilizatorul '" + idUtilizator + "' nu exista.");
            return;
        }
        u.adaugaCont(cont);
        System.out.println("[UtilizatorService] Contul " + cont.getIban()
                + " asociat utilizatorului " + u.getNume());
    }

    /** Afiseaza toate conturile unui utilizator, sortate dupa IBAN (TreeSet). */
    public void afiseazaConturiUtilizator(String idUtilizator) {
        Utilizator u = cautaDupaId(idUtilizator);
        if (u == null) {
            System.out.println("[UtilizatorService] Utilizatorul '" + idUtilizator + "' nu exista.");
            return;
        }
        System.out.println("Conturile lui " + u.getNume() + " (sortate dupa IBAN):");
        if (u.getConturi().isEmpty()) {
            System.out.println("  (niciun cont)");
            return;
        }
        for (Cont c : u.getConturi()) {
            System.out.println("  " + c);
        }
    }
}
