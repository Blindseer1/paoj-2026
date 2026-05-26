package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Main {

  public static void main(String[] args) {

    List<Transaction> data = List.of(
        new Transaction(1, new BigDecimal("150.50"),
            LocalDate.of(2025, 5, 1), "Romania", "Online"),

        new Transaction(2, new BigDecimal("300.00"),
            LocalDate.of(2025, 5, 2), "Germany", "POS"),

        new Transaction(3, new BigDecimal("300.00"),
            LocalDate.of(2025, 5, 3), "Romania", "Mobile"),

        new Transaction(4, new BigDecimal("99.99"),
            LocalDate.of(2025, 5, 4), "France", "Online"),

        new Transaction(5, new BigDecimal("700.00"),
            LocalDate.of(2025, 5, 5), "Germany", "POS"),

        new Transaction(6, new BigDecimal("250.00"),
            LocalDate.of(2025, 5, 6), "Romania", "Mobile"));

    Snapshot snapshot = data.stream()
        .collect(CustomCollectors.toSnapshot(3));

    System.out.println("=== TOP TRANSACTIONS ===");

    snapshot.getTopTransactions()
        .forEach(System.out::println);

    System.out.println("\n=== COUNT BY COUNTRY ===");

    snapshot.getCountByCountry()
        .entrySet()
        .stream()
        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
        .forEach(System.out::println);

    System.out.println("\n=== COUNT BY CHANNEL ===");

    snapshot.getCountByChannel()
        .entrySet()
        .stream()
        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
        .forEach(System.out::println);

    System.out.println("\n=== TOTAL AMOUNT ===");
    System.out.println(snapshot.getTotalAmount());
  }
}
