package com.target.targetreadyresultsservice.ServiceTest;

import com.target.targetreadyresultsservice.Dto.ClassDto;
import com.target.targetreadyresultsservice.model.ClassLevel;
import com.target.targetreadyresultsservice.model.Subject;
import com.target.targetreadyresultsservice.repository.ClassRepository;
import com.target.targetreadyresultsservice.service.ClassService;
import com.target.targetreadyresultsservice.service.SubjectService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ClassServiceTest {

    private ClassRepository classRepository = Mockito.mock(ClassRepository.class);
    private SubjectService subjectService =Mockito.mock(SubjectService.class);
    private ClassService classService=new ClassService(classRepository, subjectService);
    private ClassLevel classLevel;


    @Test
    void setClassLevelInfoShouldReturnClassDto(){
        ClassLevel classLevel = new ClassLevel("C4","4");
        ClassLevel classLevel1 = new ClassLevel();
        classLevel1.setName("4");
        when(classRepository.save(any(ClassLevel.class))).thenReturn(classLevel);
        ClassLevel actualClass = classService.setClassLevelInfo(classLevel1);
        assertEquals(classLevel,actualClass);
    }

    @Test
    void setClassLevelInfoShouldReturnException(){
        ClassLevel classLevel = new ClassLevel("","");
        when(classRepository.save(any(ClassLevel.class))).thenReturn(classLevel);
        assertThrows(RuntimeException.class,()->classService.setClassLevelInfo(classLevel));
    }

    @Test
    void getAllClassesShouldReturnClassDto(){
        List<ClassLevel> classLevels = List.of(new ClassLevel("C4","4"));
        List<String> subjects = List.of("Physics","Social");

        when(classRepository.findAll()).thenReturn(classLevels);
        when(subjectService.getSubjectsGivenClassCode(any(String.class))).thenReturn(subjects);
        List<ClassDto> responseClasses = classService.getAllClasses();
        assertThat(responseClasses).isNotNull();
        assertThat(1).isEqualTo(responseClasses.size());
    }

    @Test
    void getAllClassesShouldReturnNull(){
        when(classRepository.findAll()).thenReturn(new ArrayList<>());
        when(subjectService.getSubjectsGivenClassCode(any(String.class))).thenReturn(List.of("Physics","Social"));
        List<ClassDto> responseClasses = classService.getAllClasses();
        assertEquals(null, responseClasses);
    }


    @Test
    void getClassByIdShouldReturnClassDto(){
        ClassLevel classLevels = new ClassLevel("C4","4");
        ClassDto expectedClass = new ClassDto("C4","4",List.of("Physics","social"));

        when(classRepository.findById(any(String.class))).thenReturn(Optional.of(classLevels));
        when(subjectService.getSubjectsGivenClassCode(any(String.class))).thenReturn(List.of("Physics","Social"));
        ClassDto responseClass = classService.getClassLevelById("C4");
        assertThat(responseClass).isNotNull();
        assertTrue((expectedClass.getCode()).equals(responseClass.getCode()));
        assertTrue((expectedClass.getName()).equals(responseClass.getName()));
        assertTrue(responseClass.getSubjects().containsAll(List.of("Physics","Social")));
    }

    @Test
    void getClassByIdShouldReturnException(){
        when(classRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(classLevel));
        when(subjectService.getSubjectsGivenClassCode(any(String.class))).thenReturn(List.of("Physics","Social"));
        assertThrows(RuntimeException.class, ()-> classService.getClassLevelById("C4"));
    }

    @Test
    void deleteClassByIdShouldReturnString(){
        when(classRepository.existsById(any(String.class))).thenReturn(true);
        classService.deleteClassLevelInfo("C4");
        verify(classRepository,times(1)).deleteById("C4");
    }

    @Test
    void deleteClassByIdShouldReturnNotFound(){
        assertThrows(RuntimeException.class,()->classService.deleteClassLevelInfo("C4"));
    }

    @Test
    void getClassByClassNameShouldReturnClassDto(){
        ClassLevel classLevels = new ClassLevel("C4","4");
        ClassDto expectedClass = new ClassDto("C4","4",List.of("Physics","social"));

        when(classRepository.findByName(any(String.class))).thenReturn(Optional.of(classLevels));
        when(subjectService.getSubjectsGivenClassCode(any(String.class))).thenReturn(List.of("Physics","Social"));
        ClassDto responseClass = classService.getClassLeveByName("4");
        assertThat(responseClass).isNotNull();
        assertTrue((expectedClass.getCode()).equals(responseClass.getCode()));
        assertTrue((expectedClass.getName()).equals(responseClass.getName()));
        assertTrue(responseClass.getSubjects().containsAll(List.of("Physics","Social")));
    }

    @Test
    void getClassByClassNameShouldReturnException(){
        when(classRepository.findByName(any(String.class))).thenReturn(Optional.ofNullable(classLevel));
        when(subjectService.getSubjectsGivenClassCode(any(String.class))).thenReturn(List.of("Physics","Social"));
        assertThrows(RuntimeException.class, ()-> classService.getClassLeveByName("4"));
    }
}
