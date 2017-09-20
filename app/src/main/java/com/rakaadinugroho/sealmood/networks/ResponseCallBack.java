package com.rakaadinugroho.sealmood.networks;

import com.rakaadinugroho.sealmood.models.FaceAnalysis;

/**
 * Created by Raka Adi Nugroho on 4/18/17.
 *
 * @Github github.com/rakaadinugroho
 * @Contact nugrohoraka@gmail.com
 */

public interface ResponseCallBack {
    void onError(String errormsg);
    void onSuccess(FaceAnalysis[] response);
}
