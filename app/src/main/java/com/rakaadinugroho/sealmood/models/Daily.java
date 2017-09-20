package com.rakaadinugroho.sealmood.models;

import com.rakaadinugroho.sealmood.BaseApps;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Raka Adi Nugroho on 4/7/17.
 *
 * @Github github.com/rakaadinugroho
 * @Contact nugrohoraka@gmail.com
 */

public class Daily extends RealmObject {
    @PrimaryKey
    private int id;

    @Required
    private String description;
    @Required
    private Date createdAt;
    private float dominant;

    private RealmList<Chapter> chapters;

    public Daily() {
    }

    public Daily(String description, Date date) {
        this.description = description;
        this.id = BaseApps.DailyID.incrementAndGet();
        this.createdAt  = date;
        this.chapters   = new RealmList<Chapter>();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public float getDominant() {
        return dominant;
    }

    public void setDominant(float dominant) {
        this.dominant = dominant;
    }

    public RealmList<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(RealmList<Chapter> chapters) {
        this.chapters = chapters;
    }
}
