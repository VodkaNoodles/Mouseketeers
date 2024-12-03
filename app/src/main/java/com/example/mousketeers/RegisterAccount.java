package com.example.mousketeers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mousketeers.databinding.ActivityRegisterAccountBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import Utilities.PreferenceManager;
import Utilities.Constants;

public class RegisterAccount extends AppCompatActivity {

    private ActivityRegisterAccountBinding binding;
    private PreferenceManager pManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pManager = new PreferenceManager(getApplicationContext());

        //onclick listener that calls create account and redirects to login page on a valid account
        binding.registerButton.setOnClickListener(v -> {
            if (validAccount()) {
                createAccount();
                Intent intent = new Intent(RegisterAccount.this, LoginSignupActivity.class);
                startActivity(intent);
            }
        });
        //onclicklistener for the login text to return to the sign in page.
        binding.textSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterAccount.this, LoginSignupActivity.class);
            startActivity(intent);
        });
    }

    /**
     * A method to display a toast message.
     * @param message The message that will be displayed in the toast popup.
     */
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * A method that checks the entry boxes to ensure the user has input valid information.
     * It checks to make sure the username entry box is not empty.
     * It does the same for the password and confirm password entry boxes.
     * An additional check is done to make sure the password and confirm password entry boxes match.
     * @return A boolean value that indicates if the account is valid.
     */
    private boolean validAccount() {
        if (binding.inputUsername.getText().toString().isEmpty()) {
            showToast("Please enter your Username");
            return false;
        } else if (binding.inputPassword.getText().toString().isEmpty()) {
            showToast("Please enter your Password");
            return false;
        } else if (binding.inputConfirmPassword.getText().toString().isEmpty()) {
            showToast("Please confirm your Password");
            return false;
        }
        if (!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())) {
            showToast("Password and Confirm Password must be the same");
            return false;
        } else {
            return true;
        }
    }

    /**
     * A method that creates a user account with the text input into the entry boxes.
     * The method creates 2 hashmaps one for the user collection storing basic user information and the score of the player.
     * The other hashmap stores the unique userID and initializes the upgrades collection with the minimum value of upgrades.
     * Afterwards it stores the hashmaps into their respective collections in out firebase database
     */
    private void createAccount() {
        // Initialize FirebaseFirestore to store user and upgrade information in the database
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        largestID(database, maxId -> {
            int newUserId = maxId + 1;

            // Create the user data
            HashMap<String, Object> user = new HashMap<>();
            user.put(Constants.KEY_USER_ID, String.valueOf(newUserId));
            user.put(Constants.KEY_USERNAME, binding.inputUsername.getText().toString());
            user.put(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
            user.put(Constants.KEY_SCORE, 0);

            // Create and initialize data for the upgrades collection of a user
            HashMap<String, Object> upgrades = new HashMap<>();
            upgrades.put(Constants.KEY_USER_ID, String.valueOf(newUserId));
            upgrades.put(Constants.KEY_UPGRADE_1, 0);
            upgrades.put(Constants.KEY_UPGRADE_2, 0);
            upgrades.put(Constants.KEY_UPGRADE_3, 0);
            upgrades.put(Constants.KEY_UPGRADE_4, 0);
            upgrades.put(Constants.KEY_ICON_1, false);
            upgrades.put(Constants.KEY_ICON_2, false);

            // Add upgrades information to the database for new user
            database.collection(Constants.KEY_COLLECTION_UPGRADES)
                    .add(upgrades)
                    .addOnFailureListener(exception -> showToast("Failure initializing upgrades: " + exception.getMessage()));

            // Initialize 'friends' as a List of Integers with 20 zeroes
            List<Integer> friends = new ArrayList<>(Collections.nCopies(20, 0));
            user.put(Constants.KEY_FRIENDS, friends);

            // Add the user to Firestore
            database.collection(Constants.KEY_COLLECTION_USERS)
                    .add(user)
                    .addOnSuccessListener(documentReference -> {
                        showToast("Account successfully created");

                        // Navigate to HomePageActivity
                        Intent intent = new Intent(RegisterAccount.this, LoginSignupActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .addOnFailureListener(exception -> showToast("Failed to create account: " + exception.getMessage()));
        });
    }

    private void largestID(FirebaseFirestore database, OnMaxIdFetchedListener callback) {
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        int maxId = 0;
                        for (DocumentSnapshot document : task.getResult()) {
                            String userIdString = document.getString(Constants.KEY_USER_ID);
                            if (userIdString != null) {
                                try {
                                    int userId = Integer.parseInt(userIdString);
                                    if (userId > maxId) {
                                        maxId = userId;
                                    }
                                } catch (NumberFormatException e) {
                                    showToast("Error parsing userID: " + userIdString);
                                }
                            }
                        }
                        callback.onMaxIdFetched(maxId);
                    } else {
                        showToast("Error fetching userIDs");
                        callback.onMaxIdFetched(0); // Default to 0 if error
                    }
                });
    }

    // Listener interface for asynchronous max ID fetching
    private interface OnMaxIdFetchedListener {
        void onMaxIdFetched(int maxId);
    }
}
