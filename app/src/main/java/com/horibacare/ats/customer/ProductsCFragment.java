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
import android.widget.Button;
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

public class ProductsCFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    GridView gridView;
    ArrayList<Item> alItem;
    PrefManager prefManager;
    String category = "EMS";
    TextView tvEms, tvMct, tvTas, tvTc;

    public ProductsCFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductsCFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductsCFragment newInstance(String param1, String param2) {
        ProductsCFragment fragment = new ProductsCFragment();
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
        View view = inflater.inflate(R.layout.fragment_products_c, container, false);
        Config.screenNumber = -1;
        prefManager = new PrefManager(getActivity());
        gridView = (GridView) view.findViewById(R.id.gvProducts);

        tvEms = (TextView) view.findViewById(R.id.tvEms);
        tvMct = (TextView) view.findViewById(R.id.tvMct);
        tvTas = (TextView) view.findViewById(R.id.tvTas);
        //tvTc = (TextView) view.findViewById(R.id.tvTc);

        ImageView ivEms = (ImageView) view.findViewById(R.id.ivEMS);
        ImageView ivMct = (ImageView) view.findViewById(R.id.ivMct);
        ImageView ivTas = (ImageView) view.findViewById(R.id.ivTas);
        //ImageView ivTc = (ImageView) view.findViewById(R.id.ivTc);


        //tvEms.setTextColor(getResources().getColor(R.color.colorAccent));

        ivEms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                tvEms.setTextColor(getResources().getColor(R.color.colorAccent));
//                tvMct.setTextColor(getResources().getColor(R.color.white));
//                tvTas.setTextColor(getResources().getColor(R.color.white));
//                tvTc.setTextColor(getResources().getColor(R.color.white));
//                category = "EMS";
//                loadProducts();

                Config.productCategory = "EMS";
                Bundle bundle = new Bundle();
                bundle.putString("category", "EMS");
                Fragment selectedFragment = null;
                selectedFragment = new ProductListFragment();
                if(selectedFragment!=null) {
                    selectedFragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, selectedFragment);
                    transaction.commit();
                }
            }
        });
        ivMct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                tvEms.setTextColor(getResources().getColor(R.color.white));
//                tvMct.setTextColor(getResources().getColor(R.color.colorAccent));
//                tvTas.setTextColor(getResources().getColor(R.color.white));
//                tvTc.setTextColor(getResources().getColor(R.color.white));
//                category = "MCT";
//                loadProducts();

                Config.productCategory = "MCT";
                Bundle bundle = new Bundle();
                bundle.putString("category", "MCT");
                Fragment selectedFragment = null;
                selectedFragment = new ProductListFragment();
                if(selectedFragment!=null) {
                    selectedFragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, selectedFragment);
                    transaction.commit();
                }
            }
        });
        ivTas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                tvEms.setTextColor(getResources().getColor(R.color.white));
//                tvMct.setTextColor(getResources().getColor(R.color.white));
//                tvTas.setTextColor(getResources().getColor(R.color.colorAccent));
//                tvTc.setTextColor(getResources().getColor(R.color.white));
//                category = "TAS";
//                loadProducts();

                Config.productCategory = "TAS";
                Bundle bundle = new Bundle();
                bundle.putString("category", "TAS");
                Fragment selectedFragment = null;
                selectedFragment = new ProductListFragment();
                if(selectedFragment!=null) {
                    selectedFragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, selectedFragment);
                    transaction.commit();
                }
            }
        });
//        ivTc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                tvEms.setTextColor(getResources().getColor(R.color.white));
////                tvMct.setTextColor(getResources().getColor(R.color.white));
////                tvTas.setTextColor(getResources().getColor(R.color.white));
////                tvTc.setTextColor(getResources().getColor(R.color.colorAccent));
////                category = "TC";
////                loadProducts();
//
//                Config.productCategory = "TC";
//                Bundle bundle = new Bundle();
//                bundle.putString("category", "TC");
//                Fragment selectedFragment = null;
//                selectedFragment = new ProductListFragment();
//                if(selectedFragment!=null) {
//                    selectedFragment.setArguments(bundle);
//                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                    transaction.replace(R.id.container, selectedFragment);
//                    transaction.commit();
//                }
//            }
//        });

        //loadProducts();

        return view;
    }

    public void loadProducts() {
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
                params.put("method", "getProducts");
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
                    selectedFragment = new ProductDetailsCFragment();
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
    }
}
