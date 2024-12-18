package com.example.mousketeers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mousketeers.models.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;
import java.util.Map;

import Utilities.UserSession;

/**
 * Activity to display the leaderboard showing the top user scores.
 * Provides navigation to other app sections and dynamically updates
 * leaderboard data from Firebase Firestore.
 */
public class LeaderboardActivity extends AppCompatActivity {

    private String userId;
    private static final String TAG = "LeaderboardActivity";
    private LinearLayout leaderboardContainer;

    /**
     * Called when the activity is created.
     * Initializes UI components, sets up navigation buttons, and loads leaderboard data.
     *
     * @param savedInstanceState the saved instance state bundle, if available.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        userId = String.valueOf(UserSession.getInstance().getUserId());

        leaderboardContainer = findViewById(R.id.leaderboard_list_container);

        // Navigation Buttons
        Button homeButton = findViewById(R.id.leaderboard_page_home_button);
        Button friendsButton = findViewById(R.id.leaderboard_page_friends_button);
        Button chatButton = findViewById(R.id.leaderboard_page_chat_button);
        Button shopButton = findViewById(R.id.leaderboard_page_shop_button);

        homeButton.setOnClickListener(v -> startActivity(new Intent(LeaderboardActivity.this, HomePageActivity.class)));
        friendsButton.setOnClickListener(v -> startActivity(new Intent(LeaderboardActivity.this, FriendsActivity.class)));
        chatButton.setOnClickListener(v -> startActivity(new Intent(LeaderboardActivity.this, GlobalChatActivity.class)));
        shopButton.setOnClickListener(v -> startActivity(new Intent(LeaderboardActivity.this, UpgradeShopActivity.class)));

        // Load leaderboard data
        loadLeaderboard();
    }

    /**
     * Fetches leaderboard data from Firebase Firestore and updates the UI.
     */
    private void loadLeaderboard() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("user")
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Deserialize into User objects
                    List<User> users = queryDocumentSnapshots.toObjects(User.class);
                    updateLeaderboard(users);
                })
                .addOnFailureListener(e -> {
                    Log.e("Leaderboard", "Error fetching leaderboard data", e);
                });
    }

    /**
     * Updates the leaderboard UI with the given list of users.
     *
     * @param users the list of top users to display on the leaderboard.
     */
    private void updateLeaderboard(List<User> users) {
        // Clear existing data
        leaderboardContainer.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            String username = user.getUsername();
            String score = String.valueOf(user.getScore());

            // Inflate a leaderboard item dynamically
            LinearLayout leaderboardItem = (LinearLayout) inflater.inflate(R.layout.leaderboard_item, leaderboardContainer, false);

            // Set user details
            TextView rankTextView = leaderboardItem.findViewById(R.id.leaderboard_rank);
            TextView nameTextView = leaderboardItem.findViewById(R.id.leaderboard_name);
            TextView scoreTextView = leaderboardItem.findViewById(R.id.leaderboard_score);

            rankTextView.setText(String.format("#%d", i + 1));
            nameTextView.setText(username);
            scoreTextView.setText(score);

            // Add the item to the leaderboard container
            leaderboardContainer.addView(leaderboardItem);
        }
    }
}