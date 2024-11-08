package com.example.mousketeers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class GlobalChatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_chat);

        Button homeButton = findViewById(R.id.chat_page_home_button);
        Button friendsButton = findViewById(R.id.chat_page_friends_button);
        Button leaderboardButton = findViewById(R.id.chat_page_leaderboard_button);
        Button shopButton = findViewById(R.id.chat_page_shop_button);

        homeButton.setOnClickListener(v -> startActivity(new Intent(GlobalChatActivity.this, HomePageActivity.class)));
        friendsButton.setOnClickListener(v -> startActivity(new Intent(GlobalChatActivity.this, FriendsActivity.class)));
        leaderboardButton.setOnClickListener(v -> startActivity(new Intent(GlobalChatActivity.this, LeaderboardActivity.class)));
        shopButton.setOnClickListener(v -> startActivity(new Intent(GlobalChatActivity.this, UpgradeShopActivity.class)));
    }
}