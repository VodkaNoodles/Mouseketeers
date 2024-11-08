package com.example.mousketeers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LeaderboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        Button homeButton = findViewById(R.id.leaderboard_page_home_button);
        Button friendsButton = findViewById(R.id.leaderboard_page_friends_button);
        Button chatButton = findViewById(R.id.leaderboard_page_chat_button);
        Button shopButton = findViewById(R.id.leaderboard_page_shop_button);

        homeButton.setOnClickListener(v -> startActivity(new Intent(LeaderboardActivity.this, HomePageActivity.class)));
        friendsButton.setOnClickListener(v -> startActivity(new Intent(LeaderboardActivity.this, FriendsActivity.class)));
        chatButton.setOnClickListener(v -> startActivity(new Intent(LeaderboardActivity.this, GlobalChatActivity.class)));
        shopButton.setOnClickListener(v -> startActivity(new Intent(LeaderboardActivity.this, UpgradeShopActivity.class)));
    }
}