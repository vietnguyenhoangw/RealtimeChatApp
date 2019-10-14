package com.example.realtimechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Message> arrayList;
    MessageAdapter messageAdapter;

    EditText edtMessageInput;
    ImageView btnMessageSend;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        craeteWidget();
        listViewCreate();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Message");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Message message = item.getValue(Message.class);
                    arrayList.add(message);
                    messageAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        edtMessageInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtMessageInput.setCursorVisible(true);
            }
        });


        btnMessageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edtMessageInput.getText().toString();

                if (edtMessageInput.getText().length() == 0) {

                    edtMessageInput.setError("Empty text.");

                }
                else {

                    edtMessageInput.setText("");

                    myRef = database.getReference("Message");
                    String key = myRef.child("Message").push().getKey();

                    Message message = new Message("daniel", text);

                    myRef.child(key).setValue(message);
                }

                hideSoftKeyboard(MainActivity.this);
            }
        });
    }

    public void craeteWidget() {
        edtMessageInput = findViewById(R.id.edtMessageInput);
        btnMessageSend = findViewById(R.id.btnMessageSend);
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

        edtMessageInput.setCursorVisible(false);
    }

    public void listViewCreate() {
        listView = findViewById(R.id.messages_view);
        arrayList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, R.layout.chat_item, arrayList);

        listView.setAdapter(messageAdapter);
    }

}
