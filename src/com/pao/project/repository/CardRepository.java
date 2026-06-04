package com.pao.project.repository;

import com.pao.project.banking.*;
import com.pao.project.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardRepository implements Repository<Card, String> {

    private Connection conn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Card card) {
        throw new UnsupportedOperationException("Foloseste save(card, contIban).");
    }

    public void save(Card card, String contIban) {
        String sql = """
                INSERT INTO carduri
                    (numar_card, tip, cvv, data_expirare, titular_card, activ,
                     limita_zilnica, limita_credit, sold_utilizat, cont_iban)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, card.getNumarCard());
            ps.setString(3, card.getCvv());
            ps.setDate(4, Date.valueOf(card.getDataExpirare()));
            ps.setString(5, card.getTitularCard());
            ps.setBoolean(6, card.isActiv());
            ps.setString(10, contIban);

            if (card instanceof CardDebit cd) {
                ps.setString(2, "DEBIT");
                ps.setDouble(7, cd.getLimitaZilnica());
                ps.setNull(8, Types.DECIMAL);
                ps.setNull(9, Types.DECIMAL);
            } else if (card instanceof CardCredit cc) {
                ps.setString(2, "CREDIT");
                ps.setNull(7, Types.DECIMAL);
                ps.setDouble(8, cc.getLimitaCredit());
                ps.setDouble(9, cc.getSoldUtilizat());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("save(Card) failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Card> findById(String numarCard) {
        String sql = "SELECT * FROM carduri WHERE numar_card = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, numarCard);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("findById(Card) failed: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Card> findAll() {
        String sql = "SELECT * FROM carduri";
        List<Card> list = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("findAll(Card) failed: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void update(Card card) {
        String sql = "UPDATE carduri SET activ = ?, limita_zilnica = ?, sold_utilizat = ? WHERE numar_card = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setBoolean(1, card.isActiv());
            if (card instanceof CardDebit cd) ps.setDouble(2, cd.getLimitaZilnica());
            else ps.setNull(2, Types.DECIMAL);
            if (card instanceof CardCredit cc) ps.setDouble(3, cc.getSoldUtilizat());
            else ps.setNull(3, Types.DECIMAL);
            ps.setString(4, card.getNumarCard());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("update(Card) failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String numarCard) {
        String sql = "DELETE FROM carduri WHERE numar_card = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, numarCard);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("delete(Card) failed: " + e.getMessage(), e);
        }
    }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private Card map(ResultSet rs) throws SQLException {
        String tip     = rs.getString("tip");
        String numar   = rs.getString("numar_card");
        String cvv     = rs.getString("cvv");
        LocalDate exp  = rs.getDate("data_expirare").toLocalDate();
        String titular = rs.getString("titular_card");

        if ("DEBIT".equals(tip)) {
            CardDebit cd = new CardDebit(numar, cvv, exp, titular, rs.getDouble("limita_zilnica"));
            cd.setActiv(rs.getBoolean("activ"));
            return cd;
        } else {
            CardCredit cc = new CardCredit(numar, cvv, exp, titular, rs.getDouble("limita_credit"));
            cc.setSoldUtilizat(rs.getDouble("sold_utilizat"));
            cc.setActiv(rs.getBoolean("activ"));
            return cc;
        }
    }
}
