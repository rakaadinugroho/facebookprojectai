package com.rakaadinugroho.sealmood.core.activities.dashboard;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rakaadinugroho.sealmood.R;
import com.rakaadinugroho.sealmood.core.adapters.CalendarAdapter;
import com.rakaadinugroho.sealmood.models.Chapter;
import com.rakaadinugroho.sealmood.models.Mood;
import com.rakaadinugroho.sealmood.utils.CalendarUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class Calender extends Fragment implements View.OnClickListener {
    private static final String TAG = Calender.class.getSimpleName();
    RecyclerView mCalendarRecycler;
    TextView currentMonth;
    TextView btnBefore;
    TextView btnNext;

    TextView calender_happy;
    TextView calender_neutral;
    TextView calender_angry;
    TextView calender_contempt;
    TextView calender_suprised;
    TextView calender_sad;
    TextView calender_disgust;
    TextView calender_fear;


    ArrayList<Mood> moods;
    DisplayMetrics metrics;




    int month;
    int year;
    private RealmResults<Chapter> chapters;

    private Realm realm;

    public Calender() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view   = inflater.inflate(R.layout.fragment_calender, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View mView) {
        realm   = Realm.getDefaultInstance();
        mCalendarRecycler   = (RecyclerView) mView.findViewById(R.id.calendarGridView);
        currentMonth    = (TextView) mView.findViewById(R.id.currentMonth);
        btnBefore   = (TextView) mView.findViewById(R.id.btnBefore);
        btnNext = (TextView) mView.findViewById(R.id.btnNext);
        calender_angry  = (TextView) mView.findViewById(R.id.calender_angry);
        calender_happy  = (TextView) mView.findViewById(R.id.calender_happy);
        calender_disgust    = (TextView) mView.findViewById(R.id.calender_disgust);
        calender_fear   = (TextView) mView.findViewById(R.id.calender_fear);
        calender_neutral    = (TextView) mView.findViewById(R.id.calender_neutral);
        calender_sad    = (TextView) mView.findViewById(R.id.calender_sad);
        calender_contempt   = (TextView) mView.findViewById(R.id.calender_contempt);
        calender_suprised   = (TextView) mView.findViewById(R.id.calender_suprised);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set Calendar
        setCalendar();
        // Setup Data
        setUpData();
        // Click Navigation
        btnNext.setOnClickListener(this);
        btnBefore.setOnClickListener(this);
    }

    private float theBigest(Chapter chapter){
        List<Float> listChapter = new ArrayList<>();
        listChapter.add(chapter.getAnger());
        listChapter.add(chapter.getContempt());
        listChapter.add(chapter.getDisgust());
        listChapter.add(chapter.getFear());
        listChapter.add(chapter.getHappy());
        listChapter.add(chapter.getContempt());
        listChapter.add(chapter.getNeutral());
        listChapter.add(chapter.getSupprised());

        Collections.sort(listChapter);

        return listChapter.get(listChapter.size()-1);
    }
    private void setUpData() {
        long start  = new Date((year-1900), month, 1).getTime();
        long end    = new Date((year-1900), month, 30, 23, 59, 59).getTime();
        chapters    = realm.where(Chapter.class).between("createdAt", start, end).findAllSorted("createdAt", Sort.DESCENDING);
        if (chapters.size() > 0) {
            Log.d(TAG, "setUpData: dimualai : "+ start);
            Log.d(TAG, "setUpData: diakhiri : "+ end);
            int happy = 0;
            int sad = 0;
            int neutral = 0;
            int disgust = 0;
            int fear = 0;
            int angry = 0;
            int contempt = 0;
            int suprised = 0;

            for (Chapter chapter: chapters){
                float biggest   = theBigest(chapter);
                if (biggest == chapter.getHappy())
                    happy +=1;
                else if (biggest == chapter.getNeutral())
                    neutral +=1;
                else if (biggest == chapter.getAnger())
                    angry +=1;
                else if (biggest == chapter.getContempt())
                    contempt +=1;
                else if (biggest == chapter.getDisgust())
                    disgust +=1;
                else if (biggest == chapter.getFear())
                    fear +=1;
                else if (biggest == chapter.getSadness())
                    sad +=1;
                else if (biggest == chapter.getSupprised())
                    suprised +=1;
            }
            setUpDataMonth(happy, sad, neutral, disgust, fear, angry, contempt, suprised);
        }else{
            int happy = 0;
            int sad = 0;
            int neutral = 0;
            int disgust = 0;
            int fear = 0;
            int angry = 0;
            int contempt = 0;
            int suprised = 0;
            setUpDataMonth(happy, sad, neutral, disgust, fear, angry, contempt, suprised);
            Toast.makeText(getActivity(), "Tidak ada data Bulan ini", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpDataMonth(int happy, int sad, int neutral, int disgust, int fear, int angry, int contempt, int suprised) {
        calender_happy.setText(""+happy);
        calender_sad.setText(""+sad);
        calender_neutral.setText(""+neutral);
        calender_disgust.setText(""+disgust);
        calender_fear.setText(""+fear);
        calender_angry.setText(""+angry);
        calender_contempt.setText(""+contempt);
        calender_suprised.setText(""+suprised);
    }

    private void showToDate(long tanggal){
        Date date   = new Date(tanggal);
        SimpleDateFormat simpleDateFormat   = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.d(TAG, "setUpData: " +simpleDateFormat.format(date));
    }
    private void setCalendar() {
        Calendar mCalendar  = Calendar.getInstance();
        month   = mCalendar.get(Calendar.MONTH);
        year    = mCalendar.get(Calendar.YEAR);

        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        mCalendarRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 7));
        mCalendarRecycler.setHasFixedSize(false);

        currentMonth.setText(CalendarUtil.getMonth(month + 1) + "/" + String.valueOf(year));

        populateMoods(mCalendar);

    }

    private void populateMoods(Calendar mCalendar) {
        this.moods  = new ArrayList<>();
        /*
        Mood mood   = new Mood();
        mood.day   = 3;
        mood.month  = mCalendar.get(Calendar.MONTH) + 1;
        mood.year   = mCalendar.get(Calendar.YEAR);
        this.moods.add(mood);

        Mood mood1  = new Mood();
        mood1.day    = 4;
        mood1.month  = mCalendar.get(Calendar.MONTH) + 1;
        mood1.year   = mCalendar.get(Calendar.YEAR);
        this.moods.add(mood1);

        Mood mood2  = new Mood();
        mood2.day   = 5;
        mood2.month  = mCalendar.get(Calendar.MONTH) + 1;
        mood2.year   = mCalendar.get(Calendar.YEAR);
        this.moods.add(mood2);
        */
        mCalendarRecycler.setAdapter(new CalendarAdapter(getActivity(), month, year, metrics, this.moods));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBefore:{
                onClickBefore();
                break;
            }
            case R.id.btnNext: {
                onClickNext();
                break;
            }
        }
    }

    private void onClickBefore() {
        month   = month - 1;
        if (month < 0 ){
            // Last Year
            month   = 11;
            year    = year-1;
        }

        currentMonth.setText(CalendarUtil.getMonth(month + 1)+ "/" +String.valueOf(year));
        mCalendarRecycler.setAdapter(new CalendarAdapter(getActivity(), month, year, metrics, this.moods));

        setUpData();
    }

    private void onClickNext() {
        month   = month + 1;
        if (month > 11 ){
            // Next Year
            month   = 0;
            year    = year+1;
        }

        currentMonth.setText(CalendarUtil.getMonth(month + 1)+ "/" +String.valueOf(year));
        mCalendarRecycler.setAdapter(new CalendarAdapter(getActivity(), month, year, metrics, this.moods));
        setUpData();
    }
}
