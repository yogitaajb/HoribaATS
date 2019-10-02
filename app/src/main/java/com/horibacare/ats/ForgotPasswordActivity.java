package com.horibacare.ats;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etUnm, etOtp;
    PrefManager prefManager;
    String otp = "1111", email = "";
    LinearLayout llOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        prefManager = new PrefManager(this);
        etUnm = (EditText) findViewById(R.id.etLoginUnm);
        etOtp = (EditText) findViewById(R.id.etOtp);
        llOtp = (LinearLayout) findViewById(R.id.llOtp);
        llOtp.setVisibility(View.GONE);

        TextView tvLogin = (TextView) findViewById(R.id.tvLogin);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
                finish();
            }
        });

    }
    public void submit(View v) {
        String contactPattern = "[6789][0-9]{9}";
        email = etUnm.getText().toString().trim();

        boolean flag = true;
//        if(phone.length()==0) {
//            flag = false;
//            etUnm.setError("Mobile is mandatory");
//        }
//        if(!phone.matches(contactPattern)) {
//            flag = false;
//            etUnm.setError("Invalid mobile number");
//        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            flag = false;
            etUnm.setError("Invalid value");
        }
        if(flag) {
            sendLink();
            //llOtp.setVisibility(View.VISIBLE);
        }
    }

    public void sendLink() {
        final ProgressDialog progressDialog = ProgressDialog.show(ForgotPasswordActivity.this, "Loading", "Please Wait..", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.JSON_URL + "users.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("veer", "response" + response);
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("ack")) {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.d("veer", "" + e.getMessage());
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("veer", "" + error.getMessage());
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("method", "sendChangePwdLink");
                params.put("imei", prefManager.getImei());
                params.put("fromAccount", prefManager.getUserId());
                params.put("email", etUnm.getText().toString().trim());
                return params;
            }
        };
        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });
    }

//    public void verifyOTP(View v) {
//        String enteredOtp = etOtp.getText().toString().trim();
//        if(enteredOtp.length()==0) {
//            etOtp.setError("OTP is mandatory.");
//        } else {
//            if(enteredOtp.equals(otp)) {
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                intent.putExtra("phone", phone);
//                overridePendingTransition(R.anim.right_left, R.anim.left_right);
//                startActivity(intent);
//            } else {
//                etOtp.setError("Oops! OTP not matched.");
//            }
//        }
//
//    }
}
