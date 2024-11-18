package com.example.mousketeers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomePageActivity extends AppCompatActivity {

    int clicks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Button friendsButton = findViewById(R.id.home_page_friends_button);
        Button leaderboardButton = findViewById(R.id.home_page_leaderboard_button);
        Button chatButton = findViewById(R.id.home_page_chat_button);
        Button shopButton = findViewById(R.id.home_page_shop_button);

        friendsButton.setOnClickListener(v -> startActivity(new Intent(HomePageActivity.this, FriendsActivity.class)));
        leaderboardButton.setOnClickListener(v -> startActivity(new Intent(HomePageActivity.this, LeaderboardActivity.class)));
        chatButton.setOnClickListener(v -> startActivity(new Intent(HomePageActivity.this, GlobalChatActivity.class)));
        shopButton.setOnClickListener(v -> startActivity(new Intent(HomePageActivity.this, UpgradeShopActivity.class)));

        TextView score = (TextView)findViewById(R.id.TextScore);
        ImageButton clicker = (ImageButton)findViewById(R.id.imageButton);
        clicker.setEnabled(true);

        clicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicks++;
                String display = "Score: " + clicks;
                score.setText(display);
            }
        });
    }
}