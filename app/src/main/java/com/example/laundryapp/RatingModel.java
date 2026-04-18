package com.example.laundryapp;
public class RatingModel {
    private int id, orderId, userId, score;
    private String review, date, userName, serviceName;
    public int getId(){return id;} public void setId(int v){id=v;}
    public int getOrderId(){return orderId;} public void setOrderId(int v){orderId=v;}
    public int getUserId(){return userId;} public void setUserId(int v){userId=v;}
    public int getScore(){return score;} public void setScore(int v){score=v;}
    public String getReview(){return review;} public void setReview(String v){review=v;}
    public String getDate(){return date;} public void setDate(String v){date=v;}
    public String getUserName(){return userName;} public void setUserName(String v){userName=v;}
    public String getServiceName(){return serviceName;} public void setServiceName(String v){serviceName=v;}
    public String getStars(){StringBuilder sb=new StringBuilder();for(int i=0;i<5;i++)sb.append(i<score?"★":"☆");return sb.toString();}
}
