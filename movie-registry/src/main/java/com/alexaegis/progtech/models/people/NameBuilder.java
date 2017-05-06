package com.alexaegis.progtech.models.people;

import com.alexaegis.progtech.misc.IllegalNameExceptionIllegal;

import java.util.Arrays;


public class NameBuilder {

    private Name name;

    public NameBuilder() {
        this.name = new Name();
    }

    public NameBuilder(String names) throws IllegalNameExceptionIllegal {
        this();
        if(names.isEmpty()) {
            throw new IllegalNameExceptionIllegal("Namelist is empty");
        }
        Arrays.asList(names.split(" ")).forEach(s -> {
            try {
                this.addName(s);
            } catch (IllegalNameExceptionIllegal e) {
                e.printStackTrace();
            }
        });
        if (this.name.getNames().size() < 2) {
            throw new IllegalNameExceptionIllegal("Only one name");
        }
    }

    public NameBuilder addPrefix(String prefix) throws IllegalNameExceptionIllegal {
        if(prefix.isEmpty()) {
            throw new IllegalNameExceptionIllegal("Prefix is empty");
        } else {
            this.name.setPreFix(prefix);
            return this;
        }
    }

    public NameBuilder addName(String name) throws IllegalNameExceptionIllegal {
        if(name.isEmpty() || Character.isLowerCase(name.charAt(0))) {
            throw new IllegalNameExceptionIllegal("Name empty or starts with a lowercase letter");
        } else {
            this.name.addName(name);
            return this;
        }
    }

    public NameBuilder addPostFix(String postfix) throws IllegalNameExceptionIllegal {
        if(postfix.isEmpty()) {
            throw new IllegalNameExceptionIllegal("Postfix empty");
        } else {
            this.name.setPostFix(postfix);
            return this;
        }
    }

    public Name getName() {
        return this.name;
    }

}