package com.rakaadinugroho.sealmood.core.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.rakaadinugroho.sealmood.R;
import com.rakaadinugroho.sealmood.core.adapters.DayAdapter;
import com.rakaadinugroho.sealmood.models.Category;
import com.rakaadinugroho.sealmood.models.Chapter;
import com.rakaadinugroho.sealmood.models.Daily;
import com.rakaadinugroho.sealmood.networks.ApiService;
import com.rakaadinugroho.sealmood.utils.CalendarUtil;
import com.rakaadinugroho.sealmood.utils.RecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DayActivity extends AppCompatActivity implements RealmChangeListener<RealmModel> {
    private static final String TAG = DayActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView date_label;
    private TextView day_no_data;
    private FloatingActionButton fab_day_add;
    private TextView day_description_note;
    private RadarChart day_chart;
    private RecyclerView day_recycler;

    private Category category;
    private RealmResults<Daily> dailies;
    private int totalData;

    private RealmResults<Chapter> chapters;
    private List<Chapter> chapterList;
    private Daily daily;
    private int dailyId;
    private int totalChapter;

    private DayAdapter adapter;
    private LinearLayoutManager layoutManager;

    private int categoryId;
    private Realm realm;


    private int day;
    private int month;
    private int year;

    private Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        realm   = Realm.getDefaultInstance();
        initViews();
        setUpData();
        setUpChapter();

        fab_day_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalData > 0) {
                    Intent intent   = new Intent(DayActivity.this, RecognizeActivity.class);
                    intent.putExtra("id", dailyId);
                    intent.putExtra("category", category.getTitle().toString());
                    startActivity(intent);
                }else {
                    showAlertForCreatingDaily("Create", "Masukan keterangan terlebih dahulu untuk hari ini");
                }
            }
        });
    }

    private void recordMood() {
        createNewChapter();
    }

    private void createNewChapter() {
        /*
        realm.beginTransaction();
        Chapter chapter = new Chapter(0.9f, 0.5f, 0.1f, 0.1f, 0.2f, 0.01f, 0.01f, 0.2f);
        realm.copyToRealm(chapter);
        daily.getChapters().add(chapter);
        realm.commitTransaction();
        */
    }

    private void setUpChapter() {
        /* Jika Ada Data Dihari x */
        if (totalData > 0){
            daily       = realm.where(Daily.class).equalTo("id", dailyId).findFirst();
            //daily.addChangeListener(this);
            chapters        = daily.getChapters().where().findAllSorted("createdAt", Sort.DESCENDING);
            //totalChapter    = chapters.size();

            /* to Data List */
            chapterList = realm.copyFromRealm(chapters);
            totalChapter    = chapterList.size();

            /*
            Setup DayChart
            */


            /*
            Setup List Chapters
             */
            adapter = new DayAdapter(getApplicationContext(), chapterList, R.layout.item_day);
            layoutManager = new LinearLayoutManager(getApplicationContext());
            day_recycler.setLayoutManager(layoutManager);
            day_recycler.setAdapter(adapter);


            adapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener() {
                @Override
                public void onItemClick(int position, View view) {
                    /* Request to Send Values Lamp*/
                    try {
                        theBiggestLabel(chapterList.get(position));
                    }catch (Exception e){
                        Toast.makeText(DayActivity.this, "Cannot Connect to LampBoard Joskoding", Toast.LENGTH_SHORT).show();
                    }
                    showDetail(position);
                }
            });
        }else{
            try {
                chapterList.clear();
                adapter.notifyDataSetChanged();
            }catch (Exception e){
                Log.d(TAG, "setUpChapter: tidak ada data sebelumnya");
            }
            totalChapter    = 0;
        }
        setUpDataChapter();
    }
    /*
    * Lamp New
    * */
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
    private void theBiggestLabel(Chapter chapter){
        float biggest   = theBigest(chapter);
        String url_board    = "http://192.168.1.177/";
        Retrofit retrofit   = new Retrofit.Builder()
                .baseUrl(url_board)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService   = retrofit.create(ApiService.class);
        Call<ResponseBody> result = null;
        if (biggest == chapter.getHappy())
            result  = apiService.sendHappy();
        else if (biggest == chapter.getNeutral())
            result  = apiService.sendNeutral();
        else if (biggest == chapter.getAnger())
            result  = apiService.sendAnger();
        else if (biggest == chapter.getContempt())
            result  = apiService.sendContempt();
        else if (biggest == chapter.getDisgust())
            result  = apiService.sendDisgust();
        else if (biggest == chapter.getFear())
            result  = apiService.sendFear();
        else if (biggest == chapter.getSadness())
            result  = apiService.sendSad();
        else if (biggest == chapter.getSupprised())
            result  = apiService.sendSuprised();

        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: Sucess");
                Toast.makeText(DayActivity.this, "Lamp On", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: Gagal");
                Toast.makeText(DayActivity.this, "Lamp Off", Toast.LENGTH_SHORT).show();
            }
        });

    }
    /*
    End Lamp New
     */
    private void setUpDayChart() {
        if (totalChapter > 0) {
            day_chart.setVisibility(View.VISIBLE);
            try {
                List<IRadarDataSet> dataSets = new ArrayList<>();
                for (int i = 0; i < chapters.size(); i++) {
                    List<RadarEntry> entries = new ArrayList<>();
                    Chapter chapter = chapters.get(i);

                    entries.add(new RadarEntry(chapter.getAnger(), "Marah"));
                    entries.add(new RadarEntry(chapter.getContempt(), "Muak"));
                    entries.add(new RadarEntry(chapter.getDisgust(), "Jijik"));
                    entries.add(new RadarEntry(chapter.getFear(), "Takut"));
                    entries.add(new RadarEntry(chapter.getHappy(), "Bahagia"));
                    entries.add(new RadarEntry(chapter.getNeutral(), "Netral"));
                    entries.add(new RadarEntry(chapter.getSadness(), "Sedih"));
                    entries.add(new RadarEntry(chapter.getSupprised(), "Terkejut"));

                    RadarDataSet dataSet = new RadarDataSet(entries, ""+(i + 1));

                    // Random
                    Random random = new Random();
                    dataSet.setColor(Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256)));
                    dataSet.setDrawFilled(true);
                    dataSets.add(dataSet);
                }

                final List<String> labels = new ArrayList<>();
                labels.add("Marah");
                labels.add("Muak");
                labels.add("Jijik");
                labels.add("Takut");
                labels.add("Bahagia");
                labels.add("Netral");
                labels.add("Sedih");
                labels.add("Terkejut");

                XAxis xAxis = day_chart.getXAxis();
                xAxis.setXOffset(0f);
                xAxis.setYOffset(0f);
                xAxis.setTextSize(8f);
                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return labels.get((int) value % labels.size());
                    }
                });
                day_chart.setDescription(null);
                RadarData data = new RadarData(dataSets);
                day_chart.setData(data);
                day_chart.invalidate();
            } catch (Exception e) {

            }
        }else{
            day_chart.setDescription(null);
            day_chart.clear();
            day_chart.invalidate();
            day_chart.setVisibility(View.INVISIBLE);
        }
    }

    private void setUpDataChapter() {
        setUpDayChart();
        if (totalChapter > 0){
            day_no_data.setVisibility(View.INVISIBLE);
        }else{
            day_no_data.setVisibility(View.VISIBLE);
        }
    }

    private void showAlertForCreatingDaily(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (title != null) builder.setTitle(title);
        if (message != null) builder.setMessage(message);

        View viewInflated   = LayoutInflater.from(this).inflate(R.layout.dialog_create, null);
        builder.setView(viewInflated);

        final EditText input    = (EditText) viewInflated.findViewById(R.id.edit_title);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String noteDaily    = input.getText().toString().trim();
                if (noteDaily.length() > 0){
                    createNewDaily(noteDaily);
                }else{
                    Toast.makeText(DayActivity.this, "Masukan data secara spesifik", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.create().show();
    }

    private void createNewDaily(String noteDaily) {
        realm.beginTransaction();
        Date date   = new Date(year, month, day);
        Daily daily = new Daily(noteDaily, date);
        realm.copyToRealm(daily);
        category.getDailies().add(daily);
        realm.commitTransaction();

        setUpData();
    }

    private void setUpData() {
        Date date   = new Date(year, month, day);
        dailies     = category.getDailies().where().equalTo("createdAt", date).findAll();
        totalData   = dailies.size();
        if (totalData > 0){
            day_description_note.setText(dailies.get(0).getDescription().toString());
            dailyId = dailies.get(0).getId();
        }else{
            day_description_note.setText("-");
        }
    }

    private void initViews() {
        /*
        Component InitialViews
         */
        toolbar = (Toolbar) findViewById(R.id.toolbar_day);
        date_label  = (TextView) findViewById(R.id.day_label_date);
        day_no_data = (TextView) findViewById(R.id.day_no_data);
        fab_day_add = (FloatingActionButton) findViewById(R.id.fab_day_add);
        day_description_note    = (TextView) findViewById(R.id.day_description_note);
        day_chart   = (RadarChart) findViewById(R.id.day_chart);
        day_recycler    = (RecyclerView) findViewById(R.id.day_recycler);
        chapterList = new ArrayList<>();
        /*
        Toolbar Initial
         */
        if (toolbar != null)
            setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().getExtras() != null)
            categoryId  = getIntent().getExtras().getInt("id");
        /*
        Data Initial
         */

        category    = realm.where(Category.class).equalTo("id", categoryId).findFirst();
        this.setTitle(category.getTitle());
        calendar    = Calendar.getInstance();
        setUpCalendar();
    }

    private void showDetail(int position){
        Chapter chapter = chapters.get(position);

        final AlertDialog.Builder builder   = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view   = inflater.inflate(R.layout.chapter_dialog, null);
        builder.setView(view);

        final TextView dialog_happy   = (TextView) view.findViewById(R.id.dialog_happy);
        final TextView dialog_fear   = (TextView) view.findViewById(R.id.dialog_fear);
        final TextView dialog_sad   = (TextView) view.findViewById(R.id.dialog_sad);
        final TextView dialog_neutral   = (TextView) view.findViewById(R.id.dialog_neutral);
        final TextView dialog_disgust   = (TextView) view.findViewById(R.id.dialog_disgust);
        final TextView dialog_anger   = (TextView) view.findViewById(R.id.dialog_anger);
        final TextView dialog_contempt   = (TextView) view.findViewById(R.id.dialog_contempt);
        final TextView dialog_suprised   = (TextView) view.findViewById(R.id.dialog_suprised);

        dialog_anger.setText(chapter.getAnger()+"/1");
        dialog_happy.setText(chapter.getHappy()+"/1");
        dialog_fear.setText(chapter.getFear()+"/1");
        dialog_sad.setText(chapter.getSadness()+"/1");
        dialog_neutral.setText(chapter.getNeutral()+"/1");
        dialog_disgust.setText(chapter.getDisgust()+"/1");
        dialog_contempt.setText(chapter.getContempt()+"/1");
        dialog_suprised.setText(chapter.getSupprised()+"/1");

        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Detail Mood");
        alertDialog.show();
    }
    private void setUpCalendar() {
        day     = calendar.get(Calendar.DATE);
        month   = calendar.get(Calendar.MONTH);
        year    = calendar.get(Calendar.YEAR);
    }

    public void onDayNext(View view){
        day = day + 1;
        if (day > 30){
            day = 1;
            month   = month + 1;
        }
        setDateLabel();
        setUpData();
        setUpChapter();
    }
    public void onDayPrev(View view){
        day = day - 1;
        if (day < 0){
            day = 30;
            month   = month -1 ;
        }
        setDateLabel();
        setUpData();
        setUpChapter();
    }
    private void setDateLabel() {
        setUpData();
        if (isToday(day, month, year))
            date_label.setText("Hari Ini");
        else
            date_label.setText(day + " " + CalendarUtil.getMonth(month + 1) + " " + year);
    }
    private boolean isToday(int day, int month, int year) {
        return calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.DAY_OF_MONTH) == day;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChange(RealmModel element) {
        try {
            adapter.notifyDataSetChanged();
        }catch (Exception e){
            Toast.makeText(this, "Error : "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpData();
        setUpChapter();
    }
}
