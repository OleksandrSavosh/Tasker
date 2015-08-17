package com.aleksander.savosh.tasker.model.relational;

import java.util.Date;

public class Property extends BaseModel {

    protected String noticeId;
    protected Integer type;
    protected String text;
    protected Date date;

    Property() {
    }
    Property(String objectId, Date createdAt, Date updatedAt, String noticeId, Integer type, String text, Date date) {
        super(objectId, createdAt, updatedAt);
        this.noticeId = noticeId;
        this.type = type;
        this.text = text;
        this.date = date;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public Integer getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }

    public static PropertyBuilder builder(){
        return new PropertyBuilder();
    }
    public static PropertyBuilder builder(Property property) {
        return new PropertyBuilder(property);
    }
    @Override
    public String toString() {
        return "Property{" +
                "noticeId='" + noticeId + '\'' +
                ", type=" + type +
                ", text='" + text + '\'' +
                ", date=" + date +
                "} " + super.toString();
    }


}