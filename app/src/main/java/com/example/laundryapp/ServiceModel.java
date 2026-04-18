package com.example.laundryapp;
public class ServiceModel {
    private int id, estimateDays;
    private String name, unit, description;
    private double price;
    public ServiceModel(){}
    public ServiceModel(String name,double price,String unit,String description){this.name=name;this.price=price;this.unit=unit;this.description=description;this.estimateDays=2;}
    public int getId(){return id;} public void setId(int v){id=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public double getPrice(){return price;} public void setPrice(double v){price=v;}
    public String getUnit(){return unit;} public void setUnit(String v){unit=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public int getEstimateDays(){return estimateDays;} public void setEstimateDays(int v){estimateDays=v;}
    public String getFormattedPrice(){return "Rp "+String.format("%,.0f",price)+"/"+unit;}
    public String getEstimateText(){return "Estimasi "+estimateDays+" hari";}
}
