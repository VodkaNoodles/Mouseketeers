package com.example.mousketeers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class UpgradeShopActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_shop);

        Button homeButton = findViewById(R.id.shop_page_home_button);
        Button friendsButton = findViewById(R.id.shop_page_friends_button);
        Button leaderboardButton = findViewById(R.id.shop_page_leaderboard_button);
        Button chatButton = findViewById(R.id.shop_page_chat_button);

        homeButton.setOnClickListener(v -> startActivity(new Intent(UpgradeShopActivity.this, HomePageActivity.class)));
        friendsButton.setOnClickListener(v -> startActivity(new Intent(UpgradeShopActivity.this, FriendsActivity.class)));
        leaderboardButton.setOnClickListener(v -> startActivity(new Intent(UpgradeShopActivity.this, LeaderboardActivity.class)));
        chatButton.setOnClickListener(v -> startActivity(new Intent(UpgradeShopActivity.this, GlobalChatActivity.class)));
    }
}