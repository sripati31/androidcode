package com.divinetechs.gotstart.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.divinetechs.gotstart.Adapter.SubscriptionAdapter;
import com.divinetechs.gotstart.Interface.Watchinter;
import com.divinetechs.gotstart.Model.SubPlanModel.Result;
import com.divinetechs.gotstart.Model.SubPlanModel.SubPlanModel;
import com.divinetechs.gotstart.Model.SuccessModel.SuccessModel;
import com.divinetechs.gotstart.R;
import com.divinetechs.gotstart.Utility.PrefManager;
import com.divinetechs.gotstart.Webservice.AppAPI;
import com.divinetechs.gotstart.Webservice.BaseURL;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Subscription extends AppCompatActivity implements Watchinter {

    PrefManager prefManager;
    ProgressDialog progressDialog;

    TextView toolbar_title;
    TextView txt_continue, txt_alread_buy;

    List<Result> SubList;
    SubscriptionAdapter subscriptionAdapter;

    RecyclerView recycler_premium;

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    // note that these credentials will differ between live & sandbox
    // environments.
    private static final String CONFIG_CLIENT_ID = "AR2Grx4Mmu4IDhhFyrpCfcCaHJt2B2GTj4zdCU_gzUBG0kt3SPrjFMvsgUr9ajT6uuoFxq2xx6iwZRXN";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Ebook App")
            .merchantPrivacyPolicyUri(
                    Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(
                    Uri.parse("https://www.example.com/legal"));

    PayPalPayment thingToBuy;

    String amount, currency_code, short_description, id, state, create_time;
    String Select_item_name,Select_item_price;
    int pos = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription);

        prefManager = new PrefManager(Subscription.this);
        progressDialog = new ProgressDialog(Subscription.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("Subscription");

        TextView txt_back = (TextView) findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recycler_premium = (RecyclerView) findViewById(R.id.recycler_premium);

        txt_alread_buy = (TextView) findViewById(R.id.txt_alread_buy);
        txt_continue = (TextView) findViewById(R.id.txt_continue);
        txt_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!prefManager.getLoginId().equalsIgnoreCase("0")) {
                    thingToBuy = new PayPalPayment(new BigDecimal("" + Select_item_price), "USD",
                            "" + Select_item_name, PayPalPayment.PAYMENT_INTENT_SALE);
                    Intent intent = new Intent(Subscription.this,
                            PaymentActivity.class);
                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
                    startActivityForResult(intent, REQUEST_CODE_PAYMENT);
                } else {
                    startActivity(new Intent(Subscription.this, LoginActivity.class));
                }
            }
        });

//        ly_annual.setBackground(getResources().getDrawable(R.drawable.round_bor_gray));
//        ly_monthly.setBackground(getResources().getDrawable(R.drawable.round_bor_yellow));

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        getsublist();

        Log.e("PremiumID", "" + prefManager.getPremiumID());

        if (!prefManager.getPremiumID().equalsIgnoreCase("0")) {
            txt_alread_buy.setVisibility(View.VISIBLE);
        } else {
            txt_alread_buy.setVisibility(View.GONE);
        }

    }

    private void getsublist() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<SubPlanModel> call = bookNPlayAPI.get_subplan();
        call.enqueue(new Callback<SubPlanModel>() {
            @Override
            public void onResponse(Call<SubPlanModel> call, Response<SubPlanModel> response) {
                progressDialog.dismiss();

                SubList = new ArrayList<Result>();
                SubList = response.body().getResult();

                subscriptionAdapter = new SubscriptionAdapter(Subscription.this, SubList,
                        "episode", Subscription.this);
                recycler_premium.setHasFixedSize(true);
                recycler_premium.setLayoutManager(new GridLayoutManager(Subscription.this, 2));
                recycler_premium.setItemAnimator(new DefaultItemAnimator());
                recycler_premium.setAdapter(subscriptionAdapter);
                subscriptionAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<SubPlanModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


    public void onFuturePaymentPressed(View pressed) {
        Intent intent = new Intent(Subscription.this,
                PayPalFuturePaymentActivity.class);
        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        System.out.println(confirm.toJSONObject().toString(4));
                        System.out.println(confirm.getPayment().toJSONObject()
                                .toString(4));


                        Log.e("confirm1", "" + confirm.toJSONObject().toString(4));
                        Log.e("confirm2", "" + confirm.getPayment().toJSONObject()
                                .toString(4));

                        amount = confirm.getPayment().toJSONObject().getString("amount");
                        currency_code = confirm.getPayment().toJSONObject().getString("currency_code");
                        short_description = confirm.getPayment().toJSONObject().getString("short_description");
                        id = confirm.getProofOfPayment().toJSONObject().getString("id");
                        state = confirm.getProofOfPayment().toJSONObject().getString("state");
                        create_time = confirm.getProofOfPayment().toJSONObject().getString("create_time");

                        Log.e("amount", "" + amount);
                        Log.e("currency_code", "" + currency_code);
                        Log.e("short_description", "" + short_description);
                        Log.e("id", "" + id);
                        Log.e("state", "" + state);
                        Log.e("create_time", "" + create_time);

                        PurchaseSub();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                System.out
                        .println("An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth = data
                        .getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.e("FuturePaymentExample", auth.toJSONObject()
                                .toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.e("FuturePaymentExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        Toast.makeText(getApplicationContext(),
                                "Future Payment code received from PayPal",
                                Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample",
                                "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e("FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    private void PurchaseSub() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<SuccessModel> call = bookNPlayAPI.add_transacation(prefManager.getLoginId(), SubList.get(pos).getSubId());
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                progressDialog.dismiss();

                Log.e("PurchaseBook", "" + response.body().getMessage());
                prefManager.setPremiumID("1");

                new AlertDialog.Builder(Subscription.this)
                        .setTitle(getResources().getString(R.string.app_name))
                        .setMessage("" + response.body().getMessage())
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!prefManager.getPremiumID().equalsIgnoreCase("0")) {
                                    txt_alread_buy.setVisibility(View.VISIBLE);
                                } else {
                                    txt_alread_buy.setVisibility(View.GONE);
                                }
                                finish();
                            }
                        }).show();
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


    private void sendAuthorizationToServer(PayPalAuthorization authorization) {
    }

    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void remove_item(String id) {

        Select_item_name=SubList.get(Integer.parseInt(id)).getSubName();
        Select_item_price=SubList.get(Integer.parseInt(id)).getSubPrice();

        Log.e("Select_item_name==>", ""+Select_item_name);
        Log.e("Select_item_price==>", ""+Select_item_price);
    }
}
