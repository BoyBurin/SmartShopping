package com.example.seniorproject.smartshopping.model.dao;

import android.content.Context;

import com.example.seniorproject.smartshopping.model.manager.Contextor;

import java.util.ArrayList;
import java.util.StringTokenizer;


public class Group {

    private static Group instance;

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private String name;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}