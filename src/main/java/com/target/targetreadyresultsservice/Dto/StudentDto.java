package com.target.targetreadyresultsservice.Dto;

public class StudentDto implements Comparable<StudentDto> {
    String studentid;
    String classCode;
    String rollNum;
    String name;
    Double totalMarks;

    public StudentDto(String studentid, String classCode, String rollNum, String name, Double totalMarks) {
        this.studentid = studentid;
        this.classCode = classCode;
        this.rollNum = rollNum;
        this.name = name;
        this.totalMarks = totalMarks;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getRollNum() {
        return rollNum;
    }

    public void setRollNum(String rollNum) {
        this.rollNum = rollNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(Double totalMarks) {
        this.totalMarks = totalMarks;
    }

    @Override
    public int compareTo(StudentDto student) {
        return Double.compare(getTotalMarks(),student.getTotalMarks());
      }

}


