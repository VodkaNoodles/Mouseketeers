package com.example.mousketeers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import Utilities.UserSession;

public class UpgradeShopActivity extends AppCompatActivity {

    private String userId;
    String display = "placeholder";

    // Initial prices for items

    final private int ITEM1_BASEPRICE = 10;
    final private int ITEM2_BASEPRICE = 20;
    final private int ITEM3_BASEPRICE = 30;
    final private int ITEM4_BASEPRICE = 40;
    final private int ITEM5_BASEPRICE = 50;
    final private int ITEM6_BASEPRICE = 60;

    // Counters for each item
     static long item1Count;

    static long item2Count;
    static long item3Count;
     static long item4Count;
     static long  item5Count;
     static long item6Count;
    static boolean purchasedItem5 = false;
     static boolean purchasedItem6 = false;

    final private double PERCENT_INC1 = 1.10;
    final private double PERCENT_INC2 = 1.15;
    final private double PERCENT_INC3 = 1.20;
    final private double PERCENT_INC4 = 1.25;
    //SAMPLE CHEESE TO CHECK 'IF ENOUGH CHEESE' FUNCTION
    long cheese = HomePageActivity.locScore;

    long newCheese = 0;

    static long cheeseClick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_shop);
        getUpgrades();

        userId = String.valueOf(UserSession.getInstance().getUserId());

        updateCheese(userId, cheese);


        Button homeButton = findViewById(R.id.shop_page_home_button);
        Button friendsButton = findViewById(R.id.shop_page_friends_button);
        Button leaderboardButton = findViewById(R.id.shop_page_leaderboard_button);
        Button chatButton = findViewById(R.id.shop_page_chat_button);

        // Get references to each button and text view
        Button addItem1Button = findViewById(R.id.addItem1Button);
        Button addItem2Button = findViewById(R.id.addItem2Button);
        Button addItem3Button = findViewById(R.id.addItem3Button);
        Button addItem4Button = findViewById(R.id.addItem4Button);
        Button addItem5Button = findViewById(R.id.addItem5Button);
        Button addItem6Button = findViewById(R.id.addItem6Button);

        TextView cheeseText = findViewById(R.id.cheese);
        TextView item1Text = findViewById(R.id.item1Name);
        TextView item2Text = findViewById(R.id.item2Name);
        TextView item3Text = findViewById(R.id.item3Name);
        TextView item4Text = findViewById(R.id.item4Name);
        TextView item5Text = findViewById(R.id.item5Name);
        TextView item6Text = findViewById(R.id.item6Name);
        TextView cheeseClickNum = findViewById(R.id.cheeseClickAmount);
        TextView outputView = findViewById(R.id.output);

        getUpgrades();
        // Set initial text text
        display = "Cheese =" + cheese;
        cheeseText.setText(display);
        display = "Cheese/Click = " + cheeseClick;
        cheeseClickNum.setText(display);
        display = "Baby Mouse: 1+Cheese/click\nPrice: $" + newPrice(ITEM1_BASEPRICE, PERCENT_INC1, item1Count) + "\nPurchased: " + item1Count + " times";
        item1Text.setText(display);
        display = "Teen Mouse: 5+Cheese/click\nPrice: $" + newPrice(ITEM2_BASEPRICE, PERCENT_INC2, item2Count) + "\nPurchased: " + item2Count + " times";
        item2Text.setText(display);
        display = "Adult Mouse: 10+Cheese/click\nPrice: $" + newPrice(ITEM3_BASEPRICE, PERCENT_INC3, item3Count) + "\nPurchased: " + item3Count + " times";
        item3Text.setText(display);
        display = "Elder Mouse: 15+Cheese/click\nPrice: $" + newPrice(ITEM4_BASEPRICE, PERCENT_INC4, item4Count) + "\nPurchased: " + item4Count + " times";
        item4Text.setText(display);
        //THESE ONES BELOW WILL BE FOR COSMETIC ITEM LOGIC; PURCHASE LIMIT 1
        display = "COSME 1 - Price: $" + ITEM5_BASEPRICE;
        item5Text.setText(display);
        display = "COSME 2 - Price: $" + ITEM6_BASEPRICE;
        item6Text.setText(display);

        addItem1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cheeseMod = 1;
                if (enoughCheese(cheese,newPrice(ITEM1_BASEPRICE, PERCENT_INC1, item1Count))) {
                    newCheese = cheeseTransact(cheese,newPrice(ITEM1_BASEPRICE, PERCENT_INC1, item1Count));
                    cheese = newCheese;
                    cheeseClick = cheesePerClick(cheeseClick, cheeseMod);
                    display = "Cheese/Click = " + cheeseClick;
                    cheeseClickNum.setText(display);
                    display = "Cheese: " + cheese;
                    cheeseText.setText(display);

                    item1Count++; // Increment counter

                    updateUpgrades( userId,  item1Count,  item2Count, item3Count, item4Count, purchasedItem5,  purchasedItem6);
                    updateCheese(userId,cheese);
                    display = "Cheese Modifier: 1+Cheese/click\nPrice: $" + newPrice(ITEM1_BASEPRICE, PERCENT_INC1,item1Count) + "\nPurchased: " + item1Count + " times";
                    item1Text.setText(display);
                }else{
                    setTextWithClear(outputView, "Insufficient cheese", 3000); // Clears after 3 seconds
                }
            }
        });

        addItem2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cheeseMod =5;
                if (enoughCheese(cheese, newPrice(ITEM2_BASEPRICE, PERCENT_INC2,item2Count))) {


                    newCheese = cheeseTransact(cheese,newPrice(ITEM2_BASEPRICE, PERCENT_INC2, item2Count));
                    cheese = newCheese;
                    cheeseClick = cheesePerClick(cheeseClick, cheeseMod);
                    display = "Cheese/Click = " + cheeseClick;
                    cheeseClickNum.setText(display);
                    display = "Cheese: " + cheese;
                    cheeseText.setText(display);
                    item2Count++; // Increment counter
                    updateUpgrades( userId,  item1Count,  item2Count, item3Count, item4Count, purchasedItem5,  purchasedItem6);
                    updateCheese(userId, cheese);
                    display = "Cheese Modifier: 5+Cheese/click\nPrice: $" + newPrice(ITEM2_BASEPRICE, PERCENT_INC2,item2Count) + "\nPurchased: " + item2Count + " times";
                    item2Text.setText(display);
                }else{
                    setTextWithClear(outputView, "Insufficient cheese", 3000); // Clears after 3 seconds
                }
            }
        });
        addItem3Button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int cheeseMod = 10;
                if (enoughCheese(cheese, newPrice(ITEM3_BASEPRICE, PERCENT_INC3,item3Count))) {


                    newCheese = cheeseTransact(cheese,newPrice(ITEM3_BASEPRICE, PERCENT_INC3, item3Count));
                    cheese = newCheese;
                    cheeseClick = cheesePerClick(cheeseClick, cheeseMod);
                    cheeseClickNum.setText("Cheese/Click = " + cheeseClick);
                    cheeseText.setText("Cheese: " + cheese);
                    item3Count++; // Increment counter
                    updateUpgrades( userId,  item1Count,  item2Count, item3Count, item4Count, purchasedItem5,  purchasedItem6);
                    updateCheese(userId,cheese);
                    item3Text.setText(getString(R.string.cheese_modifier_10_cheese_click_price) + newPrice(ITEM3_BASEPRICE, PERCENT_INC3,item3Count) + "\nPurchased: " + item3Count + " times");
                }else{
                    setTextWithClear(outputView, "Insufficient cheese", 3000); // Clears after 3 seconds
                }
            }
        });
        addItem4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cheeseMod = 15;
                if (enoughCheese(cheese, newPrice(ITEM4_BASEPRICE, PERCENT_INC4,item4Count))) {


                    newCheese = cheeseTransact(cheese,newPrice(ITEM4_BASEPRICE, PERCENT_INC4, item4Count));
                    cheese = newCheese;
                    cheeseClick = cheesePerClick(cheeseClick, cheeseMod);
                    display = "Cheese/Click = " + cheeseClick;
                    cheeseClickNum.setText(display);
                    display = "Cheese: " + cheese;
                    cheeseText.setText(display);
                    item4Count++; // Increment counter
                    updateUpgrades( userId,  item1Count,  item2Count, item3Count, item4Count, purchasedItem5,  purchasedItem6);
                    updateCheese(userId, cheese);
                    display = "Cheese Modifier: 15+Cheese/click\nPrice: $" + newPrice(ITEM4_BASEPRICE, PERCENT_INC4, item4Count) + "\nPurchased: " + item4Count + " times";
                    item4Text.setText(display);
                }else{
                    setTextWithClear(outputView, "Insufficient cheese", 3000); // Clears after 3 seconds
                }
            }
        });

        addItem5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enoughCheese(cheese, ITEM5_BASEPRICE) && item5Count == 0) {


                    purchasedItem5 = true;
                    purchasedItem6 = false;
                    item5Text.setText(R.string.cosme_1_sold_out);
                    newCheese = cheeseTransact(cheese, ITEM5_BASEPRICE);
                    cheese = newCheese;
                    display = "Cheese: " + cheese;
                    cheeseText.setText(display);
                    item5Count++; // Increment counter
                    updateUpgrades( userId,  item1Count,  item2Count, item3Count, item4Count, purchasedItem5,  purchasedItem6);
                    updateCheese(userId,cheese);
                }else if (purchasedItem5) {
                    item5Text.setText(R.string.cosme_1_sold_out);
                    setTextWithClear(outputView, "Cannot purchase more than once", 3000); // Clears after 3 seconds
                }else{
                    setTextWithClear(outputView, "Insufficient cheese", 3000); // Clears after 3 seconds
                }
            }
        });
        addItem6Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (enoughCheese(cheese, ITEM6_BASEPRICE) && item6Count == 0) {

                    purchasedItem5 = false;
                    purchasedItem6 = true;
                    item6Text.setText(R.string.cosme_2_sold_out);
                    newCheese = cheeseTransact(cheese, ITEM6_BASEPRICE);
                    cheese = newCheese;

                    display = "Cheese: " + cheese;
                    cheeseText.setText(display);
                    item6Count++; // Increment counter
                    updateUpgrades( userId,  item1Count,  item2Count, item3Count, item4Count, purchasedItem5,  purchasedItem6);
                    updateCheese(userId,cheese);
                }else if (purchasedItem6) {
                    item6Text.setText(R.string.cosme_2_sold_out);
                    setTextWithClear(outputView, "Cannot purchase more than once", 3000); // Clears after 3 seconds
                }else{
                    setTextWithClear(outputView, "Insufficient cheese", 3000); // Clears after 3 seconds
                }
            }
        });





        homeButton.setOnClickListener(v -> startActivity(new Intent(UpgradeShopActivity.this, HomePageActivity.class)));
        friendsButton.setOnClickListener(v -> startActivity(new Intent(UpgradeShopActivity.this, FriendsActivity.class)));
        leaderboardButton.setOnClickListener(v -> startActivity(new Intent(UpgradeShopActivity.this, LeaderboardActivity.class)));
        chatButton.setOnClickListener(v -> startActivity(new Intent(UpgradeShopActivity.this, GlobalChatActivity.class)));
    }
    public static long newPrice(int basePrice, double percentInc, long numberOwned){
        long returnPrice = basePrice;

        int iterations = 0;
        while (iterations < numberOwned) {
            returnPrice = (long) (returnPrice * percentInc);
            iterations++;
        }
        return returnPrice;
    }

    public static boolean enoughCheese(long cheese,long price){
        return cheese >= price;
    }
    // Function to set text and clear it after a delay
    private void setTextWithClear(final TextView textView, String text, int delayMillis) {
        // Set the new text
        textView.setText(text);

        // Clear the text after the specified delay
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.setText(""); // Clear the text
            }
        }, delayMillis);
    }

    public static long cheeseTransact(long currentCheese, long transactionAmount) {
        HomePageActivity.clicks -= transactionAmount;
        return currentCheese - transactionAmount;
    }

    public  long cheesePerClick (long currentCheeseClick, int cheeseClickMod){
        return currentCheeseClick + cheeseClickMod;
    }

    private void updateCheese(String userId, long cheese){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user")
                .whereEqualTo("userID", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            String documentId = document.getId(); // get doc ID
                            Log.d("Firestore", "Found document with userID: " + userId + ". Document ID: " + documentId);

                            //update cheese
                            db.collection("user").document(documentId)
                                    .update("score", cheese)
                                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Score updated successfully"))
                                    .addOnFailureListener(e -> Log.w("Firestore", "Error updating score: " + e.getMessage()));
                        }
                    } else {
                        Log.w("Firestore", "No document found with userID: " + userId);
                    }
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Error querying collection: " + e.getMessage()));
    }
                                                                //, long item2Count, long item3Count, long item4Count, boolean purchasedItem5, boolean purchasedItem6
    private void updateUpgrades(String userId, long item1Count, long item2Count,long item3Count, long item4Count, boolean purchasedItem5, boolean purchasedItem6){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("upgrades")
                .whereEqualTo("userID", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            String documentId = document.getId(); // get doc ID
                            Log.d("Firestore", "Found document with userID: " + userId + ". Document ID: " + documentId);

                            //update cheese
                            db.collection("upgrades").document(documentId)
                                                                         //, "babymouse", item1Count, item2Count, "teenmouse", item3Count, "adultmouse", item4Count, "eldermouse", purchasedItem5, "MidMouse", purchasedItem6, "HighMouse"
                                    .update( "babymouse", item1Count,  "teenmouse", item2Count, "adultmouse" ,item3Count,  "eldermouse", item4Count, "MidMouse", purchasedItem5, "HighMouse",purchasedItem6 )
                                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Score updated successfully"))
                                    .addOnFailureListener(e -> Log.w("Firestore", "Error updating score: " + e.getMessage()));
                        }
                    } else {
                        Log.w("Firestore", "No document found with userID: " + userId);
                    }
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Error querying collection: " + e.getMessage()));
    }
    private void getUpgrades(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("upgrades")
                .whereEqualTo("userID", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        if (document.contains("babymouse")) {
                            item1Count = document.getLong("babymouse");

                        }
                        if (document.contains("teenmouse")) {
                            item2Count = document.getLong("teenmouse");

                        }
                        if (document.contains("adultmouse")) {
                            item3Count = document.getLong("adultmouse");

                        }
                        if (document.contains("eldermouse")) {
                            item4Count = document.getLong("eldermouse");

                        }
                        if (document.contains("MidMouse")) {
                            purchasedItem5 = document.getBoolean("MidMouse");

                        }
                        if (document.contains("HighMouse")) {
                            purchasedItem6 = document.getBoolean("HighMouse");

                        }

                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error fetching score.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });
    }

}