package com.example.testfinal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class TestCsvGenerator {
    public static void generateEventsFile() {
        String csvFile = "people.csv";
        int numberOfPeople = 5000;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            Random random = new Random();
            writer.write("person_type,name,surname,pesel,height,weight,email,employment_start_date,position,salary,school_name,year_of_study,field_of_study,scholarship,pension,years_worked\n");

            for (int i = 1; i <= numberOfPeople; i++) {
                String type = "STUDENT";
                String name = "name";
                String surname = "surname";
                Long pesel = generateRandom11DigitNumber();
                Integer height = 10;
                Integer weight = 10;
                String email = "email" + i + "@test.com";
                String employmentStartDate = null;
                String position = null;
                Integer salary = null;
                String schoolName = "school";
                Integer yearOfStudy = 2;
                String fieldOfStudy = "field";
                Integer scholarship = 100;
                Integer pension = null;
                Integer yearsWorked = null;

                String line = String.format("%s,%s,%s,%d,%d,%d,%s,%s,%s,%s,%s,%d,%s,%s,%s,%s\n",
                        type, name, surname, pesel, height, weight, email, employmentStartDate,
                        position, salary, schoolName, yearOfStudy, fieldOfStudy, scholarship, pension, yearsWorked);
                writer.write(line);
            }

            System.out.println("Wygenerowano plik CSV z " + numberOfPeople + " osobami.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long generateRandom11DigitNumber() {
        Random random = new Random();
        long min = 10000000000L;
        long max = 99999999999L;
        long randomValue = random.nextLong(min, max);
        return Math.abs(randomValue);
    }
}
