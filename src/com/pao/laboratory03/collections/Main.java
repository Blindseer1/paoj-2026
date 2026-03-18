package com.pao.laboratory03.collections;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.List;

import java.util.ArrayList;
import com.sun.source.tree.Tree;
import java.util.Arrays;

/**
 * Exercițiul 1 — Colecții: HashMap și TreeMap
 *
 * Creează în acest main:
 *
 * PARTEA A — HashMap (frecvența cuvintelor)
 * 1. Declară un array de String-uri:
 *    String[] words = {"java", "python", "java", "c++", "python", "java", "rust", "c++", "go"};
 * 2. Creează un HashMap<String, Integer> care contorizează de câte ori apare fiecare cuvânt.
 *    - Parcurge array-ul și folosește put() + getOrDefault() pentru a incrementa contorul.
 * 3. Afișează map-ul.
 * 4. Verifică dacă există cheia "rust" cu containsKey().
 * 5. Afișează DOAR cheile (keySet()), apoi DOAR valorile (values()).
 * 6. Parcurge map-ul cu entrySet() și afișează "cheia -> valoarea" pentru fiecare intrare.
 *
 * PARTEA B — TreeMap (sortare automată)
 * 7. Creează un TreeMap<String, Integer> din același HashMap (constructor cu argument).
 * 8. Afișează TreeMap-ul — observă ordinea alfabetică a cheilor.
 * 9. Folosește firstKey() și lastKey() pentru a afișa prima și ultima cheie.
 *
 * PARTEA C — Map cu obiecte
 * 10. Creează un HashMap<String, List<String>> care asociază materii cu liste de studenți.
 *     Exemplu: "PAOJ" -> ["Ana", "Mihai", "Ion"], "BD" -> ["Ana", "Elena"]
 * 11. Afișează toți studenții de la materia "PAOJ".
 * 12. Adaugă un student nou la "BD" și afișează lista actualizată.
 *
 * Output așteptat (orientativ — ordinea HashMap poate varia):
 *
 * === PARTEA A: HashMap — frecvența cuvintelor ===
 * Frecvență: {python=2, c++=2, java=3, rust=1, go=1}
 * Conține 'rust'? true
 * Chei: [python, c++, java, rust, go]
 * Valori: [2, 2, 3, 1, 1]
 * python -> 2
 * c++ -> 2
 * java -> 3
 * rust -> 1
 * go -> 1
 *
 * === PARTEA B: TreeMap — sortare automată ===
 * Sortat: {c++=2, go=1, java=3, python=2, rust=1}
 * Prima cheie: c++
 * Ultima cheie: rust
 *
 * === PARTEA C: Map cu obiecte ===
 * Studenți la PAOJ: [Ana, Mihai, Ion]
 * Studenți la BD (actualizat): [Ana, Elena, George]
 */
public class Main {
    public static void main(String[] args) {
        // TODO: implementează cele 3 părți de mai sus
        String[] words = { "java", "rust", "c++", "go"};
    HashMap<String,Integer> mp=new HashMap<>();
    int cont=0;
    for (String lang:words){
      mp.put(lang,cont);
      cont++;
    }
    for(HashMap.Entry<String,Integer> entry: mp.entrySet()){
        System.out.println(entry.getKey() +"->" + entry.getValue());
    }


// 4. Verifică dacă există cheia "rust" cu containsKey().
System.out.println(mp.containsKey("rust"));
// 5. Afișează DOAR cheile (keySet()), apoi DOAR valorile (values()).
  for(String key: mp.keySet()){
      System.out.println(key);
    }
  for(int value: mp.values()){
      System.out.println(value);
    }

// 6. Parcurge map-ul cu entrySet() și afișează "cheia -> valoarea" pentru fiecare intrare.
//
// PARTEA B — TreeMap (sortare automată)
// 7. Creează un TreeMap<String, Integer> din același HashMap (constructor cu argument).
TreeMap<String,Integer> copacel= new TreeMap<>(mp);
// 8. Afișează TreeMap-ul — observă ordinea alfabetică a cheilor.

for(HashMap.Entry<String,Integer> entry: copacel.entrySet()){
    System.out.println(entry.getKey() +"->" + entry.getValue());
}
// 9. Folosește firstKey() și lastKey() pentru a afișa prima și ultima cheie.

System.out.println(copacel.firstKey());
System.out.println(copacel.lastKey());


// PARTEA C — Map cu obiecte
// 10. Creează un HashMap<String, List<String>> care asociază materii cu liste de studenți.
//     Exemplu: "PAOJ" -> ["Ana", "Mihai", "Ion"], "BD" -> ["Ana", "Elena"]
HashMap<String,List<String>> ms= new HashMap<>();
ms.put("PAOJ",new ArrayList<>(Arrays.asList("Ana", "Mihai","Ion")));
ms.put("BD",new ArrayList<>(Arrays.asList("Ana","Elena")));

// 11. Afișează toți studenții de la materia "PAOJ".
for(String student: ms.get("PAOJ")){
      System.out.println(student);
    }
// 12. Adaugă un student nou la "BD" și afișează lista actualizată.
ms.get("BD").add("Constantin");
for(String student: ms.get("BD")){
      System.out.println(student);
    }
// Output așteptat (orientativ — ordinea HashMap poate varia):
//
// === PARTEA A: HashMap — frecvența cuvintelor ===
// Frecvență: {python=2, c++=2, java=3, rust=1, go=1}
// Conține 'rust'? true
// Chei: [python, c++, java, rust, go]
// Valori: [2, 2, 3, 1, 1]
// python -> 2
// c++ -> 2
// java -> 3
// rust -> 1
// go -> 1
//
// === PARTEA B: TreeMap — sortare automată ===
// Sortat: {c++=2, go=1, java=3, python=2, rust=1}
// Prima cheie: c++
// Ultima cheie: rust
//
// === PARTEA C: Map cu obiecte ===
// Studenți la PAOJ: [Ana, Mihai, Ion]
// Studenți la BD (actualizat): [Ana, Elena, George]
///
  }
}

