package com.pao.project.service;

import com.pao.project.banking.*;
import com.pao.project.repository.UtilizatorRepository; // Import the repository
import java.util.*;

public class UtilizatorService {

    private static UtilizatorService instance;

    private final Map<String, Utilizator> utilizatori;
    
    // Declare the repository
    private final UtilizatorRepository utilizatorRepository;

    private UtilizatorService() {
        this.utilizatori = new HashMap<>();
        this.utilizatorRepository = new UtilizatorRepository(); // Initialize repository
        
        // Optional but recommended: Sync existing database records back into your memory map on startup
        try {
            for (Utilizator u : utilizatorRepository.findAll()) {
                this.utilizatori.put(u.getId(), u);
            }
        } catch (Exception e) {
            System.out.println("[UtilizatorService] Atentie: Nu s-au putut incarca utilizatorii din baza de date: " + e.getMessage());
        }
    }

    public static UtilizatorService getInstance() {
        if (instance == null) {
            instance = new UtilizatorService();
        }
        return instance;
    }

    // ── CRUD de baza ─────────────────────────────────────────────────────────

    /** Adauga un utilizator in sistem (Memorie + DB). */
    public void adaugaUtilizator(Utilizator utilizator) {
        if (utilizator == null) throw new IllegalArgumentException("Utilizatorul nu poate fi null.");
        
        // 1. Save to database first so if it fails, memory remains clean
        utilizatorRepository.save(utilizator);
        
        // 2. Save to your local cache map
        utilizatori.put(utilizator.getId(), utilizator);
        System.out.println("[UtilizatorService] Utilizator adaugat in memorie si DB: " + utilizator);
    }

    /** Sterge un utilizator dupa ID. */
    public void stergeUtilizator(String id) {
        if (!utilizatori.containsKey(id)) {
            System.out.println("[UtilizatorService] Utilizatorul cu id '" + id + "' nu exista.");
            return;
        }
        
        // Optional: If your repository supports delete, invoke it here:
        // utilizatorRepository.deleteById(id); 

        utilizatori.remove(id);
        System.out.println("[UtilizatorService] Utilizator sters: " + id);
    }

    /** Cauta un utilizator dupa ID. Returneaza null daca nu exista. */
    public Utilizator cautaDupaId(String id) {
        // First look in cache memory
        if (utilizatori.containsKey(id)) {
            return utilizatori.get(id);
        }
        
        // Fallback: Check the database if it's missing from memory cache
        Optional<Utilizator> dbUser = utilizatorRepository.findById(id);
        if (dbUser.isPresent()) {
            utilizatori.put(id, dbUser.get());
            return dbUser.get();
        }
        
        return null;
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
