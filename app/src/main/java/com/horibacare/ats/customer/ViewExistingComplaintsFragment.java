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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.horibacare.ats.Config;
import com.horibacare.ats.PrefManager;
import com.horibacare.ats.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ViewExistingComplaintsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    GridView gridView;
    ArrayList<BuildingItem> alItem;
    PrefManager prefManager;
    String category = "EMS";
    String equipment_id;
    ArrayList<BuildingItem> animalList;

    public ViewExistingComplaintsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewExistingComplaintsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewExistingComplaintsFragment newInstance(String param1, String param2) {
        ViewExistingComplaintsFragment fragment = new ViewExistingComplaintsFragment();
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
        View view = inflater.inflate(R.layout.fragment_view_existing_complaints, container, false);

       // return inflater.inflate(R.layout.fragment_view_existing_complaints, container, false);
        Config.screenNumber = -1;
        Bundle bundle = getArguments();
        equipment_id = bundle.getString("equipment_id");

        prefManager = new PrefManager(getActivity());
        gridView = (GridView) view.findViewById(R.id.gvProducts);

         loadProducts();
        /*animalList=new ArrayList<BuildingItem>();
        animalList.add(new BuildingItem("1","CC00001","20-sept-2019, 6.30 PM","20-sept-2019, 6.30 PM","Closed"));
        animalList.add(new BuildingItem("1","CC00001","20-sept-2019, 6.30 PM","20-sept-2019, 6.30 PM","Open"));

        gridView.setAdapter(new ItemAdapter(getActivity(), animalList));*/
        return view;
    }


      public void loadProducts() {
            alItem = new ArrayList<>();
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
                                BuildingItem item = new BuildingItem();
                                item.setId(jo.getString("id"));
                                item.setTicket_id(jo.getString("complaintCode"));
                                item.setRaised_on(jo.getString("reportedTime"));
                                item.setClosure_date(jo.getString("malfunctionTime"));
                                item.setStatus(jo.getString("status"));
                                alItem.add(item);
                            }
                        } else {
                            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        }
                        gridView.setAdapter(new ItemAdapter(getActivity(), alItem));
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
                    params.put("equipmentId", equipment_id);//equipment_id
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


    class BuildingItem {
        String id, ticket_id,raised_on,closure_date,status;
        /*public BuildingItem(String id,String ticket_id,String raised_on,String closure_date,String status)
        {
            this.id=id;
            this.ticket_id=ticket_id;
            this.raised_on=raised_on;
            this.closure_date=closure_date;
            this.status=status;

        }*/
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTicket_id() {
            return ticket_id;
        }

        public void setTicket_id(String ticket_id) {
            this.ticket_id = ticket_id;
        }

        public String getRaised_on() {
            return raised_on;
        }

        public void setRaised_on(String raised_on) {
            this.raised_on = raised_on;
        }

        public String getClosure_date() {
            return closure_date;
        }

        public void setClosure_date(String closure_date) {
            this.closure_date = closure_date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
    class ItemAdapter extends BaseAdapter {
        Context context;
        private LayoutInflater mInflater;
        private ArrayList<BuildingItem> arrayList;// = new ArrayList<Place>();

        public ItemAdapter(Context context, ArrayList<BuildingItem> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final ViewHolder holder;
            final BuildingItem item = arrayList.get(position);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.list_existing_complaints, null);
                holder.tv_ticket_id = (TextView) convertView.findViewById(R.id.tv_ticket_id);
                holder.tv_raised_on = (TextView) convertView.findViewById(R.id.tv_raised_on);
                holder.tv_closure_date = (TextView) convertView.findViewById(R.id.tv_closure_date);
                holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
                holder.lyt_parent = (LinearLayout) convertView.findViewById(R.id.lyt_parent);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_ticket_id.setText(item.getTicket_id());
            holder.tv_raised_on.setText(item.getRaised_on());
            holder.tv_closure_date.setText(item.getClosure_date());
            if(item.getStatus().equalsIgnoreCase("0")) {
                holder.tv_status.setText("Open");
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }else
                if(item.getStatus().equalsIgnoreCase("1")) {
                holder.tv_status.setText("Closed");
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.colorAccent));
            }


            holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //BuildingItem item1 = animalList.get(v.getId());
                    Bundle bundle = new Bundle();
                    bundle.putString("complaint_id", item.getId());
                    bundle.putString("equipment_id", equipment_id);

                    Fragment selectedFragment = null;
                    selectedFragment = new OverviewFragment();
                    //  if(selectedFragment!=null) {
                    selectedFragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, selectedFragment);
                    transaction.commit();
                    //}
                }
            });

           /* Glide.with(getActivity())
                    .load(Config.IMAGE_URL + item.getImage())
                    //.centerCrop()
                    .placeholder(R.drawable.logo)
                    .into(holder.ivImage);*/

            return convertView;
        }
    }
    class ViewHolder {
        TextView tv_ticket_id,tv_raised_on,tv_closure_date,tv_status;
       LinearLayout lyt_parent;

    }
}

