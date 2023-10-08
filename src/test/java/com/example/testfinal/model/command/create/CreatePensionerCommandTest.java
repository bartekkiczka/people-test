package com.example.testfinal.model.command.create;

import com.example.testfinal.enums.PersonTypes;
import com.example.testfinal.model.Pensioner;
import com.example.testfinal.model.command.create.CreatePensionerCommand;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CreatePensionerCommandTest {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void testCreatePensionerCommandToPensionerConversion(){
        //given
        CreatePensionerCommand createPensionerCommand = CreatePensionerCommand.builder()
                .type(PersonTypes.PENSIONER)
                .name("name")
                .surname("surname")
                .pesel(11223344556L)
                .height(30)
                .weight(30)
                .email("testempensioner@gmail.com")
                .yearsWorked(20)
                .pension(2000)
                .build();

        //when
        Pensioner pensioner = modelMapper.map(createPensionerCommand, Pensioner.class);

        //then
        assertEquals(pensioner.getType(), PersonTypes.PENSIONER);
        assertEquals(pensioner.getName(), "name");
        assertEquals(pensioner.getSurname(), "surname");
        assertEquals(pensioner.getPesel(), 11223344556L);
        assertEquals(pensioner.getHeight(), 30);
        assertEquals(pensioner.getWeight(), 30);
        assertEquals(pensioner.getEmail(), "testempensioner@gmail.com");
        assertEquals(pensioner.getYearsWorked(), 20);
        assertEquals(pensioner.getPension(), 2000);
    }

}