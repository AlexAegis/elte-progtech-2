package com.alexaegis.progtech;

import com.alexaegis.progtech.logic.SecureProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MainTest {
    SecureProperties properties = new SecureProperties();
    @Before
    public void beforeTests() {

    }

    @Test
    public void mainTest() {
        char[] chars = new char[]{'a', 'b', 'c'};
        properties.setPropertyObject("asd", chars);
        System.out.println(properties.getPropertyObject("asd"));
    }

    @After
    public void afterTests() {

    }

}