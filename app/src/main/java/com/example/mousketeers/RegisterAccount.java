package com.example.mousketeers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mousketeers.databinding.ActivityRegisterAccountBinding;
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

        binding.registerButton.setOnClickListener(v -> {
            if (validAccount()) {
                createAccount();
                Intent intent = new Intent(RegisterAccount.this, HomePageActivity.class);
                startActivity(intent);
            }
        });

        binding.textSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterAccount.this, LoginSignupActivity.class);
            startActivity(intent);
        });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

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

    private void createAccount() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Fetch the current maximum userID
        database.collection(Constants.KEY_COLLECTION_USERS)
                .orderBy(Constants.KEY_USER_ID, com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        // Default userID if no users exist
                        int newUserId = 1;
                        if (!task.getResult().isEmpty()) {
                            String lastUserId = task.getResult().getDocuments().get(0).getString(Constants.KEY_USER_ID);
                            try {
                                newUserId = Integer.parseInt(lastUserId) + 1;
                            } catch (NumberFormatException e) {
                                showToast("Error parsing userID");
                            }
                        }

                        // Create the user data
                        HashMap<String, Object> user = new HashMap<>();
                        user.put(Constants.KEY_USER_ID, String.valueOf(newUserId));
                        user.put(Constants.KEY_USERNAME, binding.inputUsername.getText().toString());
                        user.put(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
                        user.put(Constants.KEY_SCORE, 0);

                        // Initialize 'friends' as a List of Integers with 20 zeroes
                        List<Integer> friends = new ArrayList<>(Collections.nCopies(20, 0));
                        user.put(Constants.KEY_FRIENDS, friends);

                        // Add the user to Firestore
                        database.collection(Constants.KEY_COLLECTION_USERS)
                                .add(user)
                                .addOnSuccessListener(documentReference -> {
                                    pManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                    pManager.putString(Constants.KEY_USERNAME, binding.inputUsername.getText().toString());

                                    showToast("Account successfully created");

                                    // Navigate to HomePageActivity
                                    Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }).addOnFailureListener(exception -> {
                                    showToast("Failed to create account: " + exception.getMessage());
                                });
                    } else {
                        showToast("Failed to fetch userID");
                    }
                });
    }


}
