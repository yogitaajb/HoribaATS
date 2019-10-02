package com.horibacare.ats.customer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.horibacare.ats.Config;
import com.horibacare.ats.PrefManager;
import com.horibacare.ats.R;
import com.horibacare.ats.helpers.CountryState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileCFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText etFName, etLName, etDesignation, etContact, etAddrLine1, etAddrLine2, etAddrLine3;
    EditText etCity, etPincode;
    Spinner spDept, spSalutation, spcountry, spState, spCompanies;
    ArrayList<String> alCountries, alStates, alDept, alCompanies, alSalutation;
    ArrayList<CountryState> alLocations;

    PrefManager prefManager;

    public ProfileCFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileCFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileCFragment newInstance(String param1, String param2) {
        ProfileCFragment fragment = new ProfileCFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_c, container, false);

        prefManager = new PrefManager(getActivity());

        etFName = (EditText) view.findViewById(R.id.etFName);
        etLName = (EditText) view.findViewById(R.id.etLName);
        etDesignation = (EditText) view.findViewById(R.id.etDesignation);
        spCompanies = (Spinner) view.findViewById(R.id.spCompanies);
        etContact = (EditText) view.findViewById(R.id.etPhone);
        etAddrLine1 = (EditText) view.findViewById(R.id.etAddrLine1);
        etAddrLine2 = (EditText) view.findViewById(R.id.etAddrLine2);
        etAddrLine3 = (EditText) view.findViewById(R.id.etAddrLine3);
        etCity = (EditText) view.findViewById(R.id.etCity);
        spState = (Spinner) view.findViewById(R.id.etState);
        spcountry = (Spinner) view.findViewById(R.id.etCountry);
        etPincode = (EditText) view.findViewById(R.id.etPincode);
        spDept = (Spinner) view.findViewById(R.id.spDept);

        spSalutation = (Spinner) view.findViewById(R.id.spSalutation);
        alSalutation = new ArrayList<>();
        //alSalutation.add("Salutation");
        alSalutation.add("Miss");alSalutation.add("Mr");alSalutation.add("Mrs");
        spSalutation.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.tv, alSalutation));

        getData();

        Button btnUpdate = (Button) view.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });


        return view;
    }

    public void submit() {
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
            Toast.makeText(getActivity(), "Select State", Toast.LENGTH_SHORT).show();
        }
        if (etPincode.getText().toString().trim().trim().length()==0) {
            flag = false;
            etPincode.setError("Pincode is mandatory");
        }
        if (spcountry.getSelectedItem().toString().trim().trim().equalsIgnoreCase("Select country")) {
            flag = false;
            //etCountry.setError("Country is mandatory");
            Toast.makeText(getActivity(), "Select Country", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Please select salutation", Toast.LENGTH_LONG).show();
        }
        if(flag) {
            update();
        } else {
            Toast.makeText(getActivity(), "Invalid details", Toast.LENGTH_LONG).show();
        }
    }

    public void update() {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Loading", "Please Wait..", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.JSON_URL + "users.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("veer", "response" + response);
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("ack")) {
                        prefManager.setName(etFName.getText().toString().trim() + " " + etLName.getText().toString().trim());
                        Fragment selectedFragment = null;
                        selectedFragment = new HomeCFragment();
                        if(selectedFragment!=null) {
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.container, selectedFragment);
                            transaction.commit();
                        }
                    } else {
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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
                params.put("isProfile", "1");
                return params;
            }
        };
        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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

        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Loading", "Please Wait..", true);
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
                        spDept.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.tv, alDept));

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
                        spcountry.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.tv, alCountries));
                        spState.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.tv, alStates));

                        JSONArray jaComp = jsonObject.getJSONArray("companies");
                        for(int i=0;i<jaComp.length();i++) {
                            JSONObject joDept = jaComp.getJSONObject(i);
                            alCompanies.add(joDept.getString("name"));
                        }
                        spCompanies.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.tv, alCompanies));

                        JSONObject joUser = jsonObject.getJSONObject("user");
                        spSalutation.setSelection(alSalutation.indexOf(joUser.getString("salutation")));
                        etFName.setText(joUser.getString("firstName"));
                        etLName.setText(joUser.getString("lastName"));
                        etDesignation.setText(joUser.getString("designation"));
                        spDept.setSelection(alDept.indexOf(joUser.getString("department")));
                        spCompanies.setSelection(alCompanies.indexOf(joUser.getString("companyName")));
                        etContact.setText(joUser.getString("mobile"));
                        etAddrLine1.setText(joUser.getString("street1"));
                        etAddrLine2.setText(joUser.getString("street2"));
                        etAddrLine3.setText(joUser.getString("street3"));
                        spcountry.setSelection(alCountries.indexOf(joUser.getString("country")));
                        spState.setSelection(alStates.indexOf(joUser.getString("state")));
                        etCity.setText(joUser.getString("city"));
                        etPincode.setText(joUser.getString("pincode"));

                        final String state = joUser.getString("state");

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
                                spState.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.tv, alStates));
                                spState.setSelection(alStates.indexOf(state));
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });


                    } else {
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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
                params.put("method", "getProfileData");
                params.put("imei", prefManager.getImei());
                params.put("fromAccount", prefManager.getUserId());
                return params;
            }
        };
        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });
    }
}
