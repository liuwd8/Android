package com.example.liuwd8.healthfood;

import java.io.Serializable;

public class Collection implements Serializable {
    String foodName;
    String foodType;
    String nutrients;
    String backgroundColor;

    protected Collection(String _foodName, String _foodType, String _nutrients, String _backgroundColor) {
        foodName = _foodName;
        foodType = _foodType;
        nutrients = _nutrients;
        backgroundColor = _backgroundColor;
    }

    public void setFoodName(String _foodName) {
        this.foodName = _foodName;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public void setNutrients(String nutrients) {
        this.nutrients = nutrients;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getFoodType() {
        return foodType;
    }

    public String getNutrients() {
        return nutrients;
    }
}
