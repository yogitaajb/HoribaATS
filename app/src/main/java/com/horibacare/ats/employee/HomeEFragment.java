package com.horibacare.ats.employee;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.horibacare.ats.Config;
import com.horibacare.ats.PrefManager;
import com.horibacare.ats.R;
import com.horibacare.ats.customer.ContactUsFragment;
import com.horibacare.ats.customer.InformationFragment;
import com.horibacare.ats.customer.NewsListCFragment;
import com.horibacare.ats.customer.ProductsCFragment;
import com.horibacare.ats.customer.SparePartFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeEFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    PrefManager prefManager;
    GridView gridView;
    TextView tvName, tvGreeting;

    int imgs[] = {R.drawable.products, R.drawable.information, R.drawable.equipment, R.drawable.news, R.drawable.spareparts, R.drawable.spareparts};
    String titles[] = {"Products", "Information", "My Equipments", "News", "Spare Parts", "Contact Us"};

    int imgsEmp[] = {R.drawable.products, R.drawable.information, R.drawable.support, R.drawable.news, R.drawable.spareparts, R.drawable.spareparts};
    String titlesEmp[] = {"Products", "Information", "Service Calls", "News", "Spare Parts", "Contact Us"};
    ArrayList<Item> alItems;

    public HomeEFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeEFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeEFragment newInstance(String param1, String param2) {
        HomeEFragment fragment = new HomeEFragment();
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
        View view = inflater.inflate(R.layout.fragment_home_e, container, false);

        Config.screenNumber = 0;
        prefManager = new PrefManager(getActivity());

        tvName = (TextView) view.findViewById(R.id.tvDrawerName);
        tvGreeting = (TextView) view.findViewById(R.id.tvDrawerGreeting);
        gridView = (GridView) view.findViewById(R.id.gvHome);

        tvName.setText(prefManager.getName());

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if(timeOfDay >= 0 && timeOfDay < 12){
            tvGreeting.setText("Good Morning");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            tvGreeting.setText("Good Afternoon");
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            tvGreeting.setText("Good Evening");
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            tvGreeting.setText("Good Night");
        }

        String userType = prefManager.getUserType();
        if(userType.trim().equalsIgnoreCase("customer")) {
            alItems = new ArrayList<>();
            for(int i=0;i<titles.length;i++) {
                Item obj = new Item();
                obj.setImgId(imgs[i]);
                obj.setTitle(titles[i]);
                alItems.add(obj);
            }
            ItemAdapter groupAdapter = new ItemAdapter(getActivity(), alItems);
            gridView.setAdapter(groupAdapter);
        } else if(userType.trim().equalsIgnoreCase("horiba")) {
            alItems = new ArrayList<>();
            for(int i=0;i<titlesEmp.length;i++) {
                Item obj = new Item();
                obj.setImgId(imgsEmp[i]);
                obj.setTitle(titlesEmp[i]);
                alItems.add(obj);
            }
            ItemAdapter groupAdapter = new ItemAdapter(getActivity(), alItems);
            gridView.setAdapter(groupAdapter);
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userType = prefManager.getUserType();
                if(userType.trim().equalsIgnoreCase("customer")) {
                    navigateCustomer(position);
                } else if(userType.trim().equalsIgnoreCase("horiba")) {
                    navigateEmp(position);
                }
            }
        });
        return view;
    }

    public void navigateCustomer(int position) {
        Fragment selectedFragment = null;
        switch (position) {
            case 0 :
                selectedFragment = new ProductsCFragment();
                break;
            case 1 :
                selectedFragment = new InformationFragment();
                break;
            case 2 :
                //selectedFragment = new MyEquipmentsFragment();
                break;
            case 3 :
                selectedFragment = new NewsListCFragment();
                break;
            case 4 :
                selectedFragment = new SparePartFragment();
                break;
            case 5 :
                selectedFragment = new ContactUsFragment();
                break;
        }
        if(selectedFragment!=null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, selectedFragment);
            transaction.commit();
        }
    }
    public void navigateEmp(int position) {
        Fragment selectedFragment = null;
        switch (position) {
            case 0 :
                selectedFragment = new ProductsCFragment();
                break;
            case 1 :
                selectedFragment = new InformationFragment();
                break;
            case 2 :
                Toast.makeText(getActivity(), "Coming Soon", Toast.LENGTH_LONG).show();
                break;
            case 3 :
                selectedFragment = new NewsListCFragment();
                break;
            case 4 :
                selectedFragment = new SparePartFragment();
                break;
            case 5 :
                selectedFragment = new ContactUsFragment();
                break;
        }
        if(selectedFragment!=null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, selectedFragment);
            transaction.commit();
        }
    }

    class Item {
        int imgId;
        String title;

        public int getImgId() {
            return imgId;
        }

        public String getTitle() {
            return title;
        }

        public void setImgId(int imgId) {
            this.imgId = imgId;
        }

        public void setTitle(String title) {
            this.title = title;
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
            final Item group = arrayList.get(position);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.list_grid_item, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.tvGridItem);
                holder.imgPath = (ImageView) convertView.findViewById(R.id.ivGridItem);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvName.setText(group.getTitle());
            holder.imgPath.setImageResource(group.getImgId());

            /*Glide.with(getActivity())
                    .load(group.getPath())
                    .error(R.drawable.tree2)
                    .into(holder.imgPath);*/

            return convertView;
        }
    }

    class ViewHolder {
        TextView tvName;
        ImageView imgPath;
    }
}
