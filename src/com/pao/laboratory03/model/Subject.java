package com.pao.laboratory03.exercise;

//    - Constante: PAOJ, BD, SO, RC (sau alte materii)
//    - Câmpuri: String fullName, int credits
//    - Constructor privat, getteri
//    - toString() → "PAOJ (Programare Avansată pe Obiecte, 6 credite)"

public enum Subject{
  PAOJ("Progamare Avansta pe Obiecte",6){},
  BD("Baze de date", 4){},
  SO("Sisteme de operare",6){},
  TW("Tehnici Web", 5){};
  private String fullName;
  private int credits;


  Subject(String fullName, int credits){ 
    this.fullName = fullName;
    this.credits = credits;
  }
  public String getFullName(){
    return fullName;
  }
  public int getCredits(){
    return credits;
  }

 @Override
  public String toString() {
  return name() + "(" + getFullName() + ", " + getCredits() + " credite)";
  }


}
