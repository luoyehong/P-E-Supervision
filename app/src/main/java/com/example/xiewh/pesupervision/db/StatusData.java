package com.example.xiewh.pesupervision.db;

import org.litepal.crud.DataSupport;

/**
 * Created by XiEwH on 2017/7/29.
 */

public class StatusData extends DataSupport {

    private String number;

    private int value; //值

    public int getValue() {

        return value;
    }

    public String getnumber() {
        return number;
    }

    public void setnumber(String number) {
        this.number = number;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
