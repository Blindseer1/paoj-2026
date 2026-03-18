package com.pao.laboratory03.exceptions;


import java.util.ArrayList;
import java.util.List;

import com.pao.laboratory03.exceptions.DuplicateEntryException;
import com.pao.laboratory03.exceptions.InvalidAgeException;
/**
 * Exercițiul 3 — Excepții (checked, unchecked, custom)
 *
 * Creează în acest pachet (lângă Main.java) două clase de excepții custom,
 * apoi demonstrează-le aici.
 *
 * PASUL 1 — Creează InvalidAgeException.java (fișier separat):
 *   - Extinde RuntimeException (unchecked)
 *   - Constructor cu String message → apelează super(message)
 *
 * PASUL 2 — Creează DuplicateEntryException.java (fișier separat):
 *   - Extinde RuntimeException (unchecked)
 *   - Constructor cu String message → apelează super(message)
 *
 * PASUL 3 — În acest Main.java, implementează și demonstrează:
 *
 *   a) UNCHECKED EXCEPTIONS — NullPointerException, ArrayIndexOutOfBoundsException:
 *      - Creează o metodă riskyMethod() care aruncă NullPointerException
 *      - Prinde-o cu try-catch, afișează mesajul erorii
 *      - Adaugă un bloc finally care se execută mereu
 *
 *   b) CUSTOM EXCEPTIONS — InvalidAgeException, DuplicateEntryException:
 *      - Creează o metodă validateAge(int age) care aruncă InvalidAgeException
 *        dacă age < 0 sau age > 150
 *      - Creează o metodă addToList(List<String> list, String name) care aruncă
 *        DuplicateEntryException dacă name există deja în listă
 *      - Demonstrează ambele cu try-catch
 *
 *   c) MULTI-CATCH:
 *      - Prinde InvalidAgeException | DuplicateEntryException într-un singur catch
 *
 *   d) CATCH ORDERING:
 *      - Demonstrează că prinderea specifică (InvalidAgeException) trebuie
 *        să fie ÎNAINTE de cea generală (RuntimeException)
 *
 *   e) THROW vs THROWS:
 *      - Creează o metodă cu semnătura: void process(int age) throws InvalidAgeException
 *      - Apeleaz-o din main cu try-catch
 *
 * Output așteptat:
 *
 * === a) Unchecked — NullPointerException ===
 * Prins: Cannot invoke "String.length()" because "s" is null
 * Finally se execută mereu!
 *
 * === b) Custom exceptions ===
 * InvalidAgeException: Vârsta -5 nu este validă (0-150)
 * DuplicateEntryException: 'Ana' există deja în listă
 *
 * === c) Multi-catch ===
 * Excepție prinsă: Vârsta 200 nu este validă (0-150)
 *
 * === d) Catch ordering (specific → general) ===
 * InvalidAgeException prinsă specific: Vârsta -1 nu este validă (0-150)
 *
 * === e) Throw vs throws ===
 * Metoda process() a aruncat: Vârsta 999 nu este validă (0-150)
 */

public class Main {
    public void riskyMethod(){
        try{ 
          String t= null;
          System.out.println(t.toUpperCase());
        } catch(NullPointerException e){
         System.out.println(e.getMessage()); 
       }
    finally{
    System.out.println("mereu");
    }
    

  }

  public static void validateAge(int age){
    if(age<18 || age > 150)      
      throw new InvalidAgeException("Varsta invalida");
    else 
    System.out.println("Varsta valida");
  }
  public  static void addToList(List<String> list,String name){
    if(list.contains(name)){
      throw new DuplicateEntryException("Nu sunt permise duplicate");
    }
    else {
      System.out.println("Se poate adauga in lista");
    }
    

  }

public static  void process(int age) throws InvalidAgeException{
    if(age<18 || age >150)
    throw new InvalidAgeException("Nu e bun");
    else {
      System.out.println("Valid");
    }
  };

    public static void main(String[] args) {
        // TODO: implementează pașii de mai sus
        // Hint: creează mai întâi InvalidAgeException.java și DuplicateEntryException.java
 // PASUL 3 — În acest Main.java, implementează și demonstrează:
 //
 //   a) UNCHECKED EXCEPTIONS — NullPointerException, ArrayIndexOutOfBoundsException:
 //      - Creează o metodă riskyMethod() care aruncă NullPointerException
 //      - Prinde-o cu try-catch, afișează mesajul erorii
 //      - Adaugă un bloc finally care se execută mereu

 //   b) CUSTOM EXCEPTIONS — InvalidAgeException, DuplicateEntryException:
 //      - Creează o metodă validateAge(int age) care aruncă InvalidAgeException
 //        dacă age < 0 sau age > 150
 //      - Creează o metodă addToList(List<String> list, String name) care aruncă
 //        DuplicateEntryException dacă name există deja în listă
 //      - Demonstrează ambele cu try-catch
        try{ 
          validateAge(-23);
        }
        catch(InvalidAgeException e){ 
        System.out.println(e.getMessage());
        };


        try{
          List<String> list=new ArrayList<>();
          list.add("ana");
          list.add("ioana");
          String name="ana";
          addToList(list, name);
        } 
    catch(DuplicateEntryException e){
      System.out.println(e.getMessage());
    }
 //   c) MULTI-CATCH:
 //      - Prinde InvalidAgeException | DuplicateEntryException într-un singur catch

     try {
        
          List<String> list=new ArrayList<>();
          list.add("ana");
          list.add("ioana");
          String name="ana";
          addToList(list, name);
          validateAge(-23);
     } catch(DuplicateEntryException | InvalidAgeException e){
      System.out.println("Multi-catch: " + e.getMessage());
    }
 //   d) CATCH ORDERING:
 //      - Demonstrează că prinderea specifică (InvalidAgeException) trebuie
 //        să fie ÎNAINTE de cea generală (RuntimeException)

    try{
    validateAge(-12);
    }
    catch(InvalidAgeException e){
      System.out.println("specific:" + e.getMessage());
    }
    catch(RuntimeException e){
      System.out.println("general: "+ e.getMessage());
    }

 //   e) THROW vs THROWS:
 //      - Creează o metodă cu semnătura: void process(int age) throws InvalidAgeException
 //      - Apeleaz-o din main cu try-catch
    try{
    process(-23);
    }
    catch(InvalidAgeException e){
      System.out.println(e.getMessage());
    }
 // Output așteptat:
 //
 // === a) Unchecked — NullPointerException ===
 // Prins: Cannot invoke "String.length()" because "s" is null
 // Finally se execută mereu!
 //
 // === b) Custom exceptions ===
 // InvalidAgeException: Vârsta -5 nu este validă (0-150)
 // DuplicateEntryException: 'Ana' există deja în listă
 //
 // === c) Multi-catch ===
 // Excepție prinsă: Vârsta 200 nu este validă (0-150)
 //
 // === d) Catch ordering (specific → general) ===
 // InvalidAgeException prinsă specific: Vârsta -1 nu este validă (0-150)
 //
 // === e) Throw vs throws ===
 // Metoda process() a aruncat: Vârsta 999 nu este validă (0-150)
 //
    }
}

