package com.target.targetreadyresultsservice.model;


//import com.mongodb.lang.NonNull;
import com.mongodb.lang.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Arrays;
import java.util.List;


@Document(collection = "Subject")
public class Subject {
    @Id
    private String subjectCode;

    @NotNull
    private String subjectName;

    @NotNull
    private String credits;


    @NotNull private String classCode;

    public Subject(String subjectCode, @NotNull String subjectName, @NotNull String credits, @NotNull String classCode) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.credits = credits;
        this.classCode = classCode;
    }
    public Subject()
    {

    }


    @Override
    public String toString() {
        return "Subject{" +
                "subjectCode='" + subjectCode + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", credits='" + credits + '\'' +
                ", classLevel=" + classCode +
                '}';
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getCredits() {
        return credits;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }
}
