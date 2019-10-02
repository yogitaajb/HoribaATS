package com.horibacare.ats.customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.horibacare.ats.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class OverviewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView tv_ticketno,tv_custNAME,tv_reported_by,tv_building_no,tv_lab_no,tv_equipname,tv_equip_serialno,tv_warranty_startdate
            ,tv_warranty_enddate,tv_description,tv_call_attended_on,tv_assigned_engineer;
    TextView txt_details;
    Button btn_back;

    String equipment_id;

    public OverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OverviewFragment newInstance(String param1, String param2) {
        OverviewFragment fragment = new OverviewFragment();
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
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        Bundle bundle = getArguments();
        equipment_id = bundle.getString("equipment_id");

        tv_ticketno=(TextView) view.findViewById(R.id.tv_ticketno);
        tv_custNAME=(TextView) view.findViewById(R.id.tv_custNAME);
        tv_reported_by=(TextView) view.findViewById(R.id.tv_reported_by);
        tv_building_no=(TextView) view.findViewById(R.id.tv_building_no);
        tv_lab_no=(TextView) view.findViewById(R.id.tv_lab_no);
        tv_equipname=(TextView) view.findViewById(R.id.tv_equipname);
        tv_equip_serialno=(TextView) view.findViewById(R.id.tv_equip_serialno);
        tv_warranty_startdate=(TextView) view.findViewById(R.id.tv_warranty_startdate);
        tv_warranty_enddate=(TextView) view.findViewById(R.id.tv_warranty_enddate);
        tv_description=(TextView) view.findViewById(R.id.tv_description);
        tv_call_attended_on=(TextView) view.findViewById(R.id.tv_call_attended_on);
        tv_assigned_engineer=(TextView) view.findViewById(R.id.tv_assigned_engineer);

        txt_details=(TextView) view.findViewById(R.id.txt_details);
        btn_back=(Button) view.findViewById(R.id.btn_back);
        loadProducts();
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                // bundle.putString("id", item1.getId());
                // bundle.putString("title", item1.getBuiding_name());

                Fragment selectedFragment = null;
                selectedFragment = new ViewExistingComplaintsFragment();
                //  if(selectedFragment!=null) {
                selectedFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, selectedFragment);
                transaction.commit();
                //}
            }
        });
        return view;
    }

    public void loadProducts() {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Loading", "Please Wait..", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.JSON_URL + "serviceCalls.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("veer", "response" + response);
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("ack")) {
                        JSONArray ja = jsonObject.getJSONArray("complaints");
                        for (int i=0;i<ja.length();i++) {
                            JSONObject jo = ja.getJSONObject(i);
                           // BuildingItem item = new BuildingItem();

                            String compl_id= jo.getString("id");
                            tv_ticketno.setText(jo.getString("complaintCode"));
                            tv_custNAME.setText(jo.getString("complaintCode"));
                            tv_reported_by.setText(jo.getString("problemReported"));
                            tv_building_no.setText(jo.getString("buildingCode"));
                            tv_lab_no.setText(jo.getString("labNumber"));
                            tv_equipname.setText(jo.getString("equipmentDesc"));
                            tv_equip_serialno.setText(jo.getString("equipmentCode"));
                            tv_warranty_startdate.setText(jo.getString("reportedTime"));
                            tv_warranty_enddate.setText(jo.getString("malfunctionTime"));
                            tv_description.setText(jo.getString("equipmentDesc"));
                            tv_call_attended_on.setText(jo.getString("customer"));
                            tv_assigned_engineer.setText(jo.getString("customer"));

                            //alItem.add(item);
                        }
                    } else {
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    //gridView.setAdapter(new ItemAdapter(getActivity(), alItem));
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
                params.put("method", "getComplaints");
                //params.put("imei", prefManager.getImei());
                params.put("equipmentId", equipment_id);
               // params.put("complaint_id", "1");//equipment_id   complaint_id
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
