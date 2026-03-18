package com.pao.laboratory03.exercise;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.pao.laboratory03.exercise.Subject;

//6. service/StudentService.java — SERVICIU (Singleton)
//   - Câmp: List<Student> students (ArrayList)
//   - Singleton pattern (constructor privat, getInstance())
//   - Metode:
//     a) void addStudent(String name, int age)
//        → creează Student și adaugă în listă
//        → dacă există deja un student cu același nume, aruncă RuntimeException
//     b) Student findByName(String name)
//        → caută în listă, aruncă StudentNotFoundException dacă nu găsește
//     c) void addGrade(String studentName, Subject subject, double grade)
//        → găsește studentul (findByName) și adaugă nota
//     d) void printAllStudents()
//        → afișează toți studenții cu notele lor
//     e) void printTopStudents()
//        → sortează studenții descrescător după medie și afișează
//     f) Map<Subject, Double> getAveragePerSubject()
//        → calculează media pe fiecare materie (din toți studenții care au notă)
//
//═══════════════════════════════════════════════════════════════
//

class Serviciu{
  List<Student> students;
    private static Serviciu instance;  
    
    private Serviciu() {  
    }

    private void printAllStudents(){
    for(Student st:students){
      System.out.println(st.getName());
      Map<Subject,Double> grades=st.getGrades();
      for(Map.Entry<Subject,Double> entry: grades.entrySet()){
        System.out.println(entry.getKey().getFullName() + " - "+entry.getValue());
      }

      }
    }
  private Map<Subject,Double> getAveragePerSubject(){
    Map<Subject,Double> ans;
     

    for(Student st:students){
      Map<Subject,Double> grades=st.getGrades();
      for(Map.Entry<Subject,Double> entry: grades.entrySet()){
        ans.put(entry.getKey(),grades.getOrDefault(entry.getKey(), 0.0) + entry.getValue());
      }
  }

for(Map.Entry<Subject,Double> entry: ans.entrySet()){
      ans.put(entry.getKey(),ans.get(entry.getKey())/students.size());
    }
  return ans;
  }
  private void printTopStudents(){

    List<Student> tmp=students;
    tmp.sort(Comparator.comparing(Student::getAverage).reversed());

    for(Student st:tmp){
      System.out.println(st.getName());
      Map<Subject,Double> grades=st.getGrades();
      for(Map.Entry<Subject,Double> entry: grades.entrySet()){
        System.out.println(entry.getKey().getFullName() + " - "+entry.getValue());
      }

      }
  }
  
    private void addGrade(String studentName,Subject subject,double grade){
      try{
      findByName(studentName);
        for(Student st: students){
        if(st.getName()==studentName)
          st.addGrade(subject, grade);
      }
      } catch(StudentNotFoundException e){
      System.out.println("Nu se poate adauga nota la un student inexistent");
    }
  }
    private void findByName(String name){
    for( Student st: students){
      if (st.getName() == name) {
        System.out.println("Exista un astfel de nume(findByName"); 
        return;
      }
    }
    throw new StudentNotFoundException("Nu exista un astfel de student");
  }

    private void addStudent(String name,int age){
    Student a= new Student(name, age);
    if(students.contains(a))
      throw new RuntimeException("Exista deja un student cu numele asta");
    else
      students.add(a);
  }
    
    public static Serviciu getInstance() {  
        if (instance == null) {
            instance = new Serviciu();
        }
        return instance;
    }

}


