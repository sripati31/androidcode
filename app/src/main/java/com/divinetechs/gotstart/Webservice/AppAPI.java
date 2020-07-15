package com.divinetechs.gotstart.Webservice;


import com.divinetechs.gotstart.Activity.DownloadList;
import com.divinetechs.gotstart.Model.AudioModel.AudioModel;
import com.divinetechs.gotstart.Model.BannerModel.BannerModel;
import com.divinetechs.gotstart.Model.ChannelModel.ChannelModel;
import com.divinetechs.gotstart.Model.DownloadModel.DownloadModel;
import com.divinetechs.gotstart.Model.GeneralSettings.GeneralSettings;
import com.divinetechs.gotstart.Model.LoginRegister.LoginRegiModel;
import com.divinetechs.gotstart.Model.ProfileModel.ProfileModel;
import com.divinetechs.gotstart.Model.SubPlanModel.SubPlanModel;
import com.divinetechs.gotstart.Model.SuccessModel.SuccessModel;
import com.divinetechs.gotstart.Model.TvSerialModel.TvSerialModel;
import com.divinetechs.gotstart.Model.TvVideoModel.TvVideoModel;
import com.divinetechs.gotstart.Model.TvsVideoDetail.TvsVideoDetail;
import com.divinetechs.gotstart.Model.WatchListModel.WatchListModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AppAPI {


    @FormUrlEncoded
    @POST("login")
    Call<LoginRegiModel> login(@Field("email") String email_id,
                               @Field("password") String password);

    @FormUrlEncoded
    @POST("registration")
    Call<LoginRegiModel> Registration(@Field("fullname") String full_name,
                                      @Field("email") String email_id,
                                      @Field("password") String password,
                                      @Field("mobile_number") String phone);

    @GET("general_setting")
    Call<GeneralSettings> general_settings();

    @FormUrlEncoded
    @POST("bannerlist")
    Call<TvVideoModel> Banner(@Field("type") String type);

    @FormUrlEncoded
    @POST("profile")
    Call<ProfileModel> profile(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("top_pick_for_you")
    Call<TvSerialModel> top_pick_for_you(@Field("type") String type);

    @FormUrlEncoded
    @POST("top_pick_for_you")
    Call<TvVideoModel> top_pick_for_you_msn(@Field("type") String type);

    @FormUrlEncoded
    @POST("popularshow")
    Call<TvSerialModel> popularshow(@Field("type") String type);

    @FormUrlEncoded
    @POST("popularshow")
    Call<TvVideoModel> popularshow_MSN(@Field("type") String type);

    @FormUrlEncoded
    @POST("popular_in")
    Call<TvSerialModel> popular_in(@Field("cat_id") String cat_id,
                                   @Field("type") String type);

    @FormUrlEncoded
    @POST("popular_in")
    Call<TvVideoModel> popular_in_msn(@Field("cat_id") String cat_id,
                                   @Field("type") String type);

    @FormUrlEncoded
    @POST("show_by_cat")
    Call<TvSerialModel> show_by_cat(@Field("cat_id") String cat_id);

    @FormUrlEncoded
    @POST("episode_by_id")
    Call<TvVideoModel> episode_by_id(@Field("ftvs_id") String tvs_id);

    @FormUrlEncoded
    @POST("popular_epi_by_id")
    Call<TvVideoModel> popular_epi_by_id(@Field("tvs_id") String tvs_id);

    @FormUrlEncoded
    @POST("tv_video_by_serial_id")
    Call<TvVideoModel> tv_video_by_serial_id(@Field("ftvs_id") String tvs_id);

    @FormUrlEncoded
    @POST("tv_video_by_serial_id_msn")
    Call<TvVideoModel> tv_video_by_serial_id_msn(@Field("tvv_id") String tvs_id,
                                                 @Field("type") String type);


    @GET("popularshow")
    Call<TvVideoModel> you_may_also_like();

    @GET("Channellist")
    Call<ChannelModel> Channellist();

    @GET("PopularChannellist")
    Call<ChannelModel> PopularChannellist();

    @FormUrlEncoded
    @POST("get_all_news")
    Call<TvVideoModel> get_all_news(@Field("type") String type);

    @FormUrlEncoded
    @POST("top_pick_for_you")
    Call<TvVideoModel> top_pick_for_you_news(@Field("type") String type);

    @FormUrlEncoded
    @POST("add_download")
    Call<SuccessModel> add_download(@Field("user_id") String user_id,
                                    @Field("tv_id") String tv_id,
                                    @Field("a_id") String a_id);

    @FormUrlEncoded
    @POST("getdownload")
    Call<DownloadModel> getdownload(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("remove_download")
    Call<SuccessModel> remove_download(@Field("d_id") String d_id);


    @FormUrlEncoded
    @POST("add_watchlist")
    Call<SuccessModel> add_watchlist(@Field("user_id") String user_id,
                                     @Field("tv_id") String tv_id,
                                     @Field("a_id") String a_id,
                                     @Field("c_id") String c_id);

    @FormUrlEncoded
    @POST("getwatchlist")
    Call<WatchListModel> getwatchlist(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("remove_watchlist")
    Call<SuccessModel> remove_watchlist(@Field("w_id") String w_id);

    @GET("get_subplan")
    Call<SubPlanModel> get_subplan();

    @FormUrlEncoded
    @POST("add_transacation")
    Call<SuccessModel> add_transacation(@Field("user_id") String user_id,
                                    @Field("sub_id") String sub_id);

    @GET("audiolist")
    Call<AudioModel> Audiolist();

    @GET("Popularaudiolist")
    Call<AudioModel> Popularaudiolist();

    /*Sport*/

    @FormUrlEncoded
    @POST("sport_by_id")
    Call<TvVideoModel> sport_by_id(@Field("fc_id") String tvs_id);


    /*====Movie======*/

    @FormUrlEncoded
    @POST("movie_by_id")
    Call<TvVideoModel> movie_by_id(@Field("fc_id") String tvs_id);

    /*====News=======*/

    @FormUrlEncoded
    @POST("news_by_id")
    Call<TvVideoModel> news_by_id(@Field("fc_id") String tvs_id);

    /*====Premium=======*/

    @FormUrlEncoded
    @POST("premium_video")
    Call<TvVideoModel> premium_video(@Field("type") String type);
}
