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


public class EquipmentFragment extends Fragment {
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
    ArrayList<BuildingItem> animalList;
    String building_id,lab_id,lab_name;
    String image,equipmentDesc,mfgNo,hgsNo,sapMaterialCode,startDate,endDate;


    public EquipmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EquipmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EquipmentFragment newInstance(String param1, String param2) {
        EquipmentFragment fragment = new EquipmentFragment();
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
        View view = inflater.inflate(R.layout.fragment_equipment, container, false);
        Config.screenNumber = -1;
        Bundle bundle = getArguments();
        building_id = bundle.getString("building_id");
        lab_id = bundle.getString("lab_id");
        lab_name= bundle.getString("lab_id");
        prefManager = new PrefManager(getActivity());
        gridView = (GridView) view.findViewById(R.id.gvProducts);

        loadProducts();

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
                        JSONArray ja = jsonObject.getJSONArray("equipments");
                        for (int i=0;i<ja.length();i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            BuildingItem item = new BuildingItem();
                            item.setId(jo.getString("id"));
                            item.setTitle(jo.getString("title"));
                            image=jo.getString("image");
                            item.setCode(jo.getString("equipmentCode"));

                            equipmentDesc=jo.getString("equipmentDesc");
                            mfgNo=jo.getString("mfgNo");
                            hgsNo= jo.getString("hgsNo");
                            sapMaterialCode=jo.getString("sapMaterialCode");
                            startDate= jo.getString("startDate");
                            endDate=   jo.getString("endDate");
                            item.setComplaint_status(jo.getString("status"));

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
                params.put("method", "getEquipments");
              //  params.put("imei", prefManager.getImei());
                params.put("userId", prefManager.getUserId());
                params.put("buildingId", building_id);
                params.put("labId", lab_id);
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
        String id, code,title,complaint_status;
      /*  public BuildingItem(String id,String Buiding_name)
        {
            this.id=id;
            this.Buiding_name=Buiding_name;
        }*/
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getComplaint_status() {
            return complaint_status;
        }

        public void setComplaint_status(String complaint_status) {
            this.complaint_status = complaint_status;
        }
    }
    class ItemAdapter extends BaseAdapter {
        Context context;
        private LayoutInflater mInflater;
        private ArrayList<EquipmentFragment.BuildingItem> arrayList;// = new ArrayList<Place>();

        public ItemAdapter(Context context, ArrayList<EquipmentFragment.BuildingItem> arrayList) {
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
            final EquipmentFragment.ViewHolder holder;
            final EquipmentFragment.BuildingItem item = arrayList.get(position);
            if (convertView == null) {
                holder = new EquipmentFragment.ViewHolder();
                convertView = mInflater.inflate(R.layout.list_equipment_item, null);
                holder.tvEquipemntName = (TextView) convertView.findViewById(R.id.tvEquipemntName);
                holder.lyt_parent= (LinearLayout) convertView.findViewById(R.id.lyt_parent);
                holder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
                convertView.setTag(holder);
            } else {
                holder = (EquipmentFragment.ViewHolder) convertView.getTag();
            }

            holder.ivImage.setId(position);
            if(item.getComplaint_status().equalsIgnoreCase("0")) {
                holder.ivImage.setBackgroundColor(context.getResources().getColor(R.color.green));
            }else{
                holder.ivImage.setBackgroundColor(context.getResources().getColor(R.color.horiba_red));
            }
            holder.tvEquipemntName.setText(item.getTitle());

            holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //EquipmentFragment.BuildingItem item1 = alItem.get(v.getId());
                    Bundle bundle = new Bundle();

                    bundle.putString("equipment_id", item.getId());
                    bundle.putString("equipment_title", item.getTitle());
                    bundle.putString("image", image);
                    bundle.putString("equipmentDesc", equipmentDesc);
                    bundle.putString("mfgNo", mfgNo);
                    bundle.putString("hgsNo", hgsNo);
                    bundle.putString("sapMaterialCode", sapMaterialCode);
                    bundle.putString("warrantyFrom", startDate);
                    bundle.putString("warrantyTill", endDate);

                    Fragment selectedFragment = null;
                    selectedFragment = new EquipmentDetailsFragment();
                    //if(selectedFragment!=null) {
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
        TextView tvEquipemntName;
        ImageView ivImage;
        LinearLayout lyt_parent;
    }
}
