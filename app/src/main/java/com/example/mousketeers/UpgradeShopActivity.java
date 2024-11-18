package com.example.mousketeers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class UpgradeShopActivity extends AppCompatActivity {
    // Initial prices for items
    final private int ITEM1_BASEPRICE = 10;
    final private int ITEM2_BASEPRICE = 20;
    final private int ITEM3_BASEPRICE = 30;
    final private int ITEM4_BASEPRICE = 40;
    final private int ITEM5_BASEPRICE = 50;
    final private int ITEM6_BASEPRICE = 60;

    // Counters for each item
    private int item1Count = 0;
    private int item2Count = 0;
    private int item3Count = 0;
    private int item4Count = 0;
    private int item5Count = 0;
    private int item6Count = 0;
    private boolean purchasedItem5 = false;
    private boolean purchasedItem6 = false;

    final private double PERCENT_INC1 = 0.10;
    final private double PERCENT_INC2 = 0.15;
    final private double PERCENT_INC3 = 0.20;
    final private double PERCENT_INC4 = 0.25;
    //SAMPLE CHEESE TO CHECK 'IF ENOUGH CHEESE' FUNCTION
    long cheese = HomePageActivity.clicks;

    long newCheese = 0;

    long cheeseClick = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_shop);

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



        // Set initial text text
        cheeseText.setText("Cheese =" + cheese);
        cheeseClickNum.setText("Cheese/Click = " + cheeseClick);
        item1Text.setText("Cheese Modifier: 1+Cheese/click\nPrice: $" + ITEM1_BASEPRICE + "\nPurchased: " + item1Count + " times");
        item2Text.setText("Cheese Modifier: 5+Cheese/click\nPrice: $" + ITEM2_BASEPRICE + "\nPurchased: " + item2Count + " times");
        item3Text.setText("Cheese Modifier: 10+Cheese/click\nPrice: $" + ITEM3_BASEPRICE + "\nPurchased: " + item3Count + " times");
        item4Text.setText("Cheese Modifier: 15+Cheese/click\nPrice: $" + ITEM4_BASEPRICE + "\nPurchased: " + item4Count + " times");
        //THESE ONES BELOW WILL BE FOR COSMETIC ITEM LOGIC; PURCHASE LIMIT 1
        item5Text.setText("COSME 1 - Price: $" + ITEM5_BASEPRICE);
        item6Text.setText("COSME 2 - Price: $" + ITEM6_BASEPRICE );

        addItem1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cheeseMod = 1;
                if (enoughCheese(cheese,newPrice(ITEM1_BASEPRICE, PERCENT_INC1, item1Count))) {
                    newCheese = cheeseTransacc(cheese,newPrice(ITEM1_BASEPRICE, PERCENT_INC1, item1Count));
                    cheese = newCheese;
                    cheeseClick = cheesePerClick(cheeseClick, cheeseMod);
                    cheeseClickNum.setText("Cheese/Click = " + cheeseClick);
                    cheeseText.setText("Cheese: " + newCheese);
                    item1Count++; // Increment counter
                    item1Text.setText("Cheese Modifier: 1+Cheese/click\nPrice: $" + ITEM1_BASEPRICE + "\nPurchased: " + item1Count + " times");
                }else{
                    setTextWithClear(outputView, "Insufficient cheese", 3000); // Clears after 3 seconds
                }
            }
        });

        addItem2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cheeseMod = 5;
                if (enoughCheese(cheese, newPrice(ITEM2_BASEPRICE, PERCENT_INC2,item2Count))) {


                    newCheese = cheeseTransacc(cheese,newPrice(ITEM2_BASEPRICE, PERCENT_INC2, item2Count));
                    cheese = newCheese;
                    cheeseClick = cheesePerClick(cheeseClick, cheeseMod);
                    cheeseClickNum.setText("Cheese/Click = " + cheeseClick);
                    cheeseText.setText("Cheese: " + newCheese);
                    item2Count++; // Increment counter
                    item2Text.setText("Cheese Modifier: 5+Cheese/click\nPrice: $" + ITEM2_BASEPRICE + "\nPurchased: " + item2Count + " times");
                }else{
                    setTextWithClear(outputView, "Insufficient cheese", 3000); // Clears after 3 seconds
                }
            }
        });
        addItem3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cheeseMod = 10;
                if (enoughCheese(cheese, newPrice(ITEM3_BASEPRICE, PERCENT_INC3,item3Count))) {


                    newCheese = cheeseTransacc(cheese,newPrice(ITEM3_BASEPRICE, PERCENT_INC3, item3Count));
                    cheese = newCheese;
                    cheeseClick = cheesePerClick(cheeseClick, cheeseMod);
                    cheeseClickNum.setText("Cheese/Click = " + cheeseClick);
                    cheeseText.setText("Cheese: " + newCheese);
                    item3Count++; // Increment counter
                    item3Text.setText("Cheese Modifier: 10+Cheese/click\nPrice: $" + ITEM3_BASEPRICE + "\nPurchased: " + item3Count + " times");
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


                    newCheese = cheeseTransacc(cheese,newPrice(ITEM4_BASEPRICE, PERCENT_INC4, item4Count));
                    cheese = newCheese;
                    cheeseClick = cheesePerClick(cheeseClick, cheeseMod);
                    cheeseClickNum.setText("Cheese/Click = " + cheeseClick);
                    cheeseText.setText("Cheese: " + newCheese);
                    item4Count++; // Increment counter
                    item4Text.setText("Cheese Modifier: 15+Cheese/click\nPrice: $" + ITEM4_BASEPRICE + "\nPurchased: " + item4Count + " times");
                }else{
                    setTextWithClear(outputView, "Insufficient cheese", 3000); // Clears after 3 seconds
                }
            }
        });

        addItem5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enoughCheese(cheese, ITEM5_BASEPRICE) && item5Count == 0) {

                    item5Text.setText("COSME 1 - SOLD OUT");
                    purchasedItem5 = true;
                    newCheese = cheeseTransacc(cheese, ITEM5_BASEPRICE);
                    cheese = newCheese;
                    cheeseText.setText("Cheese: " + newCheese);
                    item5Count++; // Increment counter
                }else if (purchasedItem5) {
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

                    item6Text.setText("COSME 2 - SOLD OUT");
                    purchasedItem6 = true;
                    newCheese = cheeseTransacc(cheese, ITEM6_BASEPRICE);
                    cheese = newCheese;

                    cheeseText.setText("Cheese: " + newCheese);
                    item6Count++; // Increment counter
                }else if (purchasedItem6) {
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
    public static long newPrice(int basePrice, double percentInc, int numberOwned){
        long returnPrice = basePrice;

        int iterations = 0;
        while (iterations < numberOwned) {
            returnPrice += (returnPrice * percentInc);
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

    public static long cheeseTransacc(long currentCheese, long transactionAmount) {
        HomePageActivity.clicks -= transactionAmount;
        return currentCheese - transactionAmount;
    }

    public static long cheesePerClick (long currentCheeseClick, int cheeseClickMod){
        return currentCheeseClick + cheeseClickMod;
    }
}