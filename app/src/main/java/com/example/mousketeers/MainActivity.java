package com.example.mousketeers;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Redirect to LoginSignupActivity
        Intent intent = new Intent(MainActivity.this, LoginSignupActivity.class);
        startActivity(intent);
        finish(); // Optional: close MainActivity from back stack
    }
}