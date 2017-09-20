package com.rakaadinugroho.sealmood.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raka Adi Nugroho on 4/18/17.
 *
 * @Github github.com/rakaadinugroho
 * @Contact nugrohoraka@gmail.com
 */

public class Scores {
    @SerializedName("anger")
    @Expose
    private Double anger;
    @SerializedName("contempt")
    @Expose
    private Double contempt;
    @SerializedName("disgust")
    @Expose
    private Double disgust;
    @SerializedName("fear")
    @Expose
    private Double fear;
    @SerializedName("happiness")
    @Expose
    private Double happiness;
    @SerializedName("neutral")
    @Expose
    private Double neutral;
    @SerializedName("sadness")
    @Expose
    private Double sadness;
    @SerializedName("surprise")
    @Expose
    private Double surprise;

    public Double getAnger() {
        return anger;
    }

    public void setAnger(Double anger) {
        this.anger = anger;
    }

    public Double getContempt() {
        return contempt;
    }

    public void setContempt(Double contempt) {
        this.contempt = contempt;
    }

    public Double getDisgust() {
        return disgust;
    }

    public void setDisgust(Double disgust) {
        this.disgust = disgust;
    }

    public Double getFear() {
        return fear;
    }

    public void setFear(Double fear) {
        this.fear = fear;
    }

    public Double getHappiness() {
        return happiness;
    }

    public void setHappiness(Double happiness) {
        this.happiness = happiness;
    }

    public Double getNeutral() {
        return neutral;
    }

    public void setNeutral(Double neutral) {
        this.neutral = neutral;
    }

    public Double getSadness() {
        return sadness;
    }

    public void setSadness(Double sadness) {
        this.sadness = sadness;
    }

    public Double getSurprise() {
        return surprise;
    }

    public void setSurprise(Double surprise) {
        this.surprise = surprise;
    }
}
