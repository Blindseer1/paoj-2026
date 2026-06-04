package com.pao.project.repository;

import com.pao.project.banking.*;
import com.pao.project.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContRepository implements Repository<Cont, String> {

    private Connection conn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Cont cont) {
        String sql = """
                INSERT INTO conturi
                    (iban, tip, sold, titular, limita_overdraft,
                     rata_dobanda, sold_minim, utilizator_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, cont.getIban());
            ps.setString(4, cont.getTitular());
            ps.setDouble(3, cont.getSold());

            if (cont instanceof ContCurent cc) {
                ps.setString(2, "CURENT");
                ps.setDouble(5, cc.getLimitaOverdraft());
                ps.setNull(6, Types.DECIMAL);
                ps.setNull(7, Types.DECIMAL);
            } else if (cont instanceof ContEconomii ce) {
                ps.setString(2, "ECONOMII");
                ps.setNull(5, Types.DECIMAL);
                ps.setDouble(6, ce.getRataDobanda());
                ps.setDouble(7, ce.getSoldMinim());
            }
            // utilizator_id: caller must set via the overloaded save below
            ps.setNull(8, Types.VARCHAR);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("save(Cont) failed: " + e.getMessage(), e);
        }
    }

    /** Saves a cont and links it to the given utilizator id in one call. */
    public void save(Cont cont, String utilizatorId) {
        String sql = """
                INSERT INTO conturi
                    (iban, tip, sold, titular, limita_overdraft,
                     rata_dobanda, sold_minim, utilizator_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, cont.getIban());
            ps.setDouble(3, cont.getSold());
            ps.setString(4, cont.getTitular());
            ps.setString(8, utilizatorId);

            if (cont instanceof ContCurent cc) {
                ps.setString(2, "CURENT");
                ps.setDouble(5, cc.getLimitaOverdraft());
                ps.setNull(6, Types.DECIMAL);
                ps.setNull(7, Types.DECIMAL);
            } else if (cont instanceof ContEconomii ce) {
                ps.setString(2, "ECONOMII");
                ps.setNull(5, Types.DECIMAL);
                ps.setDouble(6, ce.getRataDobanda());
                ps.setDouble(7, ce.getSoldMinim());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("save(Cont, utilizatorId) failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Cont> findById(String iban) {
        String sql = "SELECT * FROM conturi WHERE iban = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, iban);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("findById(Cont) failed: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Cont> findAll() {
        String sql = "SELECT * FROM conturi";
        List<Cont> list = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("findAll(Cont) failed: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void update(Cont cont) {
        String sql = """
                UPDATE conturi
                SET sold = ?, limita_overdraft = ?, rata_dobanda = ?, sold_minim = ?
                WHERE iban = ?
                """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setDouble(1, cont.getSold());
            if (cont instanceof ContCurent cc) {
                ps.setDouble(2, cc.getLimitaOverdraft());
                ps.setNull(3, Types.DECIMAL);
                ps.setNull(4, Types.DECIMAL);
            } else if (cont instanceof ContEconomii ce) {
                ps.setNull(2, Types.DECIMAL);
                ps.setDouble(3, ce.getRataDobanda());
                ps.setDouble(4, ce.getSoldMinim());
            }
            ps.setString(5, cont.getIban());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("update(Cont) failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String iban) {
        String sql = "DELETE FROM conturi WHERE iban = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, iban);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("delete(Cont) failed: " + e.getMessage(), e);
        }
    }

    // ── JOIN queries ──────────────────────────────────────────────────────────

    /**
     * JOIN 2 — Toate conturile cu numele utilizatorului proprietar.
     */
    public List<String> conturiCuProprietar() {
        String sql = """
                SELECT c.iban, c.tip, c.sold, u.nume
                FROM conturi c
                JOIN utilizatori u ON u.id = c.utilizator_id
                ORDER BY u.nume, c.iban
                """;
        List<String> result = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(String.format("%-34s %-8s %10.2f RON  [%s]",
                        rs.getString("iban"),
                        rs.getString("tip"),
                        rs.getDouble("sold"),
                        rs.getString("nume")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("conturiCuProprietar() failed: " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * JOIN 3 — Top conturi dupa volumul total transferat (sursa), cu proprietarul.
     */
    public List<String> topConturiDupaVolumTransferuri() {
        String sql = """
                SELECT c.iban, u.nume, COALESCE(SUM(t.suma), 0) AS total_transferat
                FROM conturi c
                JOIN utilizatori u ON u.id = c.utilizator_id
                LEFT JOIN tranzactii t ON t.iban_sursa = c.iban
                GROUP BY c.iban, u.nume
                ORDER BY total_transferat DESC
                LIMIT 5
                """;
        List<String> result = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(String.format("%-34s %-20s total: %10.2f RON",
                        rs.getString("iban"),
                        rs.getString("nume"),
                        rs.getDouble("total_transferat")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("topConturiDupaVolumTransferuri() failed: " + e.getMessage(), e);
        }
        return result;
    }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private Cont map(ResultSet rs) throws SQLException {
        String tip  = rs.getString("tip");
        String iban = rs.getString("iban");
        String tit  = rs.getString("titular");
        double sold = rs.getDouble("sold");

        if ("CURENT".equals(tip)) {
            return new ContCurent(iban, tit, sold, rs.getDouble("limita_overdraft"));
        } else {
            return new ContEconomii(iban, tit, sold,
                    rs.getDouble("rata_dobanda"),
                    rs.getDouble("sold_minim"));
        }
    }
}
