
package com.divinetechs.gotstart.Model.ChannelModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("channel_name")
    @Expose
    private String channelName;
    @SerializedName("channel_desc")
    @Expose
    private String channel_desc;
    @SerializedName("channel_image")
    @Expose
    private String channelImage;
    @SerializedName("channel_url")
    @Expose
    private String channelUrl;
    @SerializedName("status")
    @Expose
    private String status;

    public String getChannel_desc() {
        return channel_desc;
    }

    public void setChannel_desc(String channel_desc) {
        this.channel_desc = channel_desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelImage() {
        return channelImage;
    }

    public void setChannelImage(String channelImage) {
        this.channelImage = channelImage;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
