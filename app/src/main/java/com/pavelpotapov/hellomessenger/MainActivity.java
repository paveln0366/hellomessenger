package com.pavelpotapov.hellomessenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    private RecyclerView recyclerViewMessages;
    private MessagesAdapter adapter;

    private EditText editTextMessage;
    private ImageView imageViewSendMessage;

    private String author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        adapter = new MessagesAdapter();
        editTextMessage = findViewById(R.id.editTextMessage);
        imageViewSendMessage = findViewById(R.id.imageViewSendMessage);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(adapter);
        author = "Павел";

        imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        // If the database has changed
        db.collection("messages").orderBy("date").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                // value - list of all documents
                if (value != null) {
                    List<Message> messages = value.toObjects(Message.class);
                    adapter.setMessages(messages);
                }
            }
        });
    }

    private void sendMessage() {
        String textOfMessage = editTextMessage.getText().toString().trim();

        if (!textOfMessage.isEmpty()) {
            recyclerViewMessages.scrollToPosition(adapter.getItemCount() - 1);
            db.collection("messages")
                    .add(new Message(author, textOfMessage, System.currentTimeMillis()))
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            editTextMessage.setText("");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Сообщение не отправлено", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}