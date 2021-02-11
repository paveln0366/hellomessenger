package com.pavelpotapov.hellomessenger;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private RecyclerView recyclerViewMessages;
    private MessagesAdapter adapter;

    private EditText editTextMessage;
    private ImageView imageViewSendMessage;

    private String author;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemSignOut) {
            mAuth.signOut();
            signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
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

        if (mAuth.getCurrentUser() != null) {
            Toast.makeText(this, "Logged", Toast.LENGTH_SHORT).show();
        } else {
            signOut();
        }
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

    private void signOut() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}