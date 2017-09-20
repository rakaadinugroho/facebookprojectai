package com.rakaadinugroho.sealmood.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raka Adi Nugroho on 4/18/17.
 *
 * @Github github.com/rakaadinugroho
 * @Contact nugrohoraka@gmail.com
 */

public class FaceAnalysis {
    @SerializedName("faceRectangle")
    @Expose
    private FaceRectangle faceRectangle;
    @SerializedName("scores")
    @Expose
    private Scores scores;

    public FaceRectangle getFaceRectangle() {
        return faceRectangle;
    }

    public void setFaceRectangle(FaceRectangle faceRectangle) {
        this.faceRectangle = faceRectangle;
    }

    public Scores getScores() {
        return scores;
    }

    public void setScores(Scores scores) {
        this.scores = scores;
    }
}
