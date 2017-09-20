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

public class Category extends RealmObject {
    @PrimaryKey
    private int id;
    @Required
    private String title;
    @Required
    private Date createdAt;

    /*
    One to Many Category and Daily
     */
    private RealmList<Daily> dailies;

    public Category() {
    }

    public Category(String title) {
        this.id = BaseApps.CategoryID.incrementAndGet();
        this.title = title;
        this.dailies = new RealmList<Daily>();
        this.createdAt  = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public RealmList<Daily> getDailies() {
        return dailies;
    }

    public void setDailies(RealmList<Daily> dailies) {
        this.dailies = dailies;
    }
}
