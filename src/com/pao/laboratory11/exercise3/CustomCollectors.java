package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

public class CustomCollectors {

  public static Collector<Transaction, ?, Snapshot> toSnapshot(int topN) {

    class Agg {
      Map<String, Long> countByCountry = new HashMap<>();
      Map<String, Long> countByChannel = new HashMap<>();
      BigDecimal totalAmount = BigDecimal.ZERO;
      List<Transaction> transactions = new ArrayList<>();
    }

    return Collector.of(
        Agg::new,

        (agg, tx) -> {
          agg.countByCountry.merge(tx.getCountry(), 1L, Long::sum);
          agg.countByChannel.merge(tx.getChannel(), 1L, Long::sum);

          agg.totalAmount = agg.totalAmount.add(tx.getAmount());

          agg.transactions.add(tx);
        },

        (a, b) -> {
          b.countByCountry.forEach(
              (k, v) -> a.countByCountry.merge(k, v, Long::sum));

          b.countByChannel.forEach(
              (k, v) -> a.countByChannel.merge(k, v, Long::sum));

          a.totalAmount = a.totalAmount.add(b.totalAmount);

          a.transactions.addAll(b.transactions);

          return a;
        },

        agg -> {
          List<Transaction> top = agg.transactions.stream()
              .sorted(
                  Comparator.comparing(Transaction::getAmount)
                      .reversed()
                      .thenComparing(Transaction::getId))
              .limit(topN)
              .toList();

          return new Snapshot(
              agg.countByCountry,
              agg.countByChannel,
              agg.totalAmount,
              top);
        });
  }
}
