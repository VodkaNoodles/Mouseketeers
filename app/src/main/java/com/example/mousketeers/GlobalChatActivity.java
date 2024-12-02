package com.example.mousketeers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import Utilities.UserSession;

/**
 * Model class for chat messages.
 * Represents a message in the global chat, including the message text and sender username.
 */
class ChatMessage {
    private String message;
    private String user;

    public ChatMessage() {}

    public ChatMessage(String message, String user) {
        this.message = message;
        this.user = user;
    }

    /**
     * Gets the message text.
     *
     * @return the message text.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the username of the sender.
     *
     * @return the sender's username.
     */
    public String getUser() {
        return user;
    }
}

/**
 * Adapter class for displaying chat messages in a RecyclerView.
 * Binds chat message data to the RecyclerView for dynamic display.
 */
class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private final List<ChatMessage> chatMessages;

    /**
     * Constructor for ChatAdapter.
     *
     * @param chatMessages the list of chat messages to display.
     */
    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the custom layout for chat messages
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_message_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        // Bind data to the view holder
        ChatMessage message = chatMessages.get(position);
        holder.messageText.setText(message.getMessage());
        holder.messageUser.setText(message.getUser());
    }

    @Override
    public int getItemCount() {
        // Return the total number of items
        return chatMessages.size();
    }

    /**
     * ViewHolder class to hold chat message views.
     */
    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, messageUser;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
            messageUser = itemView.findViewById(R.id.message_user);
        }
    }
}


/**
 * Activity class for the global chat feature.
 * Displays chat messages in real-time, allows users to send messages,
 * and includes navigation to other sections of the app.
 */
public class GlobalChatActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private EditText messageInput;
    private Button sendButton;
    private FirebaseFirestore db;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList;
    private String userId;
    private String username;

    /**
     * Called when the activity is created.
     * Initializes the UI, retrieves the current user's username, and sets up
     * the chat interface and navigation buttons.
     *
     * @param savedInstanceState the saved instance state bundle, if available.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_chat);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        userId = String.valueOf(UserSession.getInstance().getUserId());
        db.collection("user")
                .whereEqualTo("userID", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        if (document.contains("username")) {
                            username = document.getString("username");
                        } else {
                            Toast.makeText(this, "Username not found!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error fetching username.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });

        // Initialize Views
        chatRecyclerView = findViewById(R.id.chat_recycler_view);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);

        // Setup RecyclerView
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // Load Messages
        loadMessages();

        // Send Message
        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
            } else {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        // Navigation Buttons
        Button homeButton = findViewById(R.id.chat_page_home_button);
        Button friendsButton = findViewById(R.id.chat_page_friends_button);
        Button leaderboardButton = findViewById(R.id.chat_page_leaderboard_button);
        Button shopButton = findViewById(R.id.chat_page_shop_button);

        homeButton.setOnClickListener(v -> startActivity(new Intent(GlobalChatActivity.this, HomePageActivity.class)));
        friendsButton.setOnClickListener(v -> startActivity(new Intent(GlobalChatActivity.this, FriendsActivity.class)));
        leaderboardButton.setOnClickListener(v -> startActivity(new Intent(GlobalChatActivity.this, LeaderboardActivity.class)));
        shopButton.setOnClickListener(v -> startActivity(new Intent(GlobalChatActivity.this, UpgradeShopActivity.class)));
    }

    /**
     * Loads chat messages from the Firestore database and updates the RecyclerView.
     */
    private void loadMessages() {
        db.collection("global_chat")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        Toast.makeText(this, "Failed to load messages", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (querySnapshot != null) {
                        messageList.clear();
                        for (com.google.firebase.firestore.DocumentSnapshot document : querySnapshot.getDocuments()) {
                            messageList.add(document.toObject(ChatMessage.class));
                        }
                        chatAdapter.notifyDataSetChanged();

                        // Scroll to the bottom of the chat
                        if (!messageList.isEmpty()) {
                            chatRecyclerView.scrollToPosition(messageList.size() - 1);
                        }
                    }
                });
    }

    /**
     * Sends a chat message to the Firestore database.
     *
     * @param message the message text to be sent.
     */
    private void sendMessage(String message) {
        Map<String, Object> chatMessage = new HashMap<>();
        chatMessage.put("message", message);
        chatMessage.put("user", username); // Replace with dynamic username
        chatMessage.put("timestamp", System.currentTimeMillis());

        db.collection("global_chat").add(chatMessage)
                .addOnSuccessListener(documentReference -> {
                    messageInput.setText("");

                    // Scroll to the bottom after sending the message
                    if (!messageList.isEmpty()) {
                        chatRecyclerView.scrollToPosition(messageList.size() - 1);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show();
                });
    }

}
