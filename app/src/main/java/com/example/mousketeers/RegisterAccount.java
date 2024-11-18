package com.example.mousketeers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mousketeers.databinding.ActivityLoginSignupBinding;
import com.example.mousketeers.databinding.ActivityRegisterAccountBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import Utilities.PreferenceManager;
import Utilities.Constants;

public class RegisterAccount extends AppCompatActivity {

    //View Binder
    private ActivityRegisterAccountBinding binding;
    //Preference Manager
    private PreferenceManager pManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialization of View and preference manager
        binding = ActivityRegisterAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pManager = new PreferenceManager(getApplicationContext());

        binding.registerButton.setOnClickListener(v -> {
            //if entry boxes have valid entries create account and redirect to Main Page
            if (validAccount()) {
                createAccount();
                Intent intent = new Intent(RegisterAccount.this, HomePageActivity.class);
                startActivity(intent);

            }
        });
        //go back to sign in page
        binding.textSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterAccount.this, LoginSignupActivity.class);
            startActivity(intent);
        });
    }
    //Method to show toast messages
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    //method to check valid entries
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
    //method to store information into the database
    private void createAccount() {

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, String> user = new HashMap<>();

        user.put(Constants.KEY_USERNAME, binding.inputUsername.getText().toString());
        user.put(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
        user.put(Constants.KEY_SCORE,"0");


        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {

                    pManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    pManager.putString(Constants.KEY_USERNAME, binding.inputUsername.getText().toString());

                    showToast("Account Successfully created");

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }).addOnFailureListener(exception -> {
                    showToast(exception.getMessage());

                });
    }
}