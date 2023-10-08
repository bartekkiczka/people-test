package com.example.testfinal.queryBuilder.search.mappers;

import com.example.testfinal.enums.PersonTypes;
import com.example.testfinal.model.Person;
import com.example.testfinal.model.Student;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;

@Service
public class StudentMapper implements PersonMapper {
    @Override
    public String getType() {
        return PersonTypes.STUDENT.toString();
    }

    @Override
    public Person map(ResultSet rs) throws Exception {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String surname = rs.getString("surname");
        long pesel = rs.getLong("pesel");
        int height = rs.getInt("height");
        int weight = rs.getInt("weight");
        String email = rs.getString("email");
        String schoolName = rs.getString("school_name");
        int yearOfStudy = rs.getInt("year_of_study");
        String fieldOfStudy = rs.getString("field_of_study");
        int scholarship = rs.getInt("scholarship");

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setSurname(surname);
        student.setPesel(pesel);
        student.setHeight(height);
        student.setWeight(weight);
        student.setEmail(email);
        student.setSchoolName(schoolName);
        student.setYearOfStudy(yearOfStudy);
        student.setFieldOfStudy(fieldOfStudy);
        student.setScholarship(scholarship);
        return student;
    }
}
