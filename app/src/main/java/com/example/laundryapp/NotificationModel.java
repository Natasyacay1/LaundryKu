package com.example.laundryapp;
public class NotificationModel {
    private int id, userId, orderId; private String title, message, date; private boolean read;
    public int getId(){return id;} public void setId(int v){id=v;}
    public int getUserId(){return userId;} public void setUserId(int v){userId=v;}
    public int getOrderId(){return orderId;} public void setOrderId(int v){orderId=v;}
    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getMessage(){return message;} public void setMessage(String v){message=v;}
    public String getDate(){return date;} public void setDate(String v){date=v;}
    public boolean isRead(){return read;} public void setRead(boolean v){read=v;}
}
