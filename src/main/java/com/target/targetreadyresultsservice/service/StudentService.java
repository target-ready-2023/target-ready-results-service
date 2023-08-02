package com.target.targetreadyresultsservice.service;


import com.target.targetreadyresultsservice.Dto.ClassDto;
import com.target.targetreadyresultsservice.Exception.NotFoundException;
import com.target.targetreadyresultsservice.model.ClassLevel;
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
    @Autowired
    private final StudentRepository studentRepository;
    @Autowired
    private final ClassService classService;

    @Autowired
    public StudentService(StudentRepository studentRepository, ClassService classService) {
        this.studentRepository = studentRepository;
        this.classService = classService;
    }

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

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
        List<Student> students=studentRepository.findByclassCode(classCode);
        if(students.isEmpty())
        {
            throw new RuntimeException("No student present in the class with the class code: "+classCode);
        }
        return students;
    }

    public List<Student> getStudentByName(String studentName){
        return studentRepository.findByName(studentName);
    }

    //get a student from class name and their roll number
    //used in results-service
    public Student getStudentFromClassRollNo(String className, String rollNo) {

        log.info("ClassName received is - {}",className);
        String classCode = "";
        ClassDto classFound = null;
        List<ClassDto> classDtoList = classService.getAllClasses();
        if (classDtoList.isEmpty()) {
            log.info("Classes not found in repository. Throws NotFoundException");
            throw new NotFoundException(("No classes found!"));
        }
        for (ClassDto classDto : classDtoList) {
            log.info("One in the list of name - "+classDto.getName());
            log.info("The class name we need -"+className);
            if (classDto.getName().equals(className)) {
                //the problem is in this line
                classCode = classDto+classDto.getCode();
                classFound=classDto;
                break;
            }
        }
        if(classCode.isEmpty()){
            throw new NotFoundException("Class not found - this is from get student fromclass name and rooll num fun");
        }
        log.info("Class code found is - {}",classCode);
        log.info("classFound has the class Code - {}",classFound.getCode());
        List<Student> students = getStudentDetailsByClassCode(classFound.getCode());
        Student student=null;
        for (Student s: students
        ) {
            if(s.getRollNumber().equals(rollNo)){
                student = s;
            }
        }
        if(student==null){
            throw new NotFoundException("Student Not Found");
        }
        log.info("Student found is - {}",student);
        return student;
    }
}
