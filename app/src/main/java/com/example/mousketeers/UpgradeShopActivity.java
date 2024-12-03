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
    String display = "placeholder"; // String variable used for concatenation
    String display1 = "placeholder"; // String variable used fora concatenation
    String display2 = "placeholder"; // String variable used for concatenation
    String display3 = "placeholder"; // String variable used for concatenation
    String display4 = "placeholder"; // String variable used for concatenation
    String display5 = "placeholder"; // String variable used for concatenation
    String display6 = "placeholder"; // String variable used for concatenation
    String display7 = "placeholder"; // String variable used for concatenation
    String display8 = "placeholder"; // String variable used for concatenation


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
    static long item5Count;
    static long item6Count;
    static boolean purchasedItem5 = false;
    static boolean purchasedItem6 = false;

    static long cheeseMod1 = 1;
    static long cheeseMod2 = 5;
    static long cheeseMod3 = 10;
    static long cheeseMod4 =15;



    final private double PERCENT_INC1 = 1.10;
    final private double PERCENT_INC2 = 1.15;
    final private double PERCENT_INC3 = 1.20;
    final private double PERCENT_INC4 = 1.25;
    //SAMPLE CHEESE TO CHECK 'IF ENOUGH CHEESE' FUNCTION
    long cheese = HomePageActivity.locScore;

    long newCheese = 0;
    static int flag = 0;
    static long cheeseClick = 1;

    /**
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
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
        display = "Cheese =" + cheese;
        cheeseText.setText(display);
        display1 = "Cheese/Click = " + cheeseClick;
        cheeseClickNum.setText(display1);


        display2 = "Baby Mouse: 1+Cheese/click\nPrice: $" + newPrice(ITEM1_BASEPRICE, PERCENT_INC1, item1Count) + "\nPurchased: " + item1Count + " times";
        item1Text.setText(display2);
        display3 = "Teen Mouse: 5+Cheese/click\nPrice: $" + newPrice(ITEM2_BASEPRICE, PERCENT_INC2, item2Count) + "\nPurchased: " + item2Count + " times";
        item2Text.setText(display3);
        display4 = "Adult Mouse: 10+Cheese/click\nPrice: $" + newPrice(ITEM3_BASEPRICE, PERCENT_INC3, item3Count) + "\nPurchased: " + item3Count + " times";
        item3Text.setText(display4);
        display5 = "Elder Mouse: 15+Cheese/click\nPrice: $" + newPrice(ITEM4_BASEPRICE, PERCENT_INC4, item4Count) + "\nPurchased: " + item4Count + " times";
        item4Text.setText(display5);
        //THESE ONES BELOW WILL BE FOR COSMETIC ITEM LOGIC; PURCHASE LIMIT 1
        display6 = "COSME 1 - Price: $" + ITEM5_BASEPRICE;
        item5Text.setText(display6);
        display7 = "COSME 2 - Price: $" + ITEM6_BASEPRICE;
        item6Text.setText(display7);

        addItem1Button.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles click events for the associated view (upgrade1).
             *
             * @param v the view that was clicked
             */
            @Override
            public void onClick(View v) {

                if (enoughCheese(cheese,newPrice(ITEM1_BASEPRICE, PERCENT_INC1, item1Count))) {
                    newCheese = cheeseTransact(cheese,newPrice(ITEM1_BASEPRICE, PERCENT_INC1, item1Count));
                    display = "Cheese: " + cheese;
                    cheeseText.setText(display);
                    cheese = newCheese;
                    cheeseClick = cheesePerClick(cheeseClick, cheeseMod1);
                    display1 = "Cheese/Click = " + cheeseClick;
                    cheeseClickNum.setText(display1);

                    item1Count++; // Increment counter

                    updateUpgrades( userId,  item1Count,  item2Count, item3Count, item4Count, purchasedItem5,  purchasedItem6);
                    updateCheese(userId,cheese);
                    display2 = "Baby Mouse: 1+Cheese/click\nPrice: $" + newPrice(ITEM1_BASEPRICE, PERCENT_INC1,item1Count) + "\nPurchased: " + item1Count + " times";
                    item1Text.setText(display2);
                }else{
                    setTextWithClear(outputView, "Insufficient cheese", 3000); // Clears after 3 seconds
                }
            }
        });

        addItem2Button.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles click events for the associated view (upgrade2).
             *
             * @param v the view that was clicked
             */
            @Override
            public void onClick(View v) {

                if (enoughCheese(cheese, newPrice(ITEM2_BASEPRICE, PERCENT_INC2,item2Count))) {
                    newCheese = cheeseTransact(cheese,newPrice(ITEM2_BASEPRICE, PERCENT_INC2, item2Count));
                    display = "Cheese: " + cheese;
                    cheeseText.setText(display);
                    cheese = newCheese;
                    cheeseClick = cheesePerClick(cheeseClick, cheeseMod2);
                    display1 = "Cheese/Click = " + cheeseClick;
                    cheeseClickNum.setText(display1);


                    item2Count++; // Increment counter

                    updateUpgrades( userId,  item1Count,  item2Count, item3Count, item4Count, purchasedItem5,  purchasedItem6);
                    updateCheese(userId, cheese);
                    display3 = "Teen Mouse: 5+Cheese/click\nPrice: $" + newPrice(ITEM2_BASEPRICE, PERCENT_INC2,item2Count) + "\nPurchased: " + item2Count + " times";
                    item2Text.setText(display3);
                }else{
                    setTextWithClear(outputView, "Insufficient cheese", 3000); // Clears after 3 seconds
                }
            }
        });
        addItem3Button.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles click events for the associated view (upgrade3).
             *
             * @param v the view that was clicked
             */
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                if (enoughCheese(cheese, newPrice(ITEM3_BASEPRICE, PERCENT_INC3,item3Count))) {

                    newCheese = cheeseTransact(cheese,newPrice(ITEM3_BASEPRICE, PERCENT_INC3, item3Count));


                    display = "Cheese: " + cheese;
                    cheeseText.setText(display);
                    cheese = newCheese;
                    cheeseClick = cheesePerClick(cheeseClick, cheeseMod3);
                    display1 = "Cheese/Click = " + cheeseClick;
                    cheeseClickNum.setText(display1);

                    item3Count++; // Increment counter

                    updateUpgrades( userId,  item1Count,  item2Count, item3Count, item4Count, purchasedItem5,  purchasedItem6);
                    updateCheese(userId,cheese);
                    display4 = "Adult Mouse: 5+Cheese/click\nPrice: $" + newPrice(ITEM3_BASEPRICE, PERCENT_INC3,item3Count) + "\nPurchased: " + item3Count + " times";
                    item3Text.setText(display4 );
                }else{
                    setTextWithClear(outputView, "Insufficient cheese", 3000); // Clears after 3 seconds
                }
            }
        });
        /**
         * Handles click events for the associated view (upgrade4).
         *
         * @param v the view that was clicked
         */
        addItem4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (enoughCheese(cheese, newPrice(ITEM4_BASEPRICE, PERCENT_INC4,item4Count))) {


                    newCheese = cheeseTransact(cheese,newPrice(ITEM4_BASEPRICE, PERCENT_INC4, item4Count));
                    cheese = newCheese;


                    display = "Cheese: " + cheese;
                    cheeseText.setText(display);
                    cheese = newCheese;
                    cheeseClick = cheesePerClick(cheeseClick, cheeseMod4);
                    display1 = "Cheese/Click = " + cheeseClick;
                    cheeseClickNum.setText(display1);

                    item4Count++; // Increment counter

                    updateUpgrades( userId,  item1Count,  item2Count, item3Count, item4Count, purchasedItem5,  purchasedItem6);
                    updateCheese(userId, cheese);
                    display5 = "Elder Mouse: 15+Cheese/click\nPrice: $" + newPrice(ITEM4_BASEPRICE, PERCENT_INC4, item4Count) + "\nPurchased: " + item4Count + " times";
                    item4Text.setText(display5);
                }else{
                    setTextWithClear(outputView, "Insufficient cheese", 3000); // Clears after 3 seconds
                }
            }
        });

        addItem5Button.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles click events for the associated view (upgrade5).
             *
             * @param v the view that was clicked
             */
            @Override
            public void onClick(View v) {
                if (enoughCheese(cheese, ITEM5_BASEPRICE) && item5Count == 0) {


                    purchasedItem5 = true;
                    purchasedItem6 = false;

                    item5Text.setText(display6);
                    newCheese = cheeseTransact(cheese, ITEM5_BASEPRICE);
                    cheese = newCheese;
                    display = "Cheese: " + cheese;
                    cheeseText.setText(display);
                    cheeseClickNum.setText(display1);
                    display6 = "COSME 1 - SOLD OUT";
                    item5Count++; // Increment counter
                    updateUpgrades( userId,  item1Count,  item2Count, item3Count, item4Count, purchasedItem5,  purchasedItem6);
                    updateCheese(userId,cheese);
                }else if (purchasedItem5) {
                    display = "Cheese: " + cheese;
                    cheeseText.setText(display);
                    cheeseClickNum.setText(display1);
                    display6 = "COSME 1 - SOLD OUT";
                    item5Text.setText(display6);
                    setTextWithClear(outputView, "Cannot purchase more than once", 3000); // Clears after 3 seconds
                }else{
                    display = "Cheese: " + cheese;
                    cheeseText.setText(display);
                    cheeseClickNum.setText(display1);
                    setTextWithClear(outputView, "Insufficient cheese", 3000); // Clears after 3 seconds
                }
            }
        });
        addItem6Button.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles click events for the associated view (upgrade6).
             *
             * @param v the view that was clicked
             */
            @Override
            public void onClick(View v) {


                if (enoughCheese(cheese, ITEM6_BASEPRICE) && item6Count == 0) {

                    purchasedItem5 = false;
                    purchasedItem6 = true;
                    cheeseClickNum.setText(display1);
                    display7 = "COSME 2 - SOLD OUT";
                    item6Text.setText(display7);
                    newCheese = cheeseTransact(cheese, ITEM6_BASEPRICE);
                    cheese = newCheese;

                    display = "Cheese: " + cheese;
                    cheeseText.setText(display);
                    item6Count++; // Increment counter
                    updateUpgrades( userId,  item1Count,  item2Count, item3Count, item4Count, purchasedItem5,  purchasedItem6);
                    updateCheese(userId,cheese);
                }else if (purchasedItem6) {
                    display = "Cheese: " + cheese;
                    cheeseText.setText(display);
                    cheeseClickNum.setText(display1);
                    display7 = "COSME 2 - SOLD OUT";
                    item6Text.setText(display7);
                    setTextWithClear(outputView, "Cannot purchase more than once", 3000); // Clears after 3 seconds
                }else{
                    display = "Cheese: " + cheese;
                    cheeseText.setText(display);
                    cheeseClickNum.setText(display1);
                    setTextWithClear(outputView, "Insufficient cheese", 3000); // Clears after 3 seconds
                }
            }
        });

        homeButton.setOnClickListener(v -> startActivity(new Intent(UpgradeShopActivity.this, HomePageActivity.class)));
        friendsButton.setOnClickListener(v -> startActivity(new Intent(UpgradeShopActivity.this, FriendsActivity.class)));
        leaderboardButton.setOnClickListener(v -> startActivity(new Intent(UpgradeShopActivity.this, LeaderboardActivity.class)));
        chatButton.setOnClickListener(v -> startActivity(new Intent(UpgradeShopActivity.this, GlobalChatActivity.class)));
    }

    /**
     * Function to set new price
     * @param basePrice Base price of item
     * @param percentInc Percentile to increment by
     * @param numberOwned Number of upgrades purchased
     * @return New price value
     */
    public static long newPrice(int basePrice, double percentInc, long numberOwned){
        long returnPrice = basePrice;

        int iterations = 0;
        while (iterations < numberOwned) {
            returnPrice = (long) (returnPrice * percentInc);
            iterations++;
        }
        return returnPrice;
    }
    /**
     * Determines if there is enough cheese to cover the price.
     *
     * @param cheese the amount of cheese available
     * @param price  the required amount of cheese
     * @return true if the available cheese is greater than or equal to the price, false otherwise
     */
    public static boolean enoughCheese(long cheese,long price){
        return cheese >= price;
    }
    // Function to set text and clear it after a delay
    /**
     * Sets text to a TextView and clears it after a delay.
     *
     * @param textView   the TextView to update
     * @param text       the text to set
     * @param delayMillis the delay in milliseconds before clearing the text
     */
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

    /**
     * Transaction function to adjust cheese value
     * @param currentCheese Current cheese amount
     * @param transactionAmount Deduction amount
     * @return New cheese value
     */
    public static long cheeseTransact(long currentCheese, long transactionAmount) {
        HomePageActivity.clicks -= transactionAmount;
        return currentCheese - transactionAmount;
    }
    /**
     * Calculates the cheese earned per click based on the current value and a modifier.
     *
     * @param currentCheeseClick the current cheese earned per click
     * @param cheeseClickMod     the modifier to adjust the cheese per click
     * @return the updated cheese earned per click
     */
    public  long cheesePerClick (long currentCheeseClick, long cheeseClickMod){
        return currentCheeseClick + cheeseClickMod;
    }

    /**
     * Updates cheese click
     *
     * @param cheeseMod the base cheese modifier
     * @param itemCount the number of items contributing to the cheese per click
     * @param flag      an additional parameter influencing the calculation
     * @return the total cheese earned per click
     */
    public static long getCheeseClick(long cheeseMod, long itemCount, int flag){
        if (flag < 2)
            return cheeseClick = cheeseMod*itemCount;
        else
            return cheeseClick;
    }

    /**
     * Updates cheese for current user to firestore .
     *
     * @param userId the ID of the user
     * @param cheese the new cheese count to set
     */
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
    /**
     * Updates the upgrade status and counts for a specific user.
     *
     * @param userId         the ID of the user
     * @param item1Count     the count of item 1
     * @param item2Count     the count of item 2
     * @param item3Count     the count of item 3
     * @param item4Count     the count of item 4
     * @param purchasedItem5 whether item 5 has been purchased
     * @param purchasedItem6 whether item 6 has been purchased
     */

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
    /**
     * Retrieves upgrades from firestore
     */
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
                            if (flag == 0) {
                            cheeseClick += getCheeseClick(cheeseMod1, item1Count, flag);
                            cheeseClick += getCheeseClick(cheeseMod2, item2Count, flag);
                            cheeseClick += getCheeseClick(cheeseMod3, item3Count, flag);
                            cheeseClick += getCheeseClick(cheeseMod4, item4Count, flag);
                            flag++;
                        }

                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error fetching score.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });
    }

}