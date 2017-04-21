package com.alexaegis.progtech;

import com.alexaegis.progtech.logic.SecureProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

public class MainTest {

    SecureProperties properties = new SecureProperties();

    @BeforeEach
    public void beforeTests() {

    }

    @Test
    public void mainTest() {
        System.out.println(Date.valueOf("2017-01-01"));
    }

    @AfterEach
    public void afterTests() {

    }

}