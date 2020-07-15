package com.divinetechs.gotstart.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.divinetechs.gotstart.Model.LoginRegister.LoginRegiModel;
import com.divinetechs.gotstart.R;
import com.divinetechs.gotstart.Utility.PrefManager;
import com.divinetechs.gotstart.Webservice.AppAPI;
import com.divinetechs.gotstart.Webservice.BaseURL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText et_email, et_password;
    String str_email, str_password;

    TextView txt_already_signup, txt_login, txt_skip, txt_forgot;
    ProgressDialog progressDialog;
    private PrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.loginactivity);

        Init();

        txt_already_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Registration.class));
            }
        });

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_email = et_email.getText().toString();
                str_password = et_password.getText().toString();

                if (TextUtils.isEmpty(str_email)) {
                    Toast.makeText(LoginActivity.this, "Enter Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(str_password)) {
                    Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                SignIn();
            }
        });
        txt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
        txt_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
            }
        });

    }


    public void Init() {
        prefManager = new PrefManager(this);
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        txt_already_signup = (TextView) findViewById(R.id.txt_already_signup);
        txt_login = (TextView) findViewById(R.id.txt_login);
        txt_skip = (TextView) findViewById(R.id.txt_skip);
        txt_forgot = (TextView) findViewById(R.id.txt_forgot);
    }

    public void SignIn() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<LoginRegiModel> call = bookNPlayAPI.login(str_email, str_password);
        call.enqueue(new Callback<LoginRegiModel>() {
            @Override
            public void onResponse(Call<LoginRegiModel> call, Response<LoginRegiModel> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    if (response.body().getStatus() == 200) {
                        prefManager.setLoginId("" + response.body().getUserid());
                        prefManager.setPremiumID("" + response.body().getIs_premium());
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else if (response.body().getStatus() == 400) {
                        Toast.makeText(LoginActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginRegiModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
