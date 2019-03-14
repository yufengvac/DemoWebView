package com.yefoo.demowebviwe;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yefoo.demowebviwe.chart.Pie;
import com.yefoo.demowebviwe.chart.PieChart;

import java.util.ArrayList;
import java.util.List;

public class ThirdActivity extends AppCompatActivity {

    private PieChart pieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        pieChart = findViewById(R.id.pieChart);

        List<Pie> pieList = new ArrayList<>();
        Pie pie = new Pie(90f, "one",  Color.parseColor("#F48FB1"));

        Pie pie2 = new Pie(210f, "two", Color.parseColor("#CE93D8"));

        Pie pie3 = new Pie(10f, "three",  Color.parseColor("#90CAF9"));

        Pie pie4 = new Pie(54f, "four", Color.parseColor("#FFE082"));

        Pie pie5 = new Pie(170f, "five", Color.parseColor("#FFAB91"));

        pieList.add(pie);
        pieList.add(pie5);
        pieList.add(pie2);
        pieList.add(pie3);
        pieList.add(pie4);
        pieChart.setPie(pieList);
        pieChart.startDrawPie();
    }
}
