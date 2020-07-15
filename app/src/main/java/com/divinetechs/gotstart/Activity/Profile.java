package com.divinetechs.gotstart.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.divinetechs.gotstart.Model.ProfileModel.ProfileModel;
import com.divinetechs.gotstart.R;
import com.divinetechs.gotstart.Utility.PrefManager;
import com.divinetechs.gotstart.Webservice.AppAPI;
import com.divinetechs.gotstart.Webservice.BaseURL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {

    ProgressDialog progressDialog;
    PrefManager prefManager;

    TextView txt_name, txt_name1, txt_email, txt_Contact;
    Toolbar toolbar;

    TextView txt_back, toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        prefManager = new PrefManager(Profile.this);
        progressDialog = new ProgressDialog(Profile.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);

        txt_back = (TextView) findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("Profile");

        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_name1 = (TextView) findViewById(R.id.txt_name1);
        txt_email = (TextView) findViewById(R.id.txt_email);
        txt_Contact = (TextView) findViewById(R.id.txt_Contact);

        Get_Profile();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private void Get_Profile() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<ProfileModel> call = bookNPlayAPI.profile("" + prefManager.getLoginId());
        call.enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                if (response.code() == 200) {
                    txt_name1.setText(response.body().getResult().get(0).getFullname());
                    txt_name.setText(response.body().getResult().get(0).getFullname());
                    txt_email.setText(response.body().getResult().get(0).getEmail());
                    txt_Contact.setText(response.body().getResult().get(0).getMobileNumber());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}
