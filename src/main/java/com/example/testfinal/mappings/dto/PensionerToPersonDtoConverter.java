package com.example.testfinal.mappings.dto;

import com.example.testfinal.model.Pensioner;
import com.example.testfinal.model.dto.PensionerDto;
import com.example.testfinal.model.dto.PersonDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PensionerToPersonDtoConverter implements Converter<Pensioner, PersonDto> {

    @Override
    public PersonDto convert(MappingContext<Pensioner, PersonDto> mappingContext) {
        Pensioner pensioner = mappingContext.getSource();

        return PensionerDto.builder()
                .id(pensioner.getId())
                .name(pensioner.getName())
                .surname(pensioner.getSurname())
                .pesel(pensioner.getPesel())
                .height(pensioner.getHeight())
                .weight(pensioner.getWeight())
                .email(pensioner.getEmail())
                .pension(pensioner.getPension())
                .yearsWorked(pensioner.getYearsWorked())
                .build();
    }
}
