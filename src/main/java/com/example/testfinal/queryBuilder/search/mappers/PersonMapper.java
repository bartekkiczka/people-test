package com.example.testfinal.queryBuilder.search.mappers;

import com.example.testfinal.model.Person;

import java.sql.ResultSet;

public interface PersonMapper {
    String getType();

    Person map(ResultSet rs) throws Exception;
}
