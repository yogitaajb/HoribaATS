package com.horibacare.ats;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.horibacare.ats.helpers.CountryState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    EditText etFName, etLName, etDesignation, etContact, etAddrLine1, etAddrLine2, etAddrLine3;
    EditText etCity, etPincode;
    CheckBox cbTerms;
    Spinner spDept, spSalutation, spcountry, spState, spCompanies;
    ArrayList<String> alCountries, alStates, alDept, alCompanies;
    ArrayList<CountryState> alLocations;

    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        prefManager = new PrefManager(this);

        etFName = (EditText) findViewById(R.id.etFName);
        etLName = (EditText) findViewById(R.id.etLName);
        etDesignation = (EditText) findViewById(R.id.etDesignation);
        spCompanies = (Spinner) findViewById(R.id.spCompanies);
        etContact = (EditText) findViewById(R.id.etPhone);
        etAddrLine1 = (EditText) findViewById(R.id.etAddrLine1);
        etAddrLine2 = (EditText) findViewById(R.id.etAddrLine2);
        etAddrLine3 = (EditText) findViewById(R.id.etAddrLine3);
        etCity = (EditText) findViewById(R.id.etCity);
        spState = (Spinner) findViewById(R.id.etState);
        spcountry = (Spinner) findViewById(R.id.etCountry);
        etPincode = (EditText) findViewById(R.id.etPincode);
        spDept = (Spinner) findViewById(R.id.spDept);

        spSalutation = (Spinner) findViewById(R.id.spSalutation);
        ArrayList<String> alSalutation = new ArrayList<>();
        alSalutation.add("Salutation");
        alSalutation.add("Miss");alSalutation.add("Mr");alSalutation.add("Mrs");
        spSalutation.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.tv, alSalutation));

        cbTerms = (CheckBox) findViewById(R.id.cbTerms);

        TextView tvTerms = (TextView) findViewById(R.id.tvTerms);
        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RegistrationActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.terms_conditions, null);
                dialogBuilder.setView(dialogView);
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });

        getData();
    }

    public void submit(View v) {
        boolean flag = true;
        String contactPattern = "[6789][0-9]{9}";
        String contact = etContact.getText().toString().trim();

        if (etFName.getText().toString().trim().trim().length()==0) {
            flag = false;
            etFName.setError("First name is mandatory");
        }
        if (etLName.getText().toString().trim().trim().length()==0) {
            flag = false;
            etLName.setError("Last name is mandatory");
        }
        if (etDesignation.getText().toString().trim().trim().length()==0) {
            flag = false;
            etDesignation.setError("Designation is mandatory");
        }
//        if (etShopName.getText().toString().trim().trim().length()==0) {
//            flag = false;
//            etShopName.setError("Company name is mandatory");
//        }
        if (etAddrLine1.getText().toString().trim().trim().length()==0) {
            flag = false;
            etAddrLine1.setError("Address is mandatory");
        }
        if (etCity.getText().toString().trim().trim().length()==0) {
            flag = false;
            etCity.setError("City is mandatory");
        }
        if (spState.getSelectedItem().toString().trim().trim().equalsIgnoreCase("Select state")) {
            flag = false;
            //etState.setError("State is mandatory");
            Toast.makeText(getApplicationContext(), "Select State", Toast.LENGTH_SHORT).show();
        }
        if (etPincode.getText().toString().trim().trim().length()==0) {
            flag = false;
            etPincode.setError("Pincode is mandatory");
        }
        if (spcountry.getSelectedItem().toString().trim().trim().equalsIgnoreCase("Select country")) {
            flag = false;
            //etCountry.setError("Country is mandatory");
            Toast.makeText(getApplicationContext(), "Select Country", Toast.LENGTH_SHORT).show();
        }
        if (contact.trim().length()==0) {
            flag = false;
            etContact.setError("Mobile is mandatory");
        }
        if(!contact.matches(contactPattern)) {
            flag = false;
            etContact.setError("Mobile must be 10 digit long");
        }
        if(spSalutation.getSelectedItem().toString().trim().equals("Salutation")) {
            flag = false;
            Toast.makeText(getApplicationContext(), "Please select salutation", Toast.LENGTH_LONG).show();
        }
        if(!cbTerms.isChecked()) {
            flag = false;
            Toast.makeText(getApplicationContext(), "Accept Terms & Conditions", Toast.LENGTH_LONG).show();
        }
        if(flag) {
            register();
        } else {
            Toast.makeText(getApplicationContext(), "Invalid details", Toast.LENGTH_LONG).show();
        }
    }

    public void register() {
        final ProgressDialog progressDialog = ProgressDialog.show(RegistrationActivity.this, "Loading", "Please Wait..", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.JSON_URL + "users.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("veer", "response" + response);
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("ack")) {
                        prefManager.setName(etFName.getText().toString().trim() + " " + etLName.getText().toString().trim());
                        Intent intent = new Intent(getApplicationContext(), OTPActivity.class);
                        intent.putExtra("otp", jsonObject.getString("otp"));
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_right, R.anim.right_left);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("veer", "response" + error.getMessage());
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("method", "register");
                params.put("imei", prefManager.getImei());
                params.put("fromAccount", prefManager.getUserId() + "");
                params.put("salutation", spSalutation.getSelectedItem().toString().trim());
                params.put("firstName", etFName.getText().toString().trim());
                params.put("lastName", etLName.getText().toString().trim());
                params.put("designation", etDesignation.getText().toString().trim());
                params.put("department", spDept.getSelectedItem().toString().trim());
                params.put("companyName", spCompanies.getSelectedItem().toString().trim() + "");
                params.put("contact", etContact.getText().toString().trim());
                params.put("addr1", etAddrLine1.getText().toString().trim());
                params.put("addr2", etAddrLine2.getText().toString().trim());
                params.put("addr3", etAddrLine3.getText().toString().trim());
                params.put("city", etCity.getText().toString().trim());
                params.put("state", spState.getSelectedItem().toString().trim());
                params.put("country", spcountry.getSelectedItem().toString().trim());
                params.put("pincode", etPincode.getText().toString().trim());
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

    public void getData() {
        alDept = new ArrayList<>();
        alCountries = new ArrayList<>();
        alStates = new ArrayList<>();
        alLocations = new ArrayList<>();
        alCompanies = new ArrayList<>();

        final ProgressDialog progressDialog = ProgressDialog.show(RegistrationActivity.this, "Loading", "Please Wait..", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.JSON_URL + "api.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("veer", "response" + response);
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("ack")) {
                        JSONArray jaDept = jsonObject.getJSONArray("departments");
                        for(int i=0;i<jaDept.length();i++) {
                            JSONObject joDept = jaDept.getJSONObject(i);
                            alDept.add(joDept.getString("name"));
                        }
                        spDept.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.tv, alDept));

                        JSONArray jaCS = jsonObject.getJSONArray("country_state");
                        for(int i=0;i<jaCS.length();i++) {
                            JSONObject joDept = jaCS.getJSONObject(i);
                            CountryState obj = new CountryState();
                            obj.setCountryName(joDept.getString("countryName"));
                            obj.setStateName(joDept.getString("stateName"));
                            obj.setParentId(joDept.getString("parentId"));
                            alLocations.add(obj);

                            if(!alCountries.contains(joDept.getString("countryName")))
                                alCountries.add(joDept.getString("countryName"));
                            if(!alStates.contains(joDept.getString("stateName")))
                                alStates.add(joDept.getString("stateName"));
                        }
                        spcountry.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.tv, alCountries));
                        spState.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.tv, alStates));

                        JSONArray jaComp = jsonObject.getJSONArray("companies");
                        for(int i=0;i<jaComp.length();i++) {
                            JSONObject joDept = jaComp.getJSONObject(i);
                            alCompanies.add(joDept.getString("name"));
                        }
                        spCompanies.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.tv, alCompanies));

                        spcountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String country = spcountry.getSelectedItem().toString().trim();
                                alStates.clear();
                                for(int  i=0;i<alLocations.size();i++) {
                                    String str = alLocations.get(i).getCountryName();
                                    if(country.equalsIgnoreCase(str)) {
                                        //Log.d("veer", country + ", " + str);
                                        alStates.add(alLocations.get(i).getStateName());
                                    }
                                }
                                spState.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.tv, alStates));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

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
                params.put("method", "getRegData");
                params.put("imei", prefManager.getImei());
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
}
