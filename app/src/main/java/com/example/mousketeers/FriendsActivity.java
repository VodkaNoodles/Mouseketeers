package com.example.mousketeers;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import Utilities.UserSession;

public class FriendsActivity extends AppCompatActivity {
    private LinearLayout friendsListContainer;
    private EditText searchFriendEditText;
    private List<Long> friendsList;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // View references
        friendsListContainer = findViewById(R.id.friends_list_container);
        ImageButton addFriendButton = findViewById(R.id.add_friend_button);
        LinearLayout searchContainer = findViewById(R.id.search_container);
        searchFriendEditText = findViewById(R.id.search_friend_edit_text);
        Button searchFriendButton = findViewById(R.id.search_friend_button);

        // Get the current user's ID
        userId = String.valueOf(UserSession.getInstance().getUserId());

        // Initialize the friends list
        friendsList = new ArrayList<>();
        loadUserFriends();

        // Show or hide the search bar when the add friend button is clicked
        addFriendButton.setOnClickListener(v -> {
            if (searchContainer.getVisibility() == View.GONE) {
                searchContainer.setVisibility(View.VISIBLE);
            } else {
                searchContainer.setVisibility(View.GONE);
            }
        });

        // Set search button click listener
        searchFriendButton.setOnClickListener(v -> {
            String friendUsername = searchFriendEditText.getText().toString().trim();
            if (!friendUsername.isEmpty()) {
                searchFriend(friendUsername);
            } else {
                Toast.makeText(this, "Please enter a username!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserFriends() {
        db.collection("user").document(userId).get()
                .addOnSuccessListener(document -> {
                    if (document.exists() && document.contains("friends")) {
                        friendsList = (List<Long>) document.get("friends");
                        if (friendsList == null) friendsList = new ArrayList<>();
                        displayFriends();
                    } else {
                        Toast.makeText(this, "No friends found!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error loading friends.", Toast.LENGTH_SHORT).show());
    }

    private void displayFriends() {
        friendsListContainer.removeAllViews();

        for (Long friendId : friendsList) {
            if (friendId == null || friendId == 0) continue;

            db.collection("user").whereEqualTo("userID", String.valueOf(friendId)).get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (!querySnapshot.isEmpty()) {
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            String username = document.getString("username");
                            Long score = document.getLong("score");

                            View friendView = getLayoutInflater().inflate(R.layout.friend_item, friendsListContainer, false);

                            TextView nameTextView = friendView.findViewById(R.id.friend_name);
                            TextView scoreTextView = friendView.findViewById(R.id.friend_score);

                            nameTextView.setText(username);
                            scoreTextView.setText(String.format("%d cheese", score));

                            friendsListContainer.addView(friendView);
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error loading friend details.", Toast.LENGTH_SHORT).show());
        }
    }

    private void searchFriend(String username) {
        db.collection("user").whereEqualTo("username", username).get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot friendDocument = querySnapshot.getDocuments().get(0);
                        String friendUserIdString = friendDocument.getString("userID");

                        if (friendUserIdString == null || friendUserIdString.isEmpty()) {
                            Toast.makeText(this, "Invalid user ID.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Long friendUserId;
                        try {
                            friendUserId = Long.parseLong(friendUserIdString);
                        } catch (NumberFormatException e) {
                            Toast.makeText(this, "User ID is not a valid number.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (friendsList.contains(friendUserId)) {
                            Toast.makeText(this, "Friend already added!", Toast.LENGTH_SHORT).show();
                        } else if (friendsList.size() >= 20 && !friendsList.contains(0L)) {
                            Toast.makeText(this, "Friend list is full!", Toast.LENGTH_SHORT).show();
                        } else {
                            for (int i = 0; i < friendsList.size(); i++) {
                                if (friendsList.get(i) == 0) {
                                    friendsList.set(i, friendUserId);
                                    break;
                                }
                            }
                            updateFriendsInDatabase();
                            displayFriends();
                        }
                    } else {
                        Toast.makeText(this, "No user found with that username.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error searching for user.", Toast.LENGTH_SHORT).show());
    }



    private void updateFriendsInDatabase() {
        db.collection("user").document(userId)
                .update("friends", friendsList)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Friend added!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error updating friends.", Toast.LENGTH_SHORT).show());
    }
}
