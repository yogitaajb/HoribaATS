package com.horibacare.ats.employee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.horibacare.ats.Config;
import com.horibacare.ats.LoginActivity;
import com.horibacare.ats.PrefManager;
import com.horibacare.ats.R;
import com.horibacare.ats.customer.HomeCFragment;
import com.horibacare.ats.customer.NewsListCFragment;
import com.horibacare.ats.customer.ProductsCFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class EmployeeActivity extends AppCompatActivity {

    PrefManager prefManager;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        prefManager = new PrefManager(this);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        bottomNavigationView.setItemIconTintList(null);   //show original color of icons

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        selectedFragment = new HomeCFragment();
                        break;
                    case R.id.navigation_catalogue:
                        selectedFragment = new ProductsCFragment();
                        break;
                    case R.id.navigation_orders:
                        selectedFragment = new NewsListCFragment();
                        break;
                    case R.id.navigation_offers:
                        Toast.makeText(getApplicationContext(), "Coming soon",Toast.LENGTH_SHORT).show();
                        break;
                }
                if(selectedFragment!=null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, selectedFragment);
                    transaction.commit();
                }
                return true;
            }
        });
//
        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new HomeCFragment());
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//            }
//        }, 2000);

        String title = getTitle().toString();
        Fragment selectedFragment = null;
        switch (Config.screenNumber) {
            case 0 :
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
                //selectedFragment = new HomeFragment();
                break;
            case 1 :
                //NewsDetailsCFragment
                selectedFragment = new ProductsCFragment();
                break;
            case 2 :
                //NewsDetailsCFragment
                selectedFragment = new NewsListCFragment();
                break;
            default :
                selectedFragment = new HomeCFragment();
        }
        if(selectedFragment!=null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, selectedFragment);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        //MenuItem itemBalance = menu.findItem(R.id.action_balance);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_logout) {
            prefManager.clearPreference();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
