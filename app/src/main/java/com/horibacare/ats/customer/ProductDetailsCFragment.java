package com.horibacare.ats.customer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.horibacare.ats.Config;
import com.horibacare.ats.R;

public class ProductDetailsCFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView tvWeight, tvPrize, tvName, tvWarrantyFrom, tvWarrantyTill;
    ImageView ivImage;

    public ProductDetailsCFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductDetailsCFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductDetailsCFragment newInstance(String param1, String param2) {
        ProductDetailsCFragment fragment = new ProductDetailsCFragment();
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
        View view = inflater.inflate(R.layout.fragment_product_details_c, container, false);
        Config.screenNumber = 2;
        Bundle bundle = getArguments();
        String id = bundle.getString("id");
        String title = bundle.getString("title");
        String desc = bundle.getString("desc");
        String category = bundle.getString("category");
        String image = bundle.getString("image");
        String type = bundle.getString("type");
        String code = bundle.getString("code");
        String mfgNo = bundle.getString("mfgNo");
        String hsnNo = bundle.getString("hsnNo");
        String warrantyFrom = bundle.getString("warrantyFrom");
        String warrantyTill = bundle.getString("warrantyTill");

        tvName = (TextView) view.findViewById(R.id.tvProductName);
        tvWarrantyFrom = (TextView) view.findViewById(R.id.tvWarrantyFrom);
        tvWarrantyTill = (TextView) view.findViewById(R.id.tvWarrantyTill);
        ivImage = (ImageView) view.findViewById(R.id.ivImage);
        TextView tvDetails = (TextView) view.findViewById(R.id.tvDetails);

        tvName.setText(title);
        tvWarrantyFrom.setText(warrantyFrom);
        tvWarrantyTill.setText(warrantyTill);
        tvDetails.setText(desc);

        Glide.with(getActivity())
                .load(Config.IMAGE_URL + image)
                //.centerCrop()
                .placeholder(R.drawable.logo)
                .into(ivImage);

        return view;
    }
}
