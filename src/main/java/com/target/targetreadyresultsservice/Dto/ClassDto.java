package com.target.targetreadyresultsservice.Dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@ToString
public class ClassDto {
    private String code;
    private String name;
    private List<String> subjects;

    public ClassDto() {
    }
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

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public ClassDto(String code, String name, List<String> subjects) {
        this.code = code;
        this.name = name;
        this.subjects = subjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassDto classDto = (ClassDto) o;
        return Objects.equals(code, classDto.code) && Objects.equals(name, classDto.name) && Objects.equals(subjects, classDto.subjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, subjects);
    }
}
