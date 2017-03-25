package com.alexaegis.progtech.models;

import com.alexaegis.progtech.models.people.PersonTypes;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PersonTypesTest {

    @Test
    public void actorNameCheck() throws Exception {
        assertEquals("Actor name correct",  PersonTypes.ACTOR.toString(), "Actor");
    }

    @Test
    public void directorNameCheck() throws Exception {
        assertEquals("Director name correct",  PersonTypes.DIRECTOR.toString(), "Director");
    }
}