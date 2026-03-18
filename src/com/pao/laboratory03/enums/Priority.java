package com.pao.laboratory03.enums;


// PASUL 1 — Creează enum-ul Priority.java (fișier separat în același pachet):
//   - Constante: LOW, MEDIUM, HIGH, CRITICAL
//   - Câmpuri private: int level, String color
//   - Constructor privat: Priority(int level, String color)
//   - Getteri: getLevel(), getColor()
//   - Metodă abstractă: String getEmoji() — fiecare constantă o implementează diferit
//     LOW → "🟢", MEDIUM → "🟡", HIGH → "🟠", CRITICAL → "🔴"
//   - Valorile sugerate:
//     LOW(1, "green"), MEDIUM(2, "yellow"), HIGH(3, "orange"), CRITICAL(4, "red")
public enum Priority{
 
  LOW(1, "green"){ 
    @Override
    public String getEmoji() {
      return "🟢";
    }
  }, MEDIUM(2, "yellow"){ 
    @Override
    public String getEmoji() {
      return "🟡";
    }
  }, HIGH(3, "orange"){ 
    @Override
    public String getEmoji() {
      return "🟠";
    }
  }, CRITICAL(4, "red"){ 
    @Override
    public String getEmoji() {
      return "🔴";
    }
  };

  private int level;
  private String color;
  Priority(int level, String color){
    this.level= level;
    this.color=color;
  }
  public String getColor(){
    return color;
  }
  public int getLevel(){
    return level;
  }
  public abstract String getEmoji();


}
// PASUL 2 — În acest Main.java:
//   a) Parcurge toate valorile cu Priority.values() și afișează:
//      "emoji name (level=X, color=Y)"
//   b) Folosește switch pe un Priority și afișează un mesaj specific.
//   c) Convertește un String în Priority cu Priority.valueOf("HIGH") — afișează rezultatul.
//   d) Demonstrează compararea: folosește == între două enum-uri (NU .equals()).
//   e) Afișează name() și ordinal() pentru fiecare constantă.
//
// Output așteptat:
//
// === Toate prioritățile ===
// 🟢 LOW (level=1, color=green)
// 🟡 MEDIUM (level=2, color=yellow)
// 🟠 HIGH (level=3, color=orange)
// 🔴 CRITICAL (level=4, color=red)
//
// === Switch pe prioritate ===
// ⚠️ Atenție! Prioritate ridicată!
//
// === valueOf ===
// Priority.valueOf("HIGH") = HIGH
//
// === Comparare enum ===
// HIGH == HIGH? true
// HIGH == LOW? false
//
// === name() și ordinal() ===
// LOW: name=LOW, ordinal=0
// MEDIUM: name=MEDIUM, ordinal=1
// HIGH: name=HIGH, ordinal=2
// CRITICAL: name=CRITICAL, ordinal=3
///
//
