package com.target.targetreadyresultsservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Student")
public class Student {

    @Id
    private String studentId;

    private String name;

    private String classCode;

    private String rollNumber;

    public Student(String studentId, String name, String classCode, String rollNumber) {
        this.studentId = studentId;
        this.name = name;
        this.classCode = classCode;
        this.rollNumber = rollNumber;
    }


    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", classCode='" + classCode + '\'' +
                ", rollNumber='" + rollNumber + '\'' +
                '}';
    }

    public String getRollNumber() {
        return studentId;
    }

    public void setRollNumber(String rollNumber) {
        this.studentId= rollNumber;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
