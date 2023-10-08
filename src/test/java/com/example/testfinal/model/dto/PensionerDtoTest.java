package com.example.testfinal.model.dto;

import com.example.testfinal.model.Pensioner;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class PensionerDtoTest {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void testPensionerToPensionerDtoConverter() {
        Pensioner pensioner = Pensioner.builder()
                .name("name")
                .surname("surname3")
                .pesel(11223344556L)
                .height(30)
                .weight(30)
                .email("testpensioner@gmail.com")
                .pension(2000)
                .yearsWorked(30)
                .build();


        PensionerDto pensionerDto = modelMapper.map(pensioner, PensionerDto.class);

        assertEquals(pensionerDto.getType(), pensioner.getType());
        assertEquals(pensionerDto.getName(), pensioner.getName());
        assertEquals(pensionerDto.getSurname(), pensioner.getSurname());
        assertEquals(pensionerDto.getPesel(), pensioner.getPesel());
        assertEquals(pensionerDto.getHeight(), pensioner.getHeight());
        assertEquals(pensionerDto.getWeight(), pensioner.getWeight());
        assertEquals(pensionerDto.getEmail(), pensioner.getEmail());
        assertEquals(pensionerDto.getPension(), pensioner.getPension());
        assertEquals(pensionerDto.getYearsWorked(), pensioner.getYearsWorked());
    }
}
