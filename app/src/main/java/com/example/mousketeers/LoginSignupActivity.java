package com.example.mousketeers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mousketeers.databinding.ActivityLoginSignupBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import Utilities.Constants;
import Utilities.PreferenceManager;
import Utilities.UserSession;

public class LoginSignupActivity extends AppCompatActivity {

    private ActivityLoginSignupBinding binding;
    private PreferenceManager pManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view and initialize preference manager
        binding = ActivityLoginSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pManager = new PreferenceManager(getApplicationContext());

        // Button click listeners
        binding.loginButton.setOnClickListener(v -> {
            // If text in entry boxes are valid, sign in and redirect to Home page if account exists
            if (validSignIn()) {
                signIn();
            }
        });

        // Redirect to the create account page
        binding.signupButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginSignupActivity.this, RegisterAccount.class);
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
     * A method to make sure the information in the entry boxes matches an entry in the firestore database
     */
    private void signIn() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_USERNAME, binding.usernameEditText.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, binding.passwordEditText.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);

                        // Retrieve and store the user's unique ID
                        int userId = Integer.parseInt(documentSnapshot.getString(Constants.KEY_USER_ID));
                        UserSession.getInstance().setUserId(userId);

                        // Save user session details in preferences
                        pManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        pManager.putString(Constants.KEY_USERNAME, documentSnapshot.getString(Constants.KEY_USERNAME));

                        showToast("Successful Login");

                        // Redirect to HomePageActivity
                        Intent intent = new Intent(LoginSignupActivity.this, HomePageActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        showToast("Unable to Login");
                    }
                });
    }

    /**
     * A method that ensures the username and password entry boxes are not empty
     * @return True or false depending on entry box emptiness
     */
    private boolean validSignIn() {
        if (binding.usernameEditText.getText().toString().isEmpty()) {
            showToast("Please enter your Username");
            return false;
        } else if (binding.passwordEditText.getText().toString().isEmpty()) {
            showToast("Please enter your Password");
            return false;
        } else {
            return true;
        }
    }
}
