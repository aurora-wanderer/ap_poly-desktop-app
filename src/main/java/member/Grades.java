package main.java.member;

import java.util.ArrayList;
import java.util.List;

public class Grades {

    private Student student;
    private Double ENScore;
    private Double ITScore;
    private Double PEScore;
    private Double AVGScore;

    public Grades() {
    }

    public Double getENScore() {
        return ENScore;
    }

    public Grades(Student student, Double ENScore, Double ITScore, Double PEScore, Double AVGScore) {
        this.student = student;
        this.ENScore = ENScore;
        this.ITScore = ITScore;
        this.PEScore = PEScore;
        this.AVGScore = AVGScore;
    }

    public void setENScore(Double ENScore) {
        this.ENScore = ENScore;
    }

    public Double getITScore() {
        return ITScore;
    }

    public void setITScore(Double ITScore) {
        this.ITScore = ITScore;
    }

    public Double getPEScore() {
        return PEScore;
    }

    public void setPEScore(Double PEScore) {
        this.PEScore = PEScore;
    }

    public Double getAVGScore() {
        return AVGScore = Double
                .valueOf(String.format("%.2f", (ENScore + ITScore + PEScore) / 3));
    }

    public void setAVGScore(Double AVGScore) {
        this.AVGScore = AVGScore;
    }

    public Student getStudent() {
        return student;
    }

    public String getStudentID() {
        return student.getID();
    }
    
    public String getStudentName() {
        return student.getName();
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Object[] toArray() {
        return new Object[]{
            student.getID(),
            student.getName(),
            ENScore,
            ITScore,
            PEScore,
            getAVGScore()
        };
    }
    
    public List<String> toList() {
        ArrayList<String> temp = new ArrayList<>();

        temp.add(student.getID());
        temp.add(ENScore.toString());
        temp.add(ITScore.toString());
        temp.add(PEScore.toString());
        temp.add(getAVGScore().toString());        
        return temp;
    }
}
