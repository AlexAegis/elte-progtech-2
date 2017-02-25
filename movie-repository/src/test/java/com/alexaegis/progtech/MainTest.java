package com.alexaegis.progtech;

import com.alexaegis.progtech.logic.SecureProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;

public class MainTest {
    SecureProperties properties = new SecureProperties();
    @Before
    public void beforeTests() {

    }

    @Test
    public void mainTest() {
        System.out.println(Date.valueOf("2017-01-01"));
    }

    @After
    public void afterTests() {

    }

}