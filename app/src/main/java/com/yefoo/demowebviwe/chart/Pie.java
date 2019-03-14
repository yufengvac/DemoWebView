package com.yefoo.demowebviwe.chart;

/**
 * Created by yufeng on 2019/3/15.
 */

public class Pie {
    public int pieColor;
    public float pieValue;
    public String pieString;

    public Pie(float pieValue,String pieString,int pieColor){
        this.pieValue = pieValue;
        this.pieColor = pieColor;
        this.pieString = pieString;
    }
}
