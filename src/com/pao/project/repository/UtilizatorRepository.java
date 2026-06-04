package com.pao.project.repository;

import com.pao.project.banking.Utilizator;
import com.pao.project.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UtilizatorRepository implements Repository<Utilizator, String> {

    private Connection conn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Utilizator u) {
        String sql = "INSERT INTO utilizatori (id, nume, email, cnp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, u.getId());
            ps.setString(2, u.getNume());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getCnp());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("save(Utilizator) failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Utilizator> findById(String id) {
        String sql = "SELECT * FROM utilizatori WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("findById(Utilizator) failed: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Utilizator> findAll() {
        String sql = "SELECT * FROM utilizatori";
        List<Utilizator> list = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("findAll(Utilizator) failed: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void update(Utilizator u) {
        String sql = "UPDATE utilizatori SET nume = ?, email = ? WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, u.getNume());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("update(Utilizator) failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM utilizatori WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("delete(Utilizator) failed: " + e.getMessage(), e);
        }
    }

    // ── JOIN queries ──────────────────────────────────────────────────────────

    /**
     * JOIN 1 — Fiecare utilizator cu numarul de conturi pe care le detine.
     */
    public List<String> utilizatoriCuNrConturi() {
        String sql = """
                SELECT u.nume, COUNT(c.iban) AS nr_conturi
                FROM utilizatori u
                LEFT JOIN conturi c ON c.utilizator_id = u.id
                GROUP BY u.id, u.nume
                ORDER BY nr_conturi DESC
                """;
        List<String> result = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(rs.getString("nume") + " -> " + rs.getInt("nr_conturi") + " conturi");
            }
        } catch (SQLException e) {
            throw new RuntimeException("utilizatoriCuNrConturi() failed: " + e.getMessage(), e);
        }
        return result;
    }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private Utilizator map(ResultSet rs) throws SQLException {
        return new Utilizator(
                rs.getString("id"),
                rs.getString("nume"),
                rs.getString("email"),
                rs.getString("cnp")
        );
    }
}
