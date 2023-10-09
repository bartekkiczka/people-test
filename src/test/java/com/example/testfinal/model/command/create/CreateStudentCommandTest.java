package com.example.testfinal.model.command.create;

import com.example.testfinal.model.Student;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CreateStudentCommandTest {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void testCreateStudentCommandToStudentConversion(){
        //given
        CreateStudentCommand createStudentCommand = CreateStudentCommand.builder()
                .type("STUDENT")
                .name("name")
                .surname("surname")
                .pesel(11223344556L)
                .height(30)
                .weight(30)
                .email("teststudent@gmail.com")
                .schoolName("school")
                .yearOfStudy(5)
                .fieldOfStudy("field")
                .scholarship(500)
                .build();

        //when
        Student student = modelMapper.map(createStudentCommand, Student.class);

        //then
        assertEquals(student.getType(), "STUDENT");
        assertEquals(student.getName(), "name");
        assertEquals(student.getSurname(), "surname");
        assertEquals(student.getPesel(), 11223344556L);
        assertEquals(student.getHeight(), 30);
        assertEquals(student.getWeight(), 30);
        assertEquals(student.getEmail(), "teststudent@gmail.com");
        assertEquals(student.getSchoolName(), "school");
        assertEquals(student.getYearOfStudy(), 5);
        assertEquals(student.getFieldOfStudy(), "field");
        assertEquals(student.getScholarship(), 500);
    }
}