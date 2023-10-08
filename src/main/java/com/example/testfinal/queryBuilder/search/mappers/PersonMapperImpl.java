package com.example.testfinal.queryBuilder.search.mappers;

import com.example.testfinal.model.Person;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PersonMapperImpl {

    private final Map<String, PersonMapper> mappers;

    public PersonMapperImpl(Set<PersonMapper> mappers) {
        this.mappers = mappers.stream().collect(Collectors.toMap(PersonMapper::getType, Function.identity()));
    }

    public Person map(ResultSet rs) throws Exception {
        return Optional.ofNullable(mappers.get(rs.getString("person_type")))
                .map(mapper -> {
                    try {
                        return mapper.map(rs);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow(RuntimeException::new);
    }
}
