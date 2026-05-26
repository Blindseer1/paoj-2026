package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Snapshot {

  private final Map<String, Long> countByCountry;
  private final Map<String, Long> countByChannel;
  private final BigDecimal totalAmount;
  private final List<Transaction> topTransactions;

  public Snapshot(
      Map<String, Long> countByCountry,
      Map<String, Long> countByChannel,
      BigDecimal totalAmount,
      List<Transaction> topTransactions) {
    this.countByCountry = Collections.unmodifiableMap(new HashMap<>(countByCountry));
    this.countByChannel = Collections.unmodifiableMap(new HashMap<>(countByChannel));
    this.totalAmount = totalAmount;
    this.topTransactions = List.copyOf(topTransactions);
  }

  public Map<String, Long> getCountByCountry() {
    return countByCountry;
  }

  public Map<String, Long> getCountByChannel() {
    return countByChannel;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public List<Transaction> getTopTransactions() {
    return topTransactions;
  }
}
