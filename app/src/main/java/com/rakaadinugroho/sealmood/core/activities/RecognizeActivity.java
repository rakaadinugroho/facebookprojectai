package com.rakaadinugroho.sealmood.core.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rakaadinugroho.sealmood.R;
import com.rakaadinugroho.sealmood.models.Chapter;
import com.rakaadinugroho.sealmood.models.Daily;
import com.rakaadinugroho.sealmood.models.FaceAnalysis;
import com.rakaadinugroho.sealmood.networks.EmotionRestClient;
import com.rakaadinugroho.sealmood.networks.ResponseCallBack;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

public class RecognizeActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CAMERA  = 100;
    private static final String TAG = RecognizeActivity.class.getSimpleName();

    private Realm realm;
    private int dayId;
    private Daily daily;

    private File file;
    private EmotionRestClient emotionRestClient;

    private FloatingActionButton recognize_create;
    private CoordinatorLayout recognize_coordinate;
    private TextView recognize_no_data;
    private ImageView recognize_image;

    private List<Chapter> chapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognize);
        realm   = Realm.getDefaultInstance();
        ActionBar actionBar = getSupportActionBar();
        if (getIntent().getExtras() != null){
            dayId   = getIntent().getExtras().getInt("id");
            daily   = realm.where(Daily.class).equalTo("id", dayId).findFirst();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getIntent().getExtras().getString("category"));
            actionBar.setSubtitle(daily.getDescription());
        }
        checkPerm();
        initViews();


        recognize_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });
        recognize_create.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                recognizeNow();
                return false;
            }
        });
    }

    private void initViews() {
        emotionRestClient   = new EmotionRestClient(this);
        recognize_create    = (FloatingActionButton) findViewById(R.id.recognize_create);
        recognize_coordinate    = (CoordinatorLayout) findViewById(R.id.recognize_coordinate);
        recognize_no_data   = (TextView) findViewById(R.id.recognize_no_data);
        recognize_image     = (ImageView) findViewById(R.id.recognize_image);
        chapters    = new ArrayList<>();
    }

    private void checkPerm() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            //recognize_create.setClickable(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    private void openCamera(){
        Intent intent   = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            file    = getOutputMediaFile();
            if (file != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.rakaadinugroho.sealmood.fileprovider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
                startActivityForResult(intent, PERMISSION_REQUEST_CAMERA);
            } else {
                Toast.makeText(this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Tidak Ada Kamera", Toast.LENGTH_SHORT).show();
        }
    }

    private File getOutputMediaFile() {
        File mediaStorageDir    = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Sealmood");
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }
        String timestamp    = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator+"SEAL_"+timestamp+".jpg");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                recognize_create.setClickable(true);
                recognize_no_data.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void recognizeNow(){
        final ProgressDialog loading  = new ProgressDialog(RecognizeActivity.this);
        loading.setIndeterminate(true);
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);

        loading.setMessage("Analyze Image Running");
        loading.show();

        emotionRestClient.analyze(Uri.fromFile(file), new ResponseCallBack() {
            @Override
            public void onError(String errormsg) {
                loading.dismiss();
                Snackbar.make(recognize_coordinate, errormsg, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(FaceAnalysis[] response) {
                loading.dismiss();
                for (FaceAnalysis faceAnalysis: response){
                    float happy     = faceAnalysis.getScores().getHappiness().floatValue();
                    float anger     = faceAnalysis.getScores().getAnger().floatValue();
                    float sadness   = faceAnalysis.getScores().getSadness().floatValue();
                    float neutral   = faceAnalysis.getScores().getNeutral().floatValue();
                    float suprised  = faceAnalysis.getScores().getSurprise().floatValue();
                    float fear      = faceAnalysis.getScores().getFear().floatValue();
                    float contempt  = faceAnalysis.getScores().getContempt().floatValue();
                    float disgust   = faceAnalysis.getScores().getDisgust().floatValue();

                    Chapter chapter = new Chapter(happy, neutral, anger, contempt, disgust, fear, sadness, suprised, dateCreator());
                    chapters.add(chapter);
                }
                Log.d(TAG, "onSuccess: "+ response[0].getScores().getHappiness());
                createNewChapter();
                finish();
            }
        });
    }

    private long dateCreator(){
        Calendar mCalendar  = Calendar.getInstance();
        final int month     = mCalendar.get(Calendar.MONTH);
        final int year      = mCalendar.get(Calendar.YEAR);
        final int day       = mCalendar.get(Calendar.DAY_OF_MONTH);
        final int hour      = mCalendar.get(Calendar.HOUR_OF_DAY);
        final int minutes   = mCalendar.get(Calendar.MINUTE);
        final int seconds   = mCalendar.get(Calendar.SECOND);

        Date tanggal    = new Date((year-1900), month, day, hour, minutes, seconds);
        return tanggal.getTime();
    }
    private void createNewChapter() {
        realm.beginTransaction();
        realm.copyToRealm(chapters);
        daily.getChapters().addAll(chapters);
        realm.commitTransaction();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_CAMERA){
            if (resultCode == RESULT_OK){
                recognize_image.setImageURI(Uri.fromFile(file));
                recognize_no_data.setVisibility(View.INVISIBLE);
                Snackbar.make(recognize_coordinate, "Tekan Lama Tombol Kamera Untuk Memproses", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
