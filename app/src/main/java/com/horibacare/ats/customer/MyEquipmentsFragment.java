package com.horibacare.ats.customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
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
import com.bumptech.glide.Glide;
import com.horibacare.ats.Config;
import com.horibacare.ats.PrefManager;
import com.horibacare.ats.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyEquipmentsFragment extends Fragment {
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
    public MyEquipmentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyEquipmentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyEquipmentsFragment newInstance(String param1, String param2) {
        MyEquipmentsFragment fragment = new MyEquipmentsFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_equipments, container, false);

        Config.screenNumber = -1;
        prefManager = new PrefManager(getActivity());
        gridView = (GridView) view.findViewById(R.id.gvProducts);

        loadProducts();
       /* animalList=new ArrayList<BuildingItem>();
        animalList.add(new BuildingItem("1","Building - B11"));
        animalList.add(new BuildingItem("2","Building - B12"));
        animalList.add(new BuildingItem("3","Building - B13"));
        animalList.add(new BuildingItem("4","Building - B14"));
        animalList.add(new BuildingItem("5","Building - B14"));
        animalList.add(new BuildingItem("6","Building - B15"));
        gridView.setAdapter(new ItemAdapter(getActivity(), animalList));*/
        return view;
    }

  /*  public void loadProducts() {
        alItem = new ArrayList<>();
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Loading", "Please Wait..", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.JSON_URL + "products.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("veer", "response" + response);
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("ack")) {
                        JSONArray ja = jsonObject.getJSONArray("result");
                        for (int i=0;i<ja.length();i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            Item item = new Item();
                            item.setId(jo.getString("id"));
                            item.setCode(jo.getString("code"));
                            item.setMfgNo(jo.getString("mfgNo"));
                            item.setHsnNo(jo.getString("hsnNo"));
                            item.setTitle(jo.getString("title"));
                            item.setDesc(jo.getString("description"));
                            item.setCategory(jo.getString("category"));
                            item.setType(jo.getString("type"));
                            item.setQty(jo.getString("quantity"));
                            item.setWarrantyFrom(jo.getString("warrantyFrom"));
                            item.setWarrantyTill(jo.getString("warrantyTill"));
                            item.setImage(jo.getString("image"));
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
                params.put("method", "getMyEquipments");
                params.put("imei", prefManager.getImei());
                params.put("fromAccount", prefManager.getUserId());
                params.put("category", category);
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

    class Item {
        String id, image, title, desc, category, type, qty, warrantyFrom, warrantyTill, code, mfgNo, hsnNo;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getWarrantyFrom() {
            return warrantyFrom;
        }

        public void setWarrantyFrom(String warrantyFrom) {
            this.warrantyFrom = warrantyFrom;
        }

        public String getWarrantyTill() {
            return warrantyTill;
        }

        public void setWarrantyTill(String warrantyTill) {
            this.warrantyTill = warrantyTill;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMfgNo() {
            return mfgNo;
        }

        public void setMfgNo(String mfgNo) {
            this.mfgNo = mfgNo;
        }

        public String getHsnNo() {
            return hsnNo;
        }

        public void setHsnNo(String hsnNo) {
            this.hsnNo = hsnNo;
        }
    }

    class ItemAdapter extends BaseAdapter {
        Context context;
        private LayoutInflater mInflater;
        private ArrayList<Item> arrayList;// = new ArrayList<Place>();

        public ItemAdapter(Context context, ArrayList<Item> arrayList) {
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
            final Item item = arrayList.get(position);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.list_product_grid, null);
                holder.tvProductName = (TextView) convertView.findViewById(R.id.tvProductName);
                holder.tvProductPrize = (TextView) convertView.findViewById(R.id.tvProductPrize);
                holder.tvProductWeight = (TextView) convertView.findViewById(R.id.tvProductWeight);
                holder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.ivImage.setId(position);

            holder.tvProductName.setText(item.getTitle());
            holder.tvProductPrize.setText("Rs. 120/-");
            holder.tvProductWeight.setText("150 G");
            //holder.etQty.setText(item.getQty());

            holder.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Item item1 = alItem.get(v.getId());
                    Bundle bundle = new Bundle();
                    bundle.putString("id", item1.getId());
                    bundle.putString("title", item1.getTitle());
                    bundle.putString("desc", item1.getDesc());
                    bundle.putString("category", item1.getCategory());
                    bundle.putString("type", item1.getType());
                    bundle.putString("code", item1.getCode());
                    bundle.putString("mfgNo", item1.getMfgNo());
                    bundle.putString("hsnNo", item1.getHsnNo());
                    bundle.putString("warrantyFrom", item1.getWarrantyFrom());
                    bundle.putString("warrantyTill", item1.getWarrantyTill());
                    bundle.putString("image", item1.getImage());
                    Fragment selectedFragment = null;
                    selectedFragment = new EquipmentDetailsFragment();
                    if(selectedFragment!=null) {
                        selectedFragment.setArguments(bundle);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, selectedFragment);
                        transaction.commit();
                    }
                }
            });

            Glide.with(getActivity())
                    .load(Config.IMAGE_URL + item.getImage())
                    //.centerCrop()
                    .placeholder(R.drawable.logo)
                    .into(holder.ivImage);

            return convertView;
        }
    }
    class ViewHolder {
        TextView tvProductName, tvProductPrize, tvProductWeight;
        ImageView ivImage;
        TextView etQty;
    }*/

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
                        JSONArray ja = jsonObject.getJSONArray("buildings");
                        for (int i=0;i<ja.length();i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            BuildingItem item = new BuildingItem();
                            item.setId(jo.getString("id"));
                            item.setBuiding_name(jo.getString("buildingCode"));

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
                params.put("method", "getBuildings");
               // params.put("imei", prefManager.getImei());
             //   params.put("fromAccount", prefManager.getUserId());
                params.put("userId", "prefManager.getUserId()");
               // params.put("userId", "3");
                //params.put("category", category);
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
        String id, Buiding_name;
      /*public BuildingItem(String id,String Buiding_name)
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

        public String getBuiding_name() {
            return Buiding_name;
        }

        public void setBuiding_name(String buiding_name) {
            Buiding_name = buiding_name;
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
                convertView = mInflater.inflate(R.layout.list_building_item, null);
                holder.tvBuildingName = (TextView) convertView.findViewById(R.id.tvBuildingName);
                holder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
                holder.lyt_parent= (LinearLayout) convertView.findViewById(R.id.lyt_parent);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.ivImage.setId(position);

            holder.tvBuildingName.setText("Building - "+item.getBuiding_name());

            holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // BuildingItem item1 = alItem.get(v.getId());
                    Bundle bundle = new Bundle();
                    bundle.putString("building_id", item.getId());
                    bundle.putString("building_code", item.getBuiding_name());

                    Fragment selectedFragment = null;
                    selectedFragment = new MyLabsFragments();
                    if(selectedFragment!=null) {
                        selectedFragment.setArguments(bundle);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, selectedFragment);
                        transaction.commit();
                    }
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
        TextView tvBuildingName;
        ImageView ivImage;
        LinearLayout lyt_parent;
    }
}
