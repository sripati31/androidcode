
package com.divinetechs.gotstart.Model.TvVideoModel;

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
    @SerializedName("tvv_video_type")
    @Expose
    private String tvvVideoType;
    @SerializedName("tvv_video_url")
    @Expose
    private String tvvVideourl;
    @SerializedName("is_premium")
    @Expose
    private String ispremium;
    @SerializedName("tvv_description")
    @Expose
    private String tvvdescription;
    @SerializedName("tvv_view")
    @Expose
    private String tvvView;
    @SerializedName("tvv_download")
    @Expose
    private String tvvDownload;
    @SerializedName("v_type")
    @Expose
    private String v_type;
    @SerializedName("tvv_date")
    @Expose
    private String tvvDate;
    @SerializedName("ftvs_id")
    @Expose
    private String ftvsId;
    @SerializedName("tvs_id")
    @Expose
    private String tvsId;
    @SerializedName("tvs_name")
    @Expose
    private String tvsName;
    @SerializedName("tvs_image")
    @Expose
    private String tvsImage;
    @SerializedName("tvs_date")
    @Expose
    private String tvsDate;
    @SerializedName("fc_id")
    @Expose
    private String fc_id;
    @SerializedName("c_id")
    @Expose
    private String c_id;
    @SerializedName("cat_name")
    @Expose
    private String cat_name;
    @SerializedName("cat_image")
    @Expose
    private String cat_image;
    @SerializedName("c_date")
    @Expose
    private String c_date;

    public String getIspremium() {
        return ispremium;
    }

    public void setIspremium(String ispremium) {
        this.ispremium = ispremium;
    }

    public String getTvvVideoType() {
        return tvvVideoType;
    }

    public void setTvvVideoType(String tvvVideoType) {
        this.tvvVideoType = tvvVideoType;
    }

    public String getTvvVideourl() {
        return tvvVideourl;
    }

    public void setTvvVideourl(String tvvVideourl) {
        this.tvvVideourl = tvvVideourl;
    }

    public String getV_type() {
        return v_type;
    }

    public void setV_type(String v_type) {
        this.v_type = v_type;
    }

    public String getFtvsId() {
        return ftvsId;
    }

    public String getTvvdescription() {
        return tvvdescription;
    }

    public void setTvvdescription(String tvvdescription) {
        this.tvvdescription = tvvdescription;
    }

    public void setFtvsId(String ftvsId) {
        this.ftvsId = ftvsId;
    }

    public String getFc_id() {
        return fc_id;
    }

    public void setFc_id(String fc_id) {
        this.fc_id = fc_id;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_image() {
        return cat_image;
    }

    public void setCat_image(String cat_image) {
        this.cat_image = cat_image;
    }

    public String getC_date() {
        return c_date;
    }

    public void setC_date(String c_date) {
        this.c_date = c_date;
    }

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

    public String getTvsDate() {
        return tvsDate;
    }

    public void setTvsDate(String tvsDate) {
        this.tvsDate = tvsDate;
    }

}
