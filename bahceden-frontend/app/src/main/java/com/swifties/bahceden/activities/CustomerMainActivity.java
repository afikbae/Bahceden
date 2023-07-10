package com.swifties.bahceden.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.swifties.bahceden.R;
import com.swifties.bahceden.data.AuthUser;
import com.swifties.bahceden.fragments.CustomerCartFragment;
import com.swifties.bahceden.fragments.CustomerFavoritesFragment;
import com.swifties.bahceden.fragments.CustomerHomeFragment;
import com.swifties.bahceden.fragments.CustomerProfileFragment;
import com.swifties.bahceden.fragments.CustomerSearchFragment;

public class CustomerMainActivity extends AppCompatActivity {

    BottomNavigationView customerBottomNav;
    CustomerHomeFragment customerHomeFragment;
    CustomerSearchFragment customerSearchFragment;
    CustomerFavoritesFragment customerFavoritesFragment;
    CustomerProfileFragment customerProfileFragment;
    CustomerCartFragment customerCartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        customerBottomNav = findViewById(R.id.customerBottomNavBar);

        customerHomeFragment = new CustomerHomeFragment();
        customerSearchFragment = new CustomerSearchFragment();
        customerFavoritesFragment = new CustomerFavoritesFragment();
        customerProfileFragment = new CustomerProfileFragment();
        customerCartFragment = new CustomerCartFragment();

//        Set HomeFragment as main fragment
        replaceFragment(customerHomeFragment);

//        Set listener for navBar
        customerBottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.customerHome:
                        replaceFragment(customerHomeFragment);
                        return true;

                    case R.id.customerSearch:
                        replaceFragment(customerSearchFragment);
                        return true;

                    case R.id.customerCart:
                        replaceFragment(customerCartFragment);
                        return true;

                    case R.id.customerFavorites:
                        replaceFragment(customerFavoritesFragment);
                        return true;

                    case R.id.customerProfile:
                        replaceFragment(customerProfileFragment);
                        return true;
                }

                return false;
            }
        });

        if (AuthUser.getCustomer().getAddresses() == null || AuthUser.getCustomer().getAddresses().isEmpty())
        {
            Toast.makeText(this, "You need to fill the required information to continue.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, CustomerAddAddressActivity.class);
            intent.putExtra("signup", true);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AuthUser.getCustomer().getAddresses() == null || AuthUser.getCustomer().getAddresses().isEmpty())
        {
            Toast.makeText(this, "You need to fill the required information to continue.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, CustomerAddAddressActivity.class);
            intent.putExtra("signup", true);
            startActivity(intent);
        }
    }

    // This is here to prevent the user from pressing the back button
    // on their phone, as that would redirect them to the login page
    @Override
    public void onBackPressed() {

    }

    //    Helper method to replace fragments
    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.customerMainFragmentContainer, fragment).commit();
    }
}