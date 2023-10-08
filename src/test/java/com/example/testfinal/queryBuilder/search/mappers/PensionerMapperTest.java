package com.example.testfinal.queryBuilder.search.mappers;

import com.example.testfinal.model.Pensioner;
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
class PensionerMapperTest {

    @Autowired
    private PensionerMapper pensionerMapper;

    @Test
    public void testMapPensioner() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getLong("id")).thenReturn(2L);
        when(resultSet.getString("name")).thenReturn("Alice");
        when(resultSet.getString("surname")).thenReturn("Smith");
        when(resultSet.getLong("pesel")).thenReturn(9876543210L);
        when(resultSet.getInt("height")).thenReturn(160);
        when(resultSet.getInt("weight")).thenReturn(70);
        when(resultSet.getString("email")).thenReturn("alice@example.com");
        when(resultSet.getInt("pension")).thenReturn(1500);
        when(resultSet.getInt("years_worked")).thenReturn(30);

        Pensioner pensioner = (Pensioner) pensionerMapper.map(resultSet);

        assertEquals(2L, pensioner.getId());
        assertEquals("Alice", pensioner.getName());
        assertEquals("Smith", pensioner.getSurname());
        assertEquals(9876543210L, pensioner.getPesel());
        assertEquals(160, pensioner.getHeight());
        assertEquals(70, pensioner.getWeight());
        assertEquals("alice@example.com", pensioner.getEmail());
        assertEquals(1500, pensioner.getPension());
        assertEquals(30, pensioner.getYearsWorked());
    }
}