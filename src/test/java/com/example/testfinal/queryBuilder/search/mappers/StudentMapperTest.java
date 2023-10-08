package com.example.testfinal.queryBuilder.search.mappers;

import com.example.testfinal.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class StudentMapperTest {

    @Autowired
    private StudentMapper studentMapper;

    @Test
    public void testMapStudent() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getLong("id")).thenReturn(3L);
        when(resultSet.getString("name")).thenReturn("Emma");
        when(resultSet.getString("surname")).thenReturn("Johnson");
        when(resultSet.getLong("pesel")).thenReturn(1231231234L);
        when(resultSet.getInt("height")).thenReturn(170);
        when(resultSet.getInt("weight")).thenReturn(60);
        when(resultSet.getString("email")).thenReturn("emma@example.com");
        when(resultSet.getString("school_name")).thenReturn("University of Example");
        when(resultSet.getInt("year_of_study")).thenReturn(2);
        when(resultSet.getString("field_of_study")).thenReturn("Computer Science");
        when(resultSet.getInt("scholarship")).thenReturn(1000);

        Student student = (Student) studentMapper.map(resultSet);

        assertEquals(3L, student.getId());
        assertEquals("Emma", student.getName());
        assertEquals("Johnson", student.getSurname());
        assertEquals(1231231234L, student.getPesel());
        assertEquals(170, student.getHeight());
        assertEquals(60, student.getWeight());
        assertEquals("emma@example.com", student.getEmail());
        assertEquals("University of Example", student.getSchoolName());
        assertEquals(2, student.getYearOfStudy());
        assertEquals("Computer Science", student.getFieldOfStudy());
        assertEquals(1000, student.getScholarship());
    }

}