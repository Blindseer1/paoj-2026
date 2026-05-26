package com.pao.laboratory10.exercise1;

import java.util.*;

public class Main {
  public static void main(String[] args) {
    LinkedList<Tranzactie> coada = new LinkedList<>();
    Scanner sc = new Scanner(System.in);

    while (sc.hasNextLine()) {
      String line = sc.nextLine().trim();
      if (line.isEmpty())
        continue;
      String[] parts = line.split("\\s+");
      String cmd = parts[0];

      switch (cmd) {
        case "ENQUEUE": {
          int id = Integer.parseInt(parts[1]);
          double suma = Double.parseDouble(parts[2]);
          String data = parts[3];
          TipTranzactie tip = TipTranzactie.valueOf(parts[4]);
          coada.addLast(new Tranzactie(id, suma, data, tip));
          break;
        }
        case "PUSH": {
          int id = Integer.parseInt(parts[1]);
          double suma = Double.parseDouble(parts[2]);
          String data = parts[3];
          TipTranzactie tip = TipTranzactie.valueOf(parts[4]);
          coada.addFirst(new Tranzactie(id, suma, data, tip));
          break;
        }
        case "DEQUEUE": {
          if (coada.isEmpty())
            System.out.println("Coada goala.");
          else
            System.out.println("Procesat: " + coada.removeFirst());
          break;
        }
        case "POP": {
          if (coada.isEmpty())
            System.out.println("Coada goala.");
          else
            System.out.println("Extras: " + coada.removeFirst());
          break;
        }
        case "REMOVE_DEBIT": {
          int n = 0;
          Iterator<Tranzactie> itr = coada.iterator();
          while (itr.hasNext()) {
            if (itr.next().getTip() == TipTranzactie.DEBIT) {
              itr.remove();
              n++;
            }
          }
          System.out.println("Eliminat " + n + " tranzactii DEBIT.");
          break;
        }
        case "REMOVE_BELOW": {
          double threshold = Double.parseDouble(parts[1]);
          int n = 0;
          Iterator<Tranzactie> itr = coada.iterator();
          while (itr.hasNext()) {
            if (itr.next().getSuma() < threshold) {
              itr.remove();
              n++;
            }
          }
          System.out.printf("Eliminat %d tranzactii sub %.2f RON.%n", n, threshold);
          break;
        }
        case "PRINT": {
          for (Tranzactie t : coada)
            System.out.println(t);
          break;
        }
        case "SIZE": {
          System.out.println("Dimensiune coada: " + coada.size());
          break;
        }
      }
    }
  }
}
