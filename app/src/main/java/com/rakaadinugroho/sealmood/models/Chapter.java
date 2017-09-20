package com.rakaadinugroho.sealmood.models;

import com.rakaadinugroho.sealmood.BaseApps;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Raka Adi Nugroho on 4/8/17.
 *
 * @Github github.com/rakaadinugroho
 * @Contact nugrohoraka@gmail.com
 */

public class Chapter extends RealmObject {
    @PrimaryKey
    private int id;
    private float happy;
    private float neutral;
    private float anger;
    private float contempt;
    private float disgust;
    private float fear;
    private float sadness;
    private float supprised;

    private long createdAt;
    public Chapter() {
    }

    public Chapter(float happy, float neutral, float anger, float contempt, float disgust, float fear, float sadness, float supprised, long createdAt) {
        this.happy = happy;
        this.neutral = neutral;
        this.anger = anger;
        this.contempt = contempt;
        this.disgust = disgust;
        this.fear = fear;
        this.sadness = sadness;
        this.supprised = supprised;
        this.id = BaseApps.ChapterID.incrementAndGet();
        this.createdAt  = createdAt;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getHappy() {
        return happy;
    }

    public void setHappy(float happy) {
        this.happy = happy;
    }

    public float getNeutral() {
        return neutral;
    }

    public void setNeutral(float neutral) {
        this.neutral = neutral;
    }

    public float getAnger() {
        return anger;
    }

    public void setAnger(float anger) {
        this.anger = anger;
    }

    public float getContempt() {
        return contempt;
    }

    public void setContempt(float contempt) {
        this.contempt = contempt;
    }

    public float getDisgust() {
        return disgust;
    }

    public void setDisgust(float disgust) {
        this.disgust = disgust;
    }

    public float getFear() {
        return fear;
    }

    public void setFear(float fear) {
        this.fear = fear;
    }

    public float getSadness() {
        return sadness;
    }

    public void setSadness(float sadness) {
        this.sadness = sadness;
    }

    public float getSupprised() {
        return supprised;
    }

    public void setSupprised(float supprised) {
        this.supprised = supprised;
    }
}
