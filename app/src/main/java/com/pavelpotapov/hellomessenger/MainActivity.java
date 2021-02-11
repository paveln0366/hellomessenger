package com.pavelpotapov.hellomessenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMessages;
    private MessagesAdapter adapter;

    private EditText editTextMessage;
    private ImageView imageViewSendMessage;

    private List<Message> messages;
    private String author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        adapter = new MessagesAdapter();

        editTextMessage = findViewById(R.id.editTextMessage);
        imageViewSendMessage = findViewById(R.id.imageViewSendMessage);

        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(adapter);

        messages = new ArrayList<>();
        author = "Павел";

        imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String textOfMessage = editTextMessage.getText().toString().trim();

        if (!textOfMessage.isEmpty()) {
            messages.add(new Message(author, textOfMessage));
            adapter.setMessages(messages);
            editTextMessage.setText("");
            recyclerViewMessages.scrollToPosition(adapter.getItemCount() - 1);
        }
    }
}