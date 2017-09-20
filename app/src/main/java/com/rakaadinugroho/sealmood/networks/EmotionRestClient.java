package com.rakaadinugroho.sealmood.networks;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.rakaadinugroho.sealmood.base.Const;
import com.rakaadinugroho.sealmood.models.FaceAnalysis;
import com.rakaadinugroho.sealmood.utils.FileUtils;

import java.io.IOException;
import java.net.URISyntaxException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * Created by Raka Adi Nugroho on 4/18/17.
 *
 * @Github github.com/rakaadinugroho
 * @Contact nugrohoraka@gmail.com
 */

public class EmotionRestClient {
    private ApiService apiService;
    private Context context;

    public EmotionRestClient(Context context) {
        this.context = context;

        Retrofit retrofit   = new Retrofit.Builder()
                .baseUrl(Const.API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService  = retrofit.create(ApiService.class);
    }

    public Response<FaceAnalysis[]> analyze(Uri fileuri) throws IOException, URISyntaxException {
        byte[] data = FileUtils.toBinary(fileuri);
        return analyze(data);
    }
    public void analyze(Uri fileuri, final ResponseCallBack callBack){
        byte[] data;
        try {
            data    = FileUtils.toBinary(fileuri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        analyze(data, callBack);
    }
    public void analyze(byte[] data, final ResponseCallBack callBack){
        getOctetStream(data).enqueue(new Callback<FaceAnalysis[]>() {
            @Override
            public void onResponse(Call<FaceAnalysis[]> call, Response<FaceAnalysis[]> response) {
                if (response.isSuccessful()){
                    FaceAnalysis[] result   = response.body();
                    callBack.onSuccess(result);
                }else{
                    Log.d(TAG, "onResponse: "+ response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<FaceAnalysis[]> call, Throwable t) {
                callBack.onError(t.toString());
            }
        });
    }
    public Response<FaceAnalysis[]> analyze(byte[] data) throws IOException {
        return getOctetStream(data).execute();
    }
    private Call<FaceAnalysis[]> getOctetStream(byte[] data){
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), data);
        Call<FaceAnalysis[]> call   = apiService.analyzeImage(Const.API_KEY, requestBody);
        return call;
    }
}
