package com.rakaadinugroho.sealmood.networks;

import com.rakaadinugroho.sealmood.models.FaceAnalysis;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Raka Adi Nugroho on 4/18/17.
 *
 * @Github github.com/rakaadinugroho
 * @Contact nugrohoraka@gmail.com
 */

public interface ApiService {
    @Headers("Content-Type: application/octet-stream")
    @POST("recognize")
    Call<FaceAnalysis[]> analyzeImage(
            @Header("Ocp-Apim-Subscription-Key") String subscriptionKey,
            @Body RequestBody image
    );

    /**
     * New for Lamp
     */
    @POST("?anger")
    Call<ResponseBody> sendAnger();
    @POST("?happy")
    Call<ResponseBody> sendHappy();
    @POST("?neutral")
    Call<ResponseBody> sendNeutral();
    @POST("?sad")
    Call<ResponseBody> sendSad();
    @POST("?contempt")
    Call<ResponseBody> sendContempt();
    @POST("?fear")
    Call<ResponseBody> sendFear();
    @POST("?disgust")
    Call<ResponseBody> sendDisgust();
    @POST("?suprised")
    Call<ResponseBody> sendSuprised();
}
