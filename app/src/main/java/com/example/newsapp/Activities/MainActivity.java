package com.example.newsapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.newsapp.Fragments.MainFragment;
import com.example.newsapp.Activities.Login;
import com.example.newsapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView footer;
    private NavigationView sidebar;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TextView username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.main_view);
        toolbar = findViewById(R.id.toolbar);
        sidebar = findViewById(R.id.sidebar);
        if (findViewById(R.id.main_view) != null) {

            if (savedInstanceState != null) {
                setSupportActionBar(toolbar);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.sidebar_open,R.string.sidebar_close);
                drawerLayout.addDrawerListener(toggle);
                toggle.syncState();
                sidebar.setNavigationItemSelectedListener(this);
                return;
            }
                getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_container, new MainFragment(), "main_fragment").commit();

        }

        sidebar.setNavigationItemSelectedListener(this);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.sidebar_open,R.string.sidebar_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        footer = findViewById(R.id.footer);
        footer.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.google:
                        Intent intent1 = getPackageManager().getLaunchIntentForPackage("com.android.chrome");
                        startActivity(intent1);
                        break;
                    case R.id.youtube:
                        Intent intent2 = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
                        startActivity(intent2);
                        break;
                    case R.id.gmail:
                        Intent intent3 = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                        startActivity(intent3);
                        break;

                }

                return false;
            }
        });
        updateUser();

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout_view:
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void updateUser(){
        sidebar = findViewById(R.id.sidebar);
        View header = sidebar.getHeaderView(0);
        username = header.findViewById(R.id.name);
        String user = getIntent().getStringExtra("name");
        username.setText(user);
    }

}