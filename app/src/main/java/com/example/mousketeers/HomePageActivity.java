package com.example.mousketeers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HomePageActivity extends AppCompatActivity {
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
    }
}