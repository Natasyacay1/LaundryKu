package com.example.laundryapp;
public class OrderModel {
    private int id, userId, serviceId;
    private double weight, totalPrice, discount, finalTotal;
    private String status, orderDate, finishDate, estimateDate, notes, promoCode;
    private String userName, serviceName, serviceUnit;
    public OrderModel(){}
    public int getId(){return id;} public void setId(int v){id=v;}
    public int getUserId(){return userId;} public void setUserId(int v){userId=v;}
    public int getServiceId(){return serviceId;} public void setServiceId(int v){serviceId=v;}
    public double getWeight(){return weight;} public void setWeight(double v){weight=v;}
    public double getTotalPrice(){return totalPrice;} public void setTotalPrice(double v){totalPrice=v;}
    public double getDiscount(){return discount;} public void setDiscount(double v){discount=v;}
    public double getFinalTotal(){return finalTotal;} public void setFinalTotal(double v){finalTotal=v;}
    public String getPromoCode(){return promoCode;} public void setPromoCode(String v){promoCode=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public String getOrderDate(){return orderDate;} public void setOrderDate(String v){orderDate=v;}
    public String getFinishDate(){return finishDate;} public void setFinishDate(String v){finishDate=v;}
    public String getEstimateDate(){return estimateDate;} public void setEstimateDate(String v){estimateDate=v;}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
    public String getUserName(){return userName;} public void setUserName(String v){userName=v;}
    public String getServiceName(){return serviceName;} public void setServiceName(String v){serviceName=v;}
    public String getServiceUnit(){return serviceUnit;} public void setServiceUnit(String v){serviceUnit=v;}
    public String getFormattedTotal(){return "Rp "+String.format("%,.0f",finalTotal>0?finalTotal:totalPrice);}
    public String getFormattedDiscount(){return "Rp "+String.format("%,.0f",discount);}
    public String getWeightFormatted(){if(weight==Math.floor(weight))return (int)weight+" "+(serviceUnit!=null?serviceUnit:"kg");return weight+" "+(serviceUnit!=null?serviceUnit:"kg");}
}
