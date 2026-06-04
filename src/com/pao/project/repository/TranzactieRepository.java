package com.pao.project.repository;

import com.pao.project.banking.Tranzactie;
import com.pao.project.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TranzactieRepository implements Repository<Tranzactie, String> {

    private Connection conn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Tranzactie t) {
        String sql = """
                INSERT INTO tranzactii
                    (id, tip, suma, descriere, data_ora, iban_sursa, iban_destinatie)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, t.getId());
            ps.setString(2, t.getTip().name());
            ps.setDouble(3, t.getSuma());
            ps.setString(4, t.getDescriere());
            ps.setTimestamp(5, Timestamp.valueOf(t.getData()));
            ps.setString(6, t.getIbanSursa());
            String dest = t.getIbanDestinatie();
            if (dest == null || dest.equals("-")) ps.setNull(7, Types.VARCHAR);
            else ps.setString(7, dest);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("save(Tranzactie) failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Tranzactie> findById(String id) {
        String sql = "SELECT * FROM tranzactii WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("findById(Tranzactie) failed: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Tranzactie> findAll() {
        String sql = "SELECT * FROM tranzactii ORDER BY data_ora DESC";
        List<Tranzactie> list = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("findAll(Tranzactie) failed: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void update(Tranzactie t) {
        // Tranzactiile sunt imutabile — update nu este suportat
        throw new UnsupportedOperationException("Tranzactiile nu pot fi modificate.");
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM tranzactii WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("delete(Tranzactie) failed: " + e.getMessage(), e);
        }
    }

    // ── Tranzactie JDBC explicita: transfer intre doua conturi ────────────────
    //
    //  Aceasta metoda:
    //    1. Scade suma din contul sursa
    //    2. Adauga suma in contul destinatie
    //    3. Insereaza doua randuri in tranzactii
    //  Totul intr-o singura tranzactie DB cu commit/rollback explicit.

    public void executeazaTransfer(Tranzactie tSursa, Tranzactie tDest, double suma)
            throws SQLException {

        Connection c = conn();
        c.setAutoCommit(false);
        try {
            // 1. Actualizeaza soldul contului sursa
            String updateSursa = "UPDATE conturi SET sold = sold - ? WHERE iban = ?";
            try (PreparedStatement ps = c.prepareStatement(updateSursa)) {
                ps.setDouble(1, suma);
                ps.setString(2, tSursa.getIbanSursa());
                ps.executeUpdate();
            }

            // 2. Actualizeaza soldul contului destinatie
            String updateDest = "UPDATE conturi SET sold = sold + ? WHERE iban = ?";
            try (PreparedStatement ps = c.prepareStatement(updateDest)) {
                ps.setDouble(1, suma);
                ps.setString(2, tDest.getIbanSursa()); // sursa din perspectiva destinatarului
                ps.executeUpdate();
            }

            // 3. Inregistreaza ambele tranzactii
            save(tSursa);
            save(tDest);

            c.commit();
        } catch (SQLException e) {
            c.rollback();
            throw e;
        } finally {
            c.setAutoCommit(true);
        }
    }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private Tranzactie map(ResultSet rs) throws SQLException {
        Tranzactie.TipTranzactie tip =
                Tranzactie.TipTranzactie.valueOf(rs.getString("tip"));
        return new Tranzactie(
                rs.getString("id"),
                rs.getString("iban_sursa"),
                rs.getString("iban_destinatie"),
                rs.getDouble("suma"),
                rs.getString("descriere"),
                tip
        );
    }
}
