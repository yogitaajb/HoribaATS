package com.horibacare.ats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {

    TextView tvForgetPwd, tvSignup;
    EditText etUnm, etPwd;
    CheckBox cbRemember;

    PrefManager prefManager;
    String userType = "customer";
    boolean passwordToggle = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefManager = new PrefManager(this);

        if (Build.VERSION.SDK_INT >= 23) {
            getAllPermissions();
        } else {
            readImei();
        }

        if(prefManager.getIsLogin()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_right, R.anim.right_left);
            finish();
        }

        etUnm = (EditText) findViewById(R.id.etLoginUnm);
        etPwd = (EditText) findViewById(R.id.etLoginPwd);
        cbRemember = (CheckBox) findViewById(R.id.cbLogin);
        tvSignup = (TextView) findViewById(R.id.tvSignup);

        tvForgetPwd = (TextView) findViewById(R.id.tvLoginForgetPwd);

        tvForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);

//                Intent intent = new Intent(getApplicationContext(), OTPActivity.class);
//                intent.putExtra("otp", "1111");
//                startActivity(intent);
//                overridePendingTransition(R.anim.left_right, R.anim.right_left);
//                finish();
            }
        });

        etPwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (etPwd.getRight() - etPwd.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if(passwordToggle) {
                            passwordToggle = false;
                            etPwd.setInputType(InputType.TYPE_CLASS_TEXT);
                            etPwd.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide, 0);
                        } else {
                            passwordToggle = true;
                            etPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            etPwd.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye, 0);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void register(View v) {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }

    public void login(View v) {
        String contactPattern = "[6789][0-9]{9}";
        String username = etUnm.getText().toString().trim();
        String password = etPwd.getText().toString().trim();

        boolean flag = true;
        if(username.length()==0) {
            flag = false;
            etUnm.setError("Username is mandatory");
        }
        if(password.length()==0) {
            flag = false;
            etPwd.setError("Password is mandatory");
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(username.trim()).matches()) {
            flag = false;
            etUnm.setError("Invalid value");
        }
//        if(!username.matches(contactPattern)) {
//            flag = false;
//            etUnm.setError("Invalid value");
//        }
        if(flag) {
            authenticate();
        } else {
            Toast.makeText(getApplicationContext(), "Invalid details.", Toast.LENGTH_LONG).show();
        }
    }

    public void authenticate() {
        final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "Loading", "Please Wait..", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.JSON_URL + "users.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("veer", "response" + response);
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("ack")) {
                        //Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        if(cbRemember.isChecked()) {
                            prefManager.setIsLogin(true);
                        }
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        prefManager.setUserId(jsonObject1.getString("id"));
                        prefManager.setName(jsonObject1.getString("firstName") + " " + jsonObject1.getString("lastName"));
                        String userType = jsonObject1.getString("userType").trim();
                        prefManager.setUserType(userType);
                        if(userType.equals("horiba")) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
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
                Log.d("veer", prefManager.getImei());
                params.put("method", "login");
                params.put("imei", prefManager.getImei());
                params.put("username", etUnm.getText().toString().trim());
                params.put("password", etPwd.getText().toString().trim());
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

    public void getAllPermissions() {
        String[] PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, PERMISSIONS, 10);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (hasAllPermissionsGranted(grantResults)) {
//                    TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
//                    String imei = telephonyManager.getDeviceId();
//                    prefManager.setImei(imei);
                    readImei();
                } else {
                    // Permission Denied
                    //ResourceElements.showDialogOk(SplashActivity.this, "Alert", "You have to accept all permission to use applicaiton");
                    getAllPermissions();
                }
                break;
            default:
                Log.d("veer", "default");
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    private void readImei() {
        if (Build.VERSION.SDK_INT >= 23) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                        android.Manifest.permission.READ_PHONE_STATE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                    TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                    String imei = telephonyManager.getDeviceId();
                    //Toast.makeText(getApplicationContext(), imei + "", Toast.LENGTH_LONG).show();
                    prefManager.setImei(imei);

                    //requestPermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE}, 1);
                } else {
                    // No explanation needed, we can request the permission.

                    requestPermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE},
                            1);

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                requestPermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE},
                        1);
            }
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            //Toast.makeText(getApplicationContext(), imei + "", Toast.LENGTH_LONG).show();
            prefManager.setImei(imei);

        }
    }
}
