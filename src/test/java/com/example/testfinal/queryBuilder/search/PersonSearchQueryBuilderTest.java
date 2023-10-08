package com.example.testfinal.queryBuilder.search;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PersonSearchQueryBuilderTest {
    @Test
    public void testBuildSearchQueryStringForString() {
        Map.Entry<String, Object> entry = new AbstractMap.SimpleEntry<>("firstName", "John");
        String queryString = PersonSearchQueryBuilder.buildSearchQuery(entry);
        assertEquals("LOWER(first_name) LIKE LOWER('%John%')", queryString);
    }

    @Test
    public void testBuildSearchQueryStringForInteger() {
        Map.Entry<String, Object> entry = new AbstractMap.SimpleEntry<>("age", 30);
        String queryString = PersonSearchQueryBuilder.buildSearchQuery(entry);
        assertEquals("age = 30", queryString);
    }

    @Test
    public void testBuildSearchQueryStringForIntegerMin() {
        Map.Entry<String, Object> entry = new AbstractMap.SimpleEntry<>("minAge", 25);
        String queryString = PersonSearchQueryBuilder.buildSearchQuery(entry);
        assertEquals("age >= 25", queryString);
    }

    @Test
    public void testBuildSearchQueryStringForIntegerMax() {
        Map.Entry<String, Object> entry = new AbstractMap.SimpleEntry<>("maxAge", 40);
        String queryString = PersonSearchQueryBuilder.buildSearchQuery(entry);
        assertEquals("age <= 40", queryString);
    }

    @Test
    public void testBuildSearchQueryStringForDate() {
        LocalDate date = LocalDate.of(1990, 5, 15);
        Map.Entry<String, Object> entry = new AbstractMap.SimpleEntry<>("minBirthDate", "1990-05-15");
        String queryString = PersonSearchQueryBuilder.buildSearchQuery(entry);
        assertEquals("birth_date >= DATE '1990-05-15'", queryString);
    }

}