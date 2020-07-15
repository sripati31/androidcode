
package com.divinetechs.gotstart.Model.SubPlanModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("sub_id")
    @Expose
    private String subId;
    @SerializedName("sub_name")
    @Expose
    private String subName;
    @SerializedName("sub_price")
    @Expose
    private String subPrice;
    @SerializedName("sub_type")
    @Expose
    private String subType;
    @SerializedName("currency_type")
    @Expose
    private String currency_type;
    @SerializedName("sub_status")
    @Expose
    private String subStatus;

    public String getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(String currency_type) {
        this.currency_type = currency_type;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getSubPrice() {
        return subPrice;
    }

    public void setSubPrice(String subPrice) {
        this.subPrice = subPrice;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }

}
