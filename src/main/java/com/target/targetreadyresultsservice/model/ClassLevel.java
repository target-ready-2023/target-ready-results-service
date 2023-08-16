package com.target.targetreadyresultsservice.model;


import com.mongodb.lang.NonNull;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;



@Document(collection = "Class")
public class ClassLevel {

    @Id
    private String code;

    @NotNull
    private String name;
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClassLevel(String code, @NotNull String name) {
        this.code = code;
        this.name = name;
    }

    public ClassLevel() {
    }

    @Override
    public String toString() {
        return "ClassLevel{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}