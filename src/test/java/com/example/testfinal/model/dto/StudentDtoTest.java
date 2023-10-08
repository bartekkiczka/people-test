package com.example.testfinal.model.dto;

import com.example.testfinal.model.Student;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class StudentDtoTest {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void testStudentToStudentDtoConverter() {

        Student student = Student.builder()
                .name("name")
                .surname("surname3")
                .pesel(11223344556L)
                .height(30)
                .weight(30)
                .email("teststudent@gmail.com")
                .schoolName("school")
                .yearOfStudy(5)
                .fieldOfStudy("field")
                .scholarship(200)
                .build();

        StudentDto studentDto = modelMapper.map(student, StudentDto.class);

        assertEquals(studentDto.getType(), student.getType());
        assertEquals(studentDto.getName(), student.getName());
        assertEquals(studentDto.getSurname(), student.getSurname());
        assertEquals(studentDto.getPesel(), student.getPesel());
        assertEquals(studentDto.getHeight(), student.getHeight());
        assertEquals(studentDto.getWeight(), student.getWeight());
        assertEquals(studentDto.getEmail(), student.getEmail());
        assertEquals(studentDto.getSchoolName(), student.getSchoolName());
        assertEquals(studentDto.getYearOfStudy(), student.getYearOfStudy());
        assertEquals(studentDto.getFieldOfStudy(), student.getFieldOfStudy());
        assertEquals(studentDto.getScholarship(), student.getScholarship());
    }
}
