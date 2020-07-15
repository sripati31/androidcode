
package com.divinetechs.gotstart.Model.TvsVideoDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("tvv_id")
    @Expose
    private String tvvId;
    @SerializedName("tvv_name")
    @Expose
    private String tvvName;
    @SerializedName("tvv_thumbnail")
    @Expose
    private String tvvThumbnail;
    @SerializedName("tvv_video")
    @Expose
    private String tvvVideo;
    @SerializedName("tvv_description")
    @Expose
    private String tvvDescription;
    @SerializedName("tvv_view")
    @Expose
    private String tvvView;
    @SerializedName("tvv_download")
    @Expose
    private String tvvDownload;
    @SerializedName("tvv_date")
    @Expose
    private String tvvDate;
    @SerializedName("ftvs_id")
    @Expose
    private String ftvsId;

    public String getTvvId() {
        return tvvId;
    }

    public void setTvvId(String tvvId) {
        this.tvvId = tvvId;
    }

    public String getTvvName() {
        return tvvName;
    }

    public void setTvvName(String tvvName) {
        this.tvvName = tvvName;
    }

    public String getTvvThumbnail() {
        return tvvThumbnail;
    }

    public void setTvvThumbnail(String tvvThumbnail) {
        this.tvvThumbnail = tvvThumbnail;
    }

    public String getTvvVideo() {
        return tvvVideo;
    }

    public void setTvvVideo(String tvvVideo) {
        this.tvvVideo = tvvVideo;
    }

    public String getTvvDescription() {
        return tvvDescription;
    }

    public void setTvvDescription(String tvvDescription) {
        this.tvvDescription = tvvDescription;
    }

    public String getTvvView() {
        return tvvView;
    }

    public void setTvvView(String tvvView) {
        this.tvvView = tvvView;
    }

    public String getTvvDownload() {
        return tvvDownload;
    }

    public void setTvvDownload(String tvvDownload) {
        this.tvvDownload = tvvDownload;
    }

    public String getTvvDate() {
        return tvvDate;
    }

    public void setTvvDate(String tvvDate) {
        this.tvvDate = tvvDate;
    }

    public String getFtvsId() {
        return ftvsId;
    }

    public void setFtvsId(String ftvsId) {
        this.ftvsId = ftvsId;
    }

}
