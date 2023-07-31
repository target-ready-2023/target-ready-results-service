package com.target.targetreadyresultsservice.service;


import com.target.targetreadyresultsservice.model.Student;
import com.target.targetreadyresultsservice.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentInfo(String studentId) {
        return studentRepository.findById(studentId);
    }

    public void setStudentInfo(Student student) {
        try {
            studentRepository.save(student);
        } catch (Exception e) {

        }
    }

    public List<Student> getStudentDetailsByClassCode(String classCode) {
        List<Student> students=studentRepository.findByClassCode(classCode);
        if(students.isEmpty())
        {
            throw new RuntimeException("No student present in the class with the class code: "+classCode);
        }
        return students;
    }

    public List<Student> getStudentByName(String studentName){
        return studentRepository.findByName(studentName);
    }
}
