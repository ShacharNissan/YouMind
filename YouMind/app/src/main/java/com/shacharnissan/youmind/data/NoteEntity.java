package com.shacharnissan.youmind.data;

import java.util.ArrayList;
import java.util.Date;

public class NoteEntity extends YouMindEntity{
    private String value;

    public NoteEntity(String name, ArrayList<String> labels, String value) {
        this(null, name, new Date(), labels, value);
    }

    public NoteEntity(String id, String name, Date createDate, ArrayList<String> labels, String value) {
        super(id, name, createDate, labels);
        setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
