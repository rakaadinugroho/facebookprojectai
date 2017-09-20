package com.rakaadinugroho.sealmood.core.activities.dashboard;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.rakaadinugroho.sealmood.R;
import com.rakaadinugroho.sealmood.models.Chapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class Stats extends Fragment implements OnChartGestureListener, OnChartValueSelectedListener {
    LineChart stat_line_chart;
    private Realm realm;
    private RealmResults<Chapter> chapters;
    private int totalData;
    public Stats() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view   = inflater.inflate(R.layout.fragment_stats, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        realm   = Realm.getDefaultInstance();
        stat_line_chart = (LineChart) view.findViewById(R.id.stat_line_chart);

        chapters    = realm.where(Chapter.class).findAll();
        if (chapters.size() > 0){
            totalData = chapters.size();
        }else{
            totalData = 0;
        }
    }

    /*
    Action
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        stat_line_chart.setOnChartGestureListener(this);
        stat_line_chart.setOnChartValueSelectedListener(this);
        if (totalData > 0){
            setUpData();
        }

    }

    private void setUpData() {
        List<Entry> entriesHappy    = new ArrayList<Entry>();
        List<Entry> entriesNeutral  = new ArrayList<Entry>();
        List<Entry> entriesSad  = new ArrayList<Entry>();
        List<Entry> entriesFear  = new ArrayList<Entry>();
        List<Entry> entriesSuprised  = new ArrayList<Entry>();
        List<Entry> entriesAngry  = new ArrayList<Entry>();
        List<Entry> entriesContempt  = new ArrayList<Entry>();
        List<Entry> entriesDisgust  = new ArrayList<Entry>();

        List<ILineDataSet> dataSets = new ArrayList<>();
        int totalChapter    = 0;
        for (Chapter chapter: chapters){
            entriesHappy.add(new Entry(totalChapter, chapter.getHappy()));
            entriesNeutral.add(new Entry(totalChapter, chapter.getNeutral()));
            entriesSad.add(new Entry(totalChapter, chapter.getSadness()));
            entriesFear.add(new Entry(totalChapter, chapter.getFear()));
            entriesSuprised.add(new Entry(totalChapter, chapter.getSupprised()));
            entriesAngry.add(new Entry(totalChapter, chapter.getAnger()));
            entriesContempt.add(new Entry(totalChapter, chapter.getContempt()));
            entriesDisgust.add(new Entry(totalChapter, chapter.getDisgust()));
            totalChapter++;
        }
        LineDataSet dataSetHappy = new LineDataSet(entriesHappy, "Hap");
        dataSetHappy.setColor(Color.parseColor("#34BC99"));

        LineDataSet dataSetNeutral = new LineDataSet(entriesNeutral, "Neu");
        dataSetNeutral.setColor(Color.parseColor("#808C8D"));

        LineDataSet dataSetSad = new LineDataSet(entriesSad, "Sad");
        dataSetSad.setColor(Color.parseColor("#317FBE"));

        LineDataSet dataSetFear = new LineDataSet(entriesFear, "Fear");
        dataSetFear.setColor(Color.parseColor("#CF5600"));

        LineDataSet dataSetSuprised = new LineDataSet(entriesSuprised, "Sup");
        dataSetSuprised.setColor(Color.parseColor("#354860"));

        LineDataSet dataSetAngry = new LineDataSet(entriesAngry, "Ang");
        dataSetAngry.setColor(Color.parseColor("#BD3D28"));

        LineDataSet dataSetContempt = new LineDataSet(entriesContempt, "Cont");
        dataSetContempt.setColor(Color.parseColor("#3B6987"));

        LineDataSet dataSetDisgust = new LineDataSet(entriesDisgust, "Dis");
        dataSetDisgust.setColor(Color.parseColor("#8B41B3"));

        dataSets.add(dataSetHappy);
        dataSets.add(dataSetNeutral);
        dataSets.add(dataSetSad);
        dataSets.add(dataSetAngry);
        dataSets.add(dataSetContempt);
        dataSets.add(dataSetDisgust);
        dataSets.add(dataSetFear);
        dataSets.add(dataSetSuprised);
        LineData lineData   = new LineData(dataSets);
        stat_line_chart.setData(lineData);
        stat_line_chart.invalidate();
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
