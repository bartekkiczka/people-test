package com.example.testfinal.mappings.dto;

import com.example.testfinal.model.Student;
import com.example.testfinal.model.dto.PersonDto;
import com.example.testfinal.model.dto.StudentDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentToPersonDtoConverter implements Converter<Student, PersonDto> {

    @Override
    public PersonDto convert(MappingContext<Student, PersonDto> mappingContext) {
        Student student = mappingContext.getSource();

        return StudentDto.builder()
                .id(student.getId())
                .name(student.getName())
                .surname(student.getSurname())
                .pesel(student.getPesel())
                .height(student.getHeight())
                .weight(student.getWeight())
                .email(student.getEmail())
                .schoolName(student.getSchoolName())
                .yearOfStudy(student.getYearOfStudy())
                .fieldOfStudy(student.getFieldOfStudy())
                .scholarship(student.getScholarship())
                .build();
    }
}
