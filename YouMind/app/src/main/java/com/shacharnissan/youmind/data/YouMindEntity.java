package com.shacharnissan.youmind.data;

import java.util.Date;

public abstract class YouMindEntity {
    private String name;
    private String id;
    private Date createDate;

    public YouMindEntity(String name) {
        this(null, name, new Date());
    }

    public YouMindEntity(String name, String id) {
        this(id, name, new Date());
    }

    public YouMindEntity(String id, String name, Date createDate) {
        setId(id);
        setName(name);
        setCreateDate(createDate);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.isEmpty())
            throw new RuntimeException("Name can not be empty!");

        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        if (createDate == null)
            throw new RuntimeException("createDate can not be empty!");

        this.createDate = createDate;
    }
}
