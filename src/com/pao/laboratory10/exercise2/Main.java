package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;
import java.util.*;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int n = Integer.parseInt(sc.nextLine().trim());

    ArrayList<Tranzactie> lista = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      String[] p = sc.nextLine().trim().split("\\s+");
      lista.add(new Tranzactie(
          Integer.parseInt(p[0]),
          Double.parseDouble(p[1]),
          p[2],
          TipTranzactie.valueOf(p[3])));
    }

    Comparator<Tranzactie> bySuma = Comparator.comparingDouble(Tranzactie::getSuma);

    while (sc.hasNextLine()) {
      String line = sc.nextLine().trim();
      if (line.isEmpty())
        continue;
      String[] parts = line.split("\\s+");

      switch (parts[0]) {
        case "UNIQUE_IDS": {
          LinkedHashSet<Integer> ids = new LinkedHashSet<>();
          for (Tranzactie t : lista)
            ids.add(t.getId());
          System.out.println("IDs unice (" + ids.size() + "): " + ids);
          break;
        }
        case "MONTHLY_REPORT": {
          TreeMap<String, double[]> report = new TreeMap<>();
          for (Tranzactie t : lista) {
            String luna = t.getData().substring(0, 7);
            report.putIfAbsent(luna, new double[] { 0.0, 0.0 });
            if (t.getTip() == TipTranzactie.CREDIT)
              report.get(luna)[0] += t.getSuma();
            else
              report.get(luna)[1] += t.getSuma();
          }
          for (Map.Entry<String, double[]> e : report.entrySet()) {
            System.out.printf("%s: CREDIT %.2f RON, DEBIT %.2f RON%n",
                e.getKey(), e.getValue()[0], e.getValue()[1]);
          }
          break;
        }
        case "TOP": {
          int k = Integer.parseInt(parts[1]);
          ArrayList<Tranzactie> copie = new ArrayList<>(lista);
          copie.sort(bySuma.reversed());
          System.out.println("Top " + k + ":");
          for (int i = 0; i < k; i++)
            System.out.println(copie.get(i));
          break;
        }
        case "SORT_ASC": {
          Collections.sort(lista, bySuma);
          for (Tranzactie t : lista)
            System.out.println(t);
          break;
        }
        case "SORT_DESC": {
          Collections.sort(lista, bySuma.reversed());
          for (Tranzactie t : lista)
            System.out.println(t);
          break;
        }
        case "REVERSE": {
          Collections.reverse(lista);
          for (Tranzactie t : lista)
            System.out.println(t);
          break;
        }
        case "MIN_MAX": {
          System.out.println("MIN: " + Collections.min(lista, bySuma));
          System.out.println("MAX: " + Collections.max(lista, bySuma));
          break;
        }
        case "CME_DEMO": {
          try {
            for (Tranzactie t : lista)
              lista.remove(t);
          } catch (ConcurrentModificationException e) {
            System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
          }
          break;
        }
      }
    }
  }
}
