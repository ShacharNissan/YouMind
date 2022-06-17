package com.shacharnissan.youmind.data;

import java.util.Date;

public class NoteEntity extends YouMindEntity{
    private String value;

    public NoteEntity(String name, Date createDate, String value) {
        this(null, name, createDate, value);
    }

    public NoteEntity(String id, String name, Date createDate, String value) {
        super(id, name, createDate);
        setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
