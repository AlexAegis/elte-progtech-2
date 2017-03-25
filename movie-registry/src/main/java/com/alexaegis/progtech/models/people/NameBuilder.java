package com.alexaegis.progtech.models.people;

import com.alexaegis.progtech.misc.IllegalNameException;

import java.util.Arrays;


public class NameBuilder {

    private Name name;

    public NameBuilder() {
        this.name = new Name();
    }

    public NameBuilder(String names) throws IllegalNameException {
        this();
        if(names.isEmpty()) {
            throw new IllegalNameException("Namelist is empty");
        }
        Arrays.asList(names.split(" ")).forEach(s -> {
            try {
                this.addName(s);
            } catch (IllegalNameException e) {
                e.printStackTrace();
            }
        });
        if (this.name.getNames().size() < 2) {
            throw new IllegalNameException("Only one name");
        }
    }

    public NameBuilder addPrefix(String prefix) throws IllegalNameException {
        if(prefix.isEmpty()) {
            throw new IllegalNameException("Prefix is empty");
        } else {
            this.name.setPreFix(prefix);
            return this;
        }
    }

    public NameBuilder addName(String name) throws IllegalNameException {
        if(name.isEmpty() || Character.isLowerCase(name.charAt(0))) {
            throw new IllegalNameException("Name empty or starts with a lowercase letter");
        } else {
            this.name.addName(name);
            return this;
        }
    }

    public NameBuilder addPostFix(String postfix) throws IllegalNameException {
        if(postfix.isEmpty()) {
            throw new IllegalNameException("Postfix empty");
        } else {
            this.name.setPostFix(postfix);
            return this;
        }
    }

    public Name getName() {
        return this.name;
    }

}