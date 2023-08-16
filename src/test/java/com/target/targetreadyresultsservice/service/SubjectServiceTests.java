package com.target.targetreadyresultsservice.service;


import com.target.targetreadyresultsservice.model.Subject;
import com.target.targetreadyresultsservice.repository.SubjectRepository;
import com.target.targetreadyresultsservice.service.SubjectService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.Mockito.*;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
public class SubjectServiceTests {
   private SubjectRepository repository= Mockito.mock(SubjectRepository.class);
   private SubjectService service=new SubjectService(repository);


    @Test public void testToSaveSubject() throws Exception
    {

        Subject subject=new Subject();
        subject.setSubjectName("Physics");
        subject.setCredits(5);
        subject.setClassCode("C1");
        subject.setMaxTestMarks(20);
        subject.setMaxExamMarks(80);
        when(repository.save(any(Subject.class))).thenReturn(subject);
        Subject sub1=service.addSubject(subject);
        assertEquals(subject,sub1);
    }
    @Test public void testToSaveSubjectAlreadyPresent()
    {

        Subject subject=new Subject();
        //subject.setSubjectCode("S-PhC1");
        subject.setSubjectName("Physics");
        subject.setCredits(5);
        subject.setClassCode("C1");
        subject.setMaxTestMarks(20);
        subject.setMaxExamMarks(80);
        when(repository.findById(anyString())).thenReturn(Optional.of(subject));
        assertThrows(RuntimeException.class,()->service.addSubject(subject));
        verify(repository,times(0)).save(subject);
    }
    @Test public void  testToGetAllSubjects()
    {
        Subject subject=new Subject();
        subject.setSubjectName("Physics");
        subject.setCredits(5);
        subject.setClassCode("C1");
        subject.setMaxTestMarks(20);
        subject.setMaxExamMarks(80);
        Subject subject1=new Subject();
        subject1.setSubjectName("English");
        subject1.setCredits(5);
        subject1.setClassCode("C1");
        subject.setMaxTestMarks(20);
        subject.setMaxExamMarks(80);
       //given(repository.findAll()).willReturn(List.of(subject1));
        when(repository.findAll()).thenReturn(List.of(subject1,subject));
        List<Subject> subjects=service.getSubjects();
        assertThat(subjects).isNotNull();
        assertThat(2).isEqualTo(subjects.size());

    }
    @Test public void testToListAllSubjectsInCaseOfEmpty()
    {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(RuntimeException.class,()->service.getSubjects());
    }
    @Test public void testToGetSubjectByIdInCaseOfEmpty()
    {
        when(repository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,()->service.getSubjectById("S-EnC1"));
    }
    @Test public void testToGetSubjectById()
    {
        Subject subject1=new Subject();
        subject1.setSubjectCode("S-EnC1");
        subject1.setSubjectName("English");
        subject1.setCredits(5);
        subject1.setClassCode("C1");
        subject1.setMaxTestMarks(20);
        subject1.setMaxExamMarks(80);
        Optional<Subject> sub=Optional.of(subject1);
        when(repository.findById(anyString())).thenReturn(sub);
        Optional<Subject> sub1=service.getSubjectById("S-EnC1");
        assertThat(sub1).isNotNull();
        assertEquals(sub,sub1);
    }
    @Test public void testToUpdateSubjectInCaseOfNotFound()
    {
        Subject subject=new Subject();
        subject.setSubjectName("Physics");
        subject.setCredits(5);
        subject.setClassCode("C1");
        subject.setMaxTestMarks(20);
        subject.setMaxExamMarks(80);
        assertThrows(RuntimeException.class,()->service.updateSubject("S-PhC1",subject));
    }
    @Test public void testToUpdateSubject()
    {
        Subject subject=new Subject();
        subject.setSubjectName("Physics");
        subject.setCredits(5);
        subject.setClassCode("C1");
        subject.setMaxTestMarks(20);
        subject.setMaxExamMarks(80);
        when(repository.findById(anyString())).thenReturn(Optional.of(subject));
        Subject newsubject=new Subject();
        newsubject.setSubjectName("Physics");
        newsubject.setCredits(10);
        newsubject.setClassCode("C1");
        subject.setMaxTestMarks(15);
        subject.setMaxExamMarks(80);
        when(repository.save(any(Subject.class))).thenReturn(newsubject);
        assertEquals(newsubject.toString(),service.updateSubject("S_PhC1",newsubject).toString());

    }
    @Test public void testToDeleteSubjectByIdNotFound()
    {
        assertThrows(RuntimeException.class,()->service.deleteSubject("S-EnC1"));
    }
    @Test public void testToDeleteSubjectById()
    {
        Subject subject=new Subject();
        subject.setSubjectName("Physics");
        subject.setCredits(5);
        subject.setClassCode("C1");
        subject.setMaxTestMarks(20);
        subject.setMaxExamMarks(80);
        when(repository.findById(anyString())).thenReturn(Optional.of(subject));
        String actual=service.deleteSubject("S_PhC1");
        assertEquals("S_PhC1",actual);
        verify(repository,times(1)).deleteById("S_PhC1");
        //assertThrows(RuntimeException.class,()->service.deleteSubject("S-EnC1"));
    }

    @Test
    public void testToGetSubjectsGivenClassCode(){
        List<Subject> sub=List.of(new Subject("S_PhC4","Physics",100,"C4",100,25),
                new Subject("S_MaC4","Math",100,"C4",100,25),
                new Subject("S_SoC4","Social",100,"C4",100,25));
        List<String> expectedOutput=List.of("Math","Physics","Social");

        when(repository.findByClassCode(anyString())).thenReturn(sub);
        List<String> actualOutput=service.getSubjectsGivenClassCode("C4");
        assertThat(actualOutput).isNotNull();
        assertEquals(expectedOutput,actualOutput);
    }

    @Test
    public void testToGetSubjectsObjectGivenClassCodeToReturnSingleObject(){
        List<Subject> sub=List.of(new Subject("S_PhC4","Physics",100,"C4",100,25));
        when(repository.findByClassCode(anyString())).thenReturn(sub);
        List<Subject> actual=service.getSubjectsObjectGivenClassCode("C4");
        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(1);
    }

    @Test
    public void testToGetSubjectObjectGivenClassCodeReturnsNull(){
        when(repository.findByClassCode(anyString())).thenReturn(List.of());
        List<Subject> actual=service.getSubjectsObjectGivenClassCode("C4");
        assertThat(actual).isNull();
    }

    @Test
    public void testToGetSubjectObjectGivenClassCodeReturnsObjectsList(){
        Subject sub1=new Subject("S_PhC4","Physics",100,"C4",100,25);
        Subject sub2=new Subject("S_MaC4","Math",100,"C4",100,25);
        List<Subject> sub = new ArrayList<>();
        sub.add(sub1);
        sub.add(sub2);
        when(repository.findByClassCode(anyString())).thenReturn(sub);
        List<Subject> actual=service.getSubjectsObjectGivenClassCode("C4");
        Collections.sort(sub, (s1, s2) -> s1.getSubjectName().compareToIgnoreCase(s2.getSubjectName()));
        assertThat(actual).isNotNull();
        assertEquals(sub,actual);
        assertThat(actual.size()).isEqualTo(2);
    }
}
