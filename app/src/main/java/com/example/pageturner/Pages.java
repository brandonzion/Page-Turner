package com.example.pageturner;

public class Pages {
    String name;
    public Pages(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    @Override
    public String toString(){
        return this.name;
    }
}
