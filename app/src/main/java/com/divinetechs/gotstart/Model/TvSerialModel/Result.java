
package com.divinetechs.gotstart.Model.TvSerialModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("tvs_id")
    @Expose
    private String tvsId;
    @SerializedName("tvs_name")
    @Expose
    private String tvsName;
    @SerializedName("tvs_image")
    @Expose
    private String tvsImage;
    @SerializedName("tvs_view")
    @Expose
    private String tvsView;
    @SerializedName("tvs_date")
    @Expose
    private String tvsDate;
    @SerializedName("fc_id")
    @Expose
    private String fcId;

    public String getTvsId() {
        return tvsId;
    }

    public void setTvsId(String tvsId) {
        this.tvsId = tvsId;
    }

    public String getTvsName() {
        return tvsName;
    }

    public void setTvsName(String tvsName) {
        this.tvsName = tvsName;
    }

    public String getTvsImage() {
        return tvsImage;
    }

    public void setTvsImage(String tvsImage) {
        this.tvsImage = tvsImage;
    }

    public String getTvsView() {
        return tvsView;
    }

    public void setTvsView(String tvsView) {
        this.tvsView = tvsView;
    }

    public String getTvsDate() {
        return tvsDate;
    }

    public void setTvsDate(String tvsDate) {
        this.tvsDate = tvsDate;
    }

    public String getFcId() {
        return fcId;
    }

    public void setFcId(String fcId) {
        this.fcId = fcId;
    }

}
