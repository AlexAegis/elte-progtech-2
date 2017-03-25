package com.alexaegis.progtech.models.people;

public enum PersonTypes {
    ACTOR,
    DIRECTOR;

    @Override
    public String toString() {
        return super.toString().substring(0, 1)
                + super.toString().substring(1).toLowerCase();
    }
}
