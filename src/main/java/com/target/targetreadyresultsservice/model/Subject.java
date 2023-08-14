package com.target.targetreadyresultsservice.model;


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
    private Integer credits;


    @NotNull private String classCode;
    @NotNull private Integer maxTestMarks;
    @NotNull private Integer maxExamMarks;


    public Subject(String subjectCode, @NotNull String subjectName, @NotNull Integer credits, @NotNull String classCode,@NotNull Integer maxExamMarks,@NotNull Integer maxTestMarks) {

        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.credits = credits;
        this.classCode = classCode;
        this.maxExamMarks=maxExamMarks;
        this.maxTestMarks=maxTestMarks;
    }

    public Subject() {}

    @Override
    public String toString() {
        return "Subject{" +
                "subjectCode='" + subjectCode + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", credits=" + credits +
                ", classCode='" + classCode + '\'' +
                ", maxTestMarks=" + maxTestMarks +
                ", maxExamMarks=" + maxExamMarks +
                '}';
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public Integer getCredits() {
        return credits;
    }

    public String getClassCode() {
        return classCode;
    }

    public Integer getMaxTestMarks() {
        return maxTestMarks;
    }

    public void setMaxTestMarks(Integer maxTestMarks) {
        this.maxTestMarks = maxTestMarks;
    }

    public Integer getMaxExamMarks() {
        return maxExamMarks;
    }

    public void setMaxExamMarks(Integer maxExamMarks) {
        this.maxExamMarks = maxExamMarks;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

}
