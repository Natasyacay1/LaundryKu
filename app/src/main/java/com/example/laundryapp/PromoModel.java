package com.example.laundryapp;
public class PromoModel {
    private int id; private String code, description, type, expiryDate;
    private double value, minOrder; private boolean active;
    public int getId(){return id;} public void setId(int v){id=v;}
    public String getCode(){return code;} public void setCode(String v){code=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public String getType(){return type;} public void setType(String v){type=v;}
    public double getValue(){return value;} public void setValue(double v){value=v;}
    public double getMinOrder(){return minOrder;} public void setMinOrder(double v){minOrder=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
    public String getExpiryDate(){return expiryDate;} public void setExpiryDate(String v){expiryDate=v;}
    public double calculateDiscount(double total){
        if(total<minOrder)return 0;
        if("percent".equals(type)) return total*value/100;
        return value;
    }
    public String getDisplayValue(){ return "percent".equals(type) ? (int)value+"%" : "Rp "+String.format("%,.0f",value); }
}
