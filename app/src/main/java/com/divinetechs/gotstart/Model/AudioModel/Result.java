
package com.divinetechs.gotstart.Model.AudioModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("audio_name")
    @Expose
    private String audioName;
    @SerializedName("audio_desc")
    @Expose
    private String audioDesc;
    @SerializedName("audio_image")
    @Expose
    private String audioImage;
    @SerializedName("audio_url")
    @Expose
    private String audioUrl;
    @SerializedName("audio_view")
    @Expose
    private String audioView;
    @SerializedName("status")
    @Expose
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public String getAudioDesc() {
        return audioDesc;
    }

    public void setAudioDesc(String audioDesc) {
        this.audioDesc = audioDesc;
    }

    public String getAudioImage() {
        return audioImage;
    }

    public void setAudioImage(String audioImage) {
        this.audioImage = audioImage;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getAudioView() {
        return audioView;
    }

    public void setAudioView(String audioView) {
        this.audioView = audioView;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
