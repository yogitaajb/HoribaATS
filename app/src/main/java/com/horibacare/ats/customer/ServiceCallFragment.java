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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ServiceCallFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView tv_reported_by,tv_emailId,tv_mobile_no,tv_description;
Button btn_cancel,btn_submit;
TextView tvattach;
    String equipment_id,reported_date,reported_time;



    public ServiceCallFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServiceCallFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ServiceCallFragment newInstance(String param1, String param2) {
        ServiceCallFragment fragment = new ServiceCallFragment();
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
        View view=inflater.inflate(R.layout.fragment_service_call, container, false);

        Bundle bundle = getArguments();
        equipment_id = bundle.getString("equipment_id");

        tv_reported_by=(TextView) view.findViewById(R.id.tv_reported_by);
        tv_emailId=(TextView) view.findViewById(R.id.tv_emailId);
        tv_mobile_no=(TextView) view.findViewById(R.id.tv_mobile_no);
        tv_description=(TextView) view.findViewById(R.id.tv_description);
        tvattach=(TextView) view.findViewById(R.id.tvattach);

        btn_cancel=(Button) view.findViewById(R.id.btn_cancel);
        btn_submit=(Button) view.findViewById(R.id.btn_submit);


        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
         reported_date = format.format(today);
       // System.out.println(dateToStr);

        Date today1 = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("hh:mm:ss a");
         reported_time = format.format(today);
       // System.out.println(dateToStr);

        tvattach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                // bundle.putString("id", item1.getId());
                // bundle.putString("title", item1.getBuiding_name());

                Fragment selectedFragment = null;
                selectedFragment = new EquipmentDetailsFragment();
                //  if(selectedFragment!=null) {
                selectedFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, selectedFragment);
                transaction.commit();
                //}
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertComplaint();
            }
        });

        return view;
    }

    public void InsertComplaint() {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Loading", "Please Wait..", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.JSON_URL + "serviceCalls.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("veer", "response" + response);
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("message").equalsIgnoreCase("Success")) {

                        Toast.makeText(getActivity(), "Complaint Register successfully", Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), "Complaint not registered", Toast.LENGTH_LONG).show();
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
                params.put("method", "insertComplaint");
                params.put("equipmentId", equipment_id);  // equipment_id;
                params.put("reportedDate", reported_date);//reported_date
                 params.put("reportedTime", reported_date);//reported_time
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
