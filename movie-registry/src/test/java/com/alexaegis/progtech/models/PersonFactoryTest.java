package com.alexaegis.progtech.models;

import com.alexaegis.progtech.misc.InvalidBirthDateException;
import com.alexaegis.progtech.misc.PersonException;
import com.alexaegis.progtech.models.people.Name;
import com.alexaegis.progtech.models.people.NameBuilder;
import com.alexaegis.progtech.models.people.PersonFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class PersonFactoryTest {

    private Name validName;
    private Date validBirthDate;
    private Name invalidName;
    private Date invalidBirthDate;

    @BeforeEach
    public void setUp() throws Exception {
        Calendar calendar = new GregorianCalendar();
        calendar.set(2000,Calendar.AUGUST,10);
        validBirthDate = calendar.getTime();
        calendar.set(3000,Calendar.JULY,20);
        invalidBirthDate = calendar.getTime();
        validName = new NameBuilder("Abraham Lincoln").getName();
    }

    @Test
    public void actorCreateTest() throws Exception {
        PersonFactory.createActor(0, validName, validBirthDate);
    }
/*
    @Test(expected = InvalidBirthDateException.class)
    public void createFromFutureTest() throws Exception {
        PersonFactory.createActor(0, validName, invalidBirthDate);

        Throwable throwable = Assertions.assertThrows(PersonException.class, () -> new PersonException(""));
        Assertions.assertEquals();
    }*/
}
