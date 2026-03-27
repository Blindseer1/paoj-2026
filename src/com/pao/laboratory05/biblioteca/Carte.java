package com.pao.laboratory05.biblioteca;


public class Carte implements Comparable<Carte>{
  
  String titlu;
  String autor;
  int an;
  double rating;
  Carte(String titlu, String autor, int an, double rating){
    this.titlu=titlu;
    this.autor=autor;
    this.an=an;
    this.rating=rating;
  }

  @Override 
  public int compareTo(Carte other){
     return Double.compare(this.rating,other.rating);
  }
  //getters 
  public int getAn(){
    return this.an;
  }
  public String getTitlu(){
    return this.titlu;
  }
  public double getRating(){
    return this.rating;
  }
  public String getAutor(){
    return this.autor;
  }

  @Override
  public String toString() {
    return "Carte{titlu= " + this.titlu + " ,autor= " + this.autor + ",an= "+ an + ",rating= " + this.rating+ "}";
  }


}
