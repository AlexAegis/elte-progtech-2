package com.alexaegis.progtech.database;

import com.alexaegis.progtech.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.lang.Thread.sleep;

public class MovieInserterTest {

    Connector connector;

    @BeforeEach
    public void beforeTests() throws Exception {

    }

    @Test
    public void mainTest() throws Exception {

    }

    @AfterEach
    public void afterTests() {
        connector.disconnect();
    }

}