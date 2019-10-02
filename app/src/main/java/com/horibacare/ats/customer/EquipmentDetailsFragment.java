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

public class EquipmentDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView tvWeight, tvPrize, tvName, tvWarrantyFrom, tvWarrantyTill;
    ImageView ivImage;
    Button btn_view_existing_complaint,btn_service_call;

    public EquipmentDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EquipmentDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EquipmentDetailsFragment newInstance(String param1, String param2) {
        EquipmentDetailsFragment fragment = new EquipmentDetailsFragment();
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
        View view = inflater.inflate(R.layout.fragment_equipment_details, container, false);
        Config.screenNumber = 5;
        Bundle bundle = getArguments();
        final String id = bundle.getString("equipment_id");//id
        String title = bundle.getString("equipment_title");//title
        String desc = bundle.getString("equipmentDesc");
        String category = bundle.getString("category");
        String image = bundle.getString("image");
        String type = bundle.getString("type");
        String code = bundle.getString("code");
        String mfgNo = bundle.getString("mfgNo");
        String hsnNo = bundle.getString("hgsNo");
        String warrantyFrom = bundle.getString("warrantyFrom");
        String warrantyTill = bundle.getString("warrantyTill");

        tvName = (TextView) view.findViewById(R.id.tvProductName);
        tvWarrantyFrom = (TextView) view.findViewById(R.id.tvWarrantyFrom);
        tvWarrantyTill = (TextView) view.findViewById(R.id.tvWarrantyTill);
        ivImage = (ImageView) view.findViewById(R.id.ivImage);
        TextView tvDetails = (TextView) view.findViewById(R.id.tvDetails);

        btn_service_call = (Button) view.findViewById(R.id.btn_service_call);
        btn_view_existing_complaint = (Button) view.findViewById(R.id.btn_view_existing_complaint);

        tvName.setText(title);
        tvWarrantyFrom.setText(warrantyFrom);
        tvWarrantyTill.setText(warrantyTill);
        tvDetails.setText(desc);

        Glide.with(getActivity())
                .load(Config.IMAGE_URL + image)
                //.centerCrop()
                .placeholder(R.drawable.logo)
                .into(ivImage);

        btn_service_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("equipment_id", id);
              //  bundle.putString("title", item1.getBuiding_name());
              //  bundle.putString("title", item1.getBuiding_name());

                Fragment selectedFragment = null;
                selectedFragment = new ServiceCallFragment();
                // if(selectedFragment!=null) {
                selectedFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, selectedFragment);
                transaction.commit();
            }
        });

        btn_view_existing_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("equipment_id", id);
                //bundle.putString("title", item1.getBuiding_name());

                Fragment selectedFragment = null;
                selectedFragment = new ViewExistingComplaintsFragment();
                // if(selectedFragment!=null) {
                selectedFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, selectedFragment);
                transaction.commit();
            }
        });

        return view;
    }
}
