package com.shacharnissan.youmind.data;

import java.util.ArrayList;
import java.util.Date;

public abstract class YouMindEntity {
    private String id;
    private String name;
    private Date createDate;
    private ArrayList<String> labels;

    public YouMindEntity(String name) {
        this(null, name);
    }

    public YouMindEntity(String id, String name) {
        this(id, name, new Date());
    }

    public YouMindEntity(String id, String name, Date createDate) {
        this(id, name, createDate, new ArrayList<>());
    }

    public YouMindEntity(String id, String name, Date createDate, ArrayList<String> labels) {
        setId(id);
        setName(name);
        setCreateDate(createDate);
        setLabels(labels);
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

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<String> labels) {
        if (labels == null)
            throw new RuntimeException("setLabels: Labels array is null.");

        this.labels = labels;
    }

    public void setLabel(String name) {
        if (name == null || name.isEmpty())
            throw new RuntimeException("setLabel: Label name cannot be Empty.");

        labels.add(name);
    }
}
