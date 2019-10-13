package com.example.realtimechat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Message> arrayList;
    MessageAdapter messageAdapter;

    EditText edtMessageInput;
    ImageView btnMessageSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.messages_view);
        arrayList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, R.layout.chat_item, arrayList);

        listView.setAdapter(messageAdapter);

        edtMessageInput = findViewById(R.id.edtMessageInput);
        edtMessageInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtMessageInput.setCursorVisible(true);
            }
        });

        btnMessageSend = findViewById(R.id.btnMessageSend);
        btnMessageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edtMessageInput.getText().toString();

                arrayList.add(new Message("Daniel", text));
                messageAdapter.notifyDataSetChanged();

                edtMessageInput.setText("");
                hideSoftKeyboard(MainActivity.this);
            }
        });
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

        edtMessageInput.setCursorVisible(false);
    }
}
