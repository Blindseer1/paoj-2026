package com.pao.laboratory03.exercise;

import java.util.HashMap;
import java.util.Map;


//   - Câmpuri private: String name, int age, Map<Subject, Double> grades
//   - Constructor: Student(String name, int age)
//     → inițializează grades ca HashMap gol
//     → validare: dacă age < 18 sau age > 60, aruncă InvalidStudentException
//   - Metode: getName(), getAge(), getGrades()
//   - addGrade(Subject subject, double grade)
//     → dacă grade < 1 sau grade > 10, aruncă InvalidGradeException
//     → pune nota în map (suprascrie dacă materia există deja)
//   - double getAverage()
//     → calculează media aritmetică a notelor (returnează 0 dacă nu are note)
//   - toString() → "Student{name='Ana', age=20, avg=8.50}"
//


class  Student {

private String name;
int age;
Map<Subject, Double>grades;
  
Student( String name,int age){
      this.name=name;
      this.age=age;
    grades = new HashMap<>();
    if(age<18 || age > 60){
      throw new InvalidStudentException("Nu are varsta unui student");
    }
  }

  public void addGrade(Subject subiect, double grade){
    if(grade<1 || grade > 10)
    throw new InvalidGradeException("Nota invalida");
  
  else { 
      grades.put(subiect,grade);
  }
}
  public double getAverage(){
    int ans=0;
    for(double grade:grades.values())
      ans+=grade;
    return ans/grades.size();
  }

  @Override
  public String toString() {
    return "{Student "+ getName() + ", age= " + getAge() + ", avg=" + getAverage()+ " }";
  }

  public int getAge(){return age;}
  public String getName(){return name;}
  public Map<Subject, Double> getGrades(){return grades;}

}
