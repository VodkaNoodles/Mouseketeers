package com.example.mousketeers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class FriendsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        Button homeButton = findViewById(R.id.friends_page_home_button);
        Button leaderboardButton = findViewById(R.id.friends_page_leaderboard_button);
        Button chatButton = findViewById(R.id.friends_page_chat_button);
        Button shopButton = findViewById(R.id.friends_page_shop_button);

        homeButton.setOnClickListener(v -> startActivity(new Intent(FriendsActivity.this, HomePageActivity.class)));
        leaderboardButton.setOnClickListener(v -> startActivity(new Intent(FriendsActivity.this, LeaderboardActivity.class)));
        chatButton.setOnClickListener(v -> startActivity(new Intent(FriendsActivity.this, GlobalChatActivity.class)));
        shopButton.setOnClickListener(v -> startActivity(new Intent(FriendsActivity.this, UpgradeShopActivity.class)));
    }
}