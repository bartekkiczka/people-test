package com.example.testfinal.queryBuilder.search.mappers;

import com.example.testfinal.enums.PersonTypes;
import com.example.testfinal.model.Pensioner;
import com.example.testfinal.model.Person;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;

@Service
public class PensionerMapper implements PersonMapper {
    @Override
    public String getType() {
        return PersonTypes.PENSIONER.toString();
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
        int pension = rs.getInt("pension");
        int yearsWorked = rs.getInt("years_worked");

        Pensioner pensioner = new Pensioner();
        pensioner.setId(id);
        pensioner.setName(name);
        pensioner.setSurname(surname);
        pensioner.setPesel(pesel);
        pensioner.setHeight(height);
        pensioner.setWeight(weight);
        pensioner.setEmail(email);
        pensioner.setPension(pension);
        pensioner.setYearsWorked(yearsWorked);
        return pensioner;
    }
}
