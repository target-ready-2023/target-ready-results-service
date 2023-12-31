package com.target.targetreadyresultsservice.service;

import com.target.targetreadyresultsservice.Dto.ClassDto;
import com.target.targetreadyresultsservice.model.ClassLevel;
import com.target.targetreadyresultsservice.repository.ClassRepository;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;



import java.util.*;

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
    void setClassLevelInfoShouldReturnClassLevel(){
        ClassLevel classLevel = new ClassLevel("C4","4");
        ClassLevel classLevel1 = new ClassLevel();
        classLevel1.setName("4");
        when(classRepository.existsByName(anyString())).thenReturn(false);
        when(classRepository.save(any(ClassLevel.class))).thenReturn(classLevel);
        ClassLevel actualClass = classService.setClassLevelInfo(classLevel1);
        assertEquals(classLevel,actualClass);
    }

    @Test
    void setClassLevelInfoShouldReturnClassNameException(){
        ClassLevel classLevel = new ClassLevel("","");
        when(classRepository.existsByName(anyString())).thenReturn(false);
        when(classRepository.save(any(ClassLevel.class))).thenReturn(classLevel);
        assertThrows(RuntimeException.class,()->classService.setClassLevelInfo(classLevel));
    }

    @Test
    void setClassLevelInfoShouldReturnExistsException(){
        ClassLevel classLevel = new ClassLevel("C4","4");
        when(classRepository.existsByName(anyString())).thenReturn(true);
        when(classRepository.save(any(ClassLevel.class))).thenReturn(classLevel);
        assertThrows(RuntimeException.class,()->classService.setClassLevelInfo(classLevel));
    }

    @Test
    void getAllClassesShouldReturnClassDto(){
        List<ClassLevel> classLevels = List.of(new ClassLevel("C4","4"),
                new ClassLevel("C4","5"));
        List<String> subjects = List.of("Physics","Social");

        when(classRepository.findAll()).thenReturn(classLevels);
        when(subjectService.getSubjectsGivenClassCode(any(String.class))).thenReturn(subjects);
        List<ClassDto> responseClasses = classService.getAllClasses();
        assertThat(responseClasses).isNotNull();
        assertThat(2).isEqualTo(responseClasses.size());
    }

    @Test
    void getAllClassesIsNumeric1() {
        List<ClassLevel> classLevels = List.of(new ClassLevel("C4","4"),
                new ClassLevel("C4","4a"));
        List<String> subjects = List.of("Physics","Social");

        when(classRepository.findAll()).thenReturn(classLevels);
        when(subjectService.getSubjectsGivenClassCode(any(String.class))).thenReturn(subjects);
        List<ClassDto> responseClasses = classService.getAllClasses();
        assertThat(responseClasses).isNotNull();
        assertThat(2).isEqualTo(responseClasses.size());
    }

    @Test
    void getAllClassesIsNumeric2() {
        List<ClassLevel> classLevels = List.of(new ClassLevel("C4","4a"),
                new ClassLevel("C5","5"));
        List<String> subjects = List.of("Physics","Social");

        when(classRepository.findAll()).thenReturn(classLevels);
        when(subjectService.getSubjectsGivenClassCode(any(String.class))).thenReturn(subjects);
        List<ClassDto> responseClasses = classService.getAllClasses();
        assertThat(responseClasses).isNotNull();
        assertThat(2).isEqualTo(responseClasses.size());
    }

    @Test
    void getAllClassesIsNonNumeric() {
        List<ClassLevel> classLevels = List.of(new ClassLevel("C4","four"),
                new ClassLevel("C4","five"));
        List<String> subjects = List.of("Physics","Social");

        when(classRepository.findAll()).thenReturn(classLevels);
        when(subjectService.getSubjectsGivenClassCode(any(String.class))).thenReturn(subjects);
        List<ClassDto> responseClasses = classService.getAllClasses();
        assertThat(responseClasses).isNotNull();
        assertThat(2).isEqualTo(responseClasses.size());
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
    void updateClassShouldReturnClassLevel(){
        ClassLevel classLevel1 = new ClassLevel("C4", "4");
        ClassLevel expectedClass = new ClassLevel("C4","Four");
        ClassLevel classLevel2 = new ClassLevel();
        classLevel2.setName("Four");

        when(classRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(classLevel1));
        when(classRepository.save(any(ClassLevel.class))).thenReturn(expectedClass);
        ClassLevel actualClass= classService.updateClassLevelInfo("C4", classLevel2);
        assertEquals(expectedClass,actualClass);
    }

    @Test
    void updateClassShouldReturnException(){
        ClassLevel expectedClass = new ClassLevel("C5","5");
        ClassLevel classLevel2 = new ClassLevel();
        classLevel2.setName("5");

        when(classRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(classLevel));
        when(classRepository.save(any(ClassLevel.class))).thenReturn(expectedClass);
        assertThrows(RuntimeException.class, ()-> classService.updateClassLevelInfo("C4",classLevel2));
    }

    @Test
    void deleteClassByIdShouldReturnString(){
        when(classRepository.existsById(any(String.class))).thenReturn(true);
        classService.deleteClassLevelInfo("C4");
        verify(classRepository,times(1)).deleteById("C4");
    }

    @Test
    void deleteClassByIdShouldReturnNotFound(){
        when(classRepository.existsById(any(String.class))).thenReturn(false);
        assertThrows(RuntimeException.class,()->classService.deleteClassLevelInfo("C4"));
    }

    @Test
    void getClassByClassNameShouldReturnClassDto(){
        when(classRepository.findByNameIgnoreCase(anyString())).thenReturn(List.of(new ClassLevel("C4","4")));
        when(subjectService.getSubjectsGivenClassCode(any(String.class))).thenReturn(List.of("Physics","Social"));

        List<ClassDto> responseClasses = classService.getClassLevelByName("4");
        assertThat(responseClasses).isNotNull();
        assertThat(1).isEqualTo(responseClasses.size());
    }

    @Test
    void getClassByClassNameShouldReturnException(){
        when(classRepository.findByNameIgnoreCase(anyString())).thenThrow(RuntimeException.class);
        when(subjectService.getSubjectsGivenClassCode(any(String.class))).thenReturn(List.of("Physics","Social"));
        assertThrows(RuntimeException.class, ()-> classService.getClassLevelByName("4"));
    }

    @Test
    void getClassByClassNameShouldReturnNull(){
        when(classRepository.findByNameIgnoreCase(anyString())).thenReturn(List.of());
        when(subjectService.getSubjectsGivenClassCode(any(String.class))).thenReturn(List.of("Physics","Social"));
        List<ClassDto> responseClasses = classService.getClassLevelByName("4");
        assertThat(responseClasses).isNull();
        verify(subjectService,times(0)).getSubjectsGivenClassCode(any(String.class));

    }

    @Test
    void getClassCodeFromNameSuccessful() {
        List<ClassLevel> classLevels = List.of(new ClassLevel("C4","4"));
        List<String> subjects = List.of("Physics","Social");
        when(classRepository.findAll()).thenReturn(classLevels);
        when(subjectService.getSubjectsGivenClassCode(any(String.class))).thenReturn(subjects);

        String expected = "C4";
        String actual = classService.getClassCodeFromName("4");
        assertEquals(expected,actual);
    }
}
