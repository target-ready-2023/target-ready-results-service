package com.target.targetreadyresultsservice.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Class")
public class ClassLevel {

    @Id
    private String code;
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

    @Override
    public String toString() {
        return "ClassLevel{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}