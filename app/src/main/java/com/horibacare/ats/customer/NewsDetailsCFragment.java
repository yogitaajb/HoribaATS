package com.horibacare.ats.customer;

import android.app.AlertDialog;
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

public class NewsDetailsCFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView tvTitle, tvDesc, tvDate;
    ImageView ivImage, ivImage1;
    String path, path1;

    public NewsDetailsCFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsDetailsCFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsDetailsCFragment newInstance(String param1, String param2) {
        NewsDetailsCFragment fragment = new NewsDetailsCFragment();
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
        View view = inflater.inflate(R.layout.fragment_news_details_c, container, false);
        Config.screenNumber = 3;
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        tvDesc = (TextView) view.findViewById(R.id.tvDesc);
        ivImage = (ImageView) view.findViewById(R.id.ivImage);
        ivImage1 = (ImageView) view.findViewById(R.id.ivImage1);

        Bundle bundle = getArguments();

        tvTitle.setText(bundle.getString("title") + "");
        tvDate.setText(bundle.getString("date") + "");
        tvDesc.setText(bundle.getString("desc") + "");
        path = bundle.getString("image1") + "";
        path1 = bundle.getString("image2") + "";

        if(path1.trim().length()>0)
            ivImage1.setVisibility(View.VISIBLE);

        Glide.with(getActivity())
                    .load(Config.IMAGE_URL + path)
                    .error(R.drawable.logo)
                    .into(ivImage);

        Glide.with(getActivity())
                .load(Config.IMAGE_URL + path1)
                .error(R.drawable.logo)
                .into(ivImage1);

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.iv, null);
                dialogBuilder.setView(dialogView);
                ImageView imageView = (ImageView) dialogView.findViewById(R.id.iv);

                Glide.with(getActivity())
                        .load(Config.IMAGE_URL + path)
                        .error(R.drawable.logo)
                        .into(imageView);

                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });

        ivImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.iv, null);
                dialogBuilder.setView(dialogView);
                ImageView imageView = (ImageView) dialogView.findViewById(R.id.iv);

                Glide.with(getActivity())
                        .load(Config.IMAGE_URL + path1)
                        .error(R.drawable.logo)
                        .into(imageView);

                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });

        return view;
    }
}
