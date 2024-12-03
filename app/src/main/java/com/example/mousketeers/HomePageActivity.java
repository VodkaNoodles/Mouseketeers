package com.example.mousketeers;

import static com.example.mousketeers.UpgradeShopActivity.purchasedItem5;
import static com.example.mousketeers.UpgradeShopActivity.purchasedItem6;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

import Utilities.Constants;
import Utilities.UserSession;

public class HomePageActivity extends AppCompatActivity {

    static long clicks = 0;// static so this can be accessed by upgrade shop
    UpgradeShopActivity shop = new UpgradeShopActivity();
    private String userId;
    static long locScore;//static variable for the score

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        userId = String.valueOf(UserSession.getInstance().getUserId());
        getScore();

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
        String display = "Score: " + locScore;
        score.setText(display);
        if(purchasedItem5){
            clicker.setImageResource(R.drawable.mid_tier_icon);
        }else if(purchasedItem6){
            clicker.setImageResource(R.drawable.high_tier_icon);
        }

        clicker.setEnabled(true);

        clicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicks = UpgradeShopActivity.cheeseClick;
                locScore += clicks;
                String display = "Score: " + locScore;
                shop.updateCheese(userId,locScore);
                score.setText(display);
            }
        });
    }

    //updates locScore with the value stored in the database
    private void getScore(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user")
                .whereEqualTo("userID", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        if (document.contains("score")) {
                            locScore = document.getLong("score");;
                        } else {
                            Toast.makeText(this, "Score not found!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Score not found!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error fetching score.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });
    }

}