package com.horibacare.ats.customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
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

public class SparePartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListView gridView;
    ArrayList<Item> alItem, alItemCopy;
    PrefManager prefManager;
    TextView tvQty;
    EditText etSearch;

    public SparePartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SparePartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SparePartFragment newInstance(String param1, String param2) {
        SparePartFragment fragment = new SparePartFragment();
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
        View view = inflater.inflate(R.layout.fragment_spare_part, container, false);
        Config.screenNumber = -1;
        prefManager = new PrefManager(getActivity());
        gridView = (ListView) view.findViewById(R.id.gvSparePart);
        etSearch = (EditText) view.findViewById(R.id.etSearch);

        TextWatcher tw = new TextWatcher() {
            public void afterTextChanged(Editable s){
                alItemCopy = new ArrayList<>();
                String searchString = etSearch.getText().toString().toLowerCase().trim();
                if(searchString.length()>0) {
                    for(int i=0;i<alItem.size();i++) {
                        if(alItem.get(i).getTitle().toLowerCase().contains(searchString) || alItem.get(i).getSapId().toLowerCase().contains(searchString)) {
                            alItemCopy.add(alItem.get(i));
                        }
                    }
                    gridView.setAdapter(new ItemAdapter(getActivity(), alItemCopy));
                } else {
                    gridView.setAdapter(new ItemAdapter(getActivity(), alItem));
                }
            }
            public void  beforeTextChanged(CharSequence s, int start, int count, int after){
                // you can check for enter key here
            }
            public void  onTextChanged (CharSequence s, int start, int before,int count) {
            }
        };
        etSearch.addTextChangedListener(tw);

        loadProducts();
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
                            item.setTitle(jo.getString("name"));
                            item.setDesc(jo.getString("description"));
                            item.setQty(jo.getString("quantity"));
                            item.setImage(jo.getString("image"));
                            item.setSapId(jo.getString("sapId"));
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
                params.put("method", "getSparePart");
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

    class Item {
        String id, image, title, desc, qty, sapId;

        public String getSapId() {
            return sapId;
        }

        public void setSapId(String sapId) {
            this.sapId = sapId;
        }

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

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
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
                convertView = mInflater.inflate(R.layout.list_sparepart_item, null);
                holder.tvTitle= (TextView) convertView.findViewById(R.id.tvTitle);
                holder.tvDesc= (TextView) convertView.findViewById(R.id.tvDesc);
                holder.tvQty = (TextView) convertView.findViewById(R.id.tvQty);
                holder.ivImage = (ImageView) convertView.findViewById(R.id.ivSparepart);
                holder.tvSapId = (TextView) convertView.findViewById(R.id.tvSapId);
                //holder.tvUnit = (TextView) convertView.findViewById(R.id.tvUnit);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.ivImage.setId(position);
            holder.tvTitle.setText(item.getTitle());
            holder.tvDesc.setText(item.getDesc());
            holder.tvQty.setText("" + item.getQty());
            holder.tvSapId.setText(item.getSapId() + "");

            Glide.with(getActivity())
                    .load(Config.IMAGE_URL + item.getImage())
                    //.centerCrop()
                    .placeholder(R.drawable.logo)
                    .into(holder.ivImage);

            return convertView;
        }
    }
    class ViewHolder {
        TextView tvTitle, tvDesc, tvQty, tvSapId, tvUnit;
        ImageView ivImage;
    }
}
