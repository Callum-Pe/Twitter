package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

public class Profile extends AppCompatActivity {

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

    }
}
