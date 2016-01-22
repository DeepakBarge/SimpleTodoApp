package com.example.deepak.simpletodo.utils;

import java.io.Serializable;

public class Item implements Serializable {

    public int id;
    public String label;
    public String notes;
    public String priorityLevel;
    public String dueDate;
    public String status;

    public Item(){}

    public Item(String label, String notes, String priorityLevel, String status){
        this.notes = notes;
        this.label = label;
        this.priorityLevel = priorityLevel;
        this.status = status;
        //this.dueDate = dueDate;
    }
}
