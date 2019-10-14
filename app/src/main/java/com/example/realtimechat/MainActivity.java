package com.example.realtimechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

    /* Listview */
    ListView listView;
    ArrayList<Message> arrayList;
    MessageAdapter messageAdapter;

    EditText edtMessageInput;
    ImageView btnMessageSend;

    /* Firebase Database */
    FirebaseDatabase database;
    DatabaseReference myRef;

    /* Dialog */
    EditText edtUserNameInput;
    static String UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createWidget();
        listViewCreate();
        craeteDialog();

        /*
         * call this method here because it's alway run when open app
         * (Read database Realtime)
         * */
        readFromFirebase();

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
                    // create random key (id) on firebase
                    String key = myRef.child("Message").push().getKey();
                    Message message = new Message(UserName, text);

                    myRef.child(key).setValue(message);
                }

                hideSoftKeyboard(MainActivity.this);
            }
        });
    }

    /* Dialog configure */
    private void craeteDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        final View dialogView = li.inflate(R.layout.name_input_dialog, null);


        final AlertDialog alertDialogBuilder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .create();
        alertDialogBuilder.setView(dialogView);

        edtUserNameInput = dialogView.findViewById(R.id.edtUserNameInputt);

        alertDialogBuilder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button =  alertDialogBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (edtUserNameInput.getText().length() == 0) {
                            edtUserNameInput.setError("Empty name!");
                        }
                        else {
                            UserName = edtUserNameInput.getText().toString();
                            // when everything is ok, using dismiss.
                            alertDialogBuilder.dismiss();
                        }
                    }
                });
            }
        });

        alertDialogBuilder.show();
    }

    public void createWidget() {
        edtMessageInput = findViewById(R.id.edtMessageInput);
        btnMessageSend = findViewById(R.id.btnMessageSend);
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

        edtMessageInput.setCursorVisible(false);
    }

    /* create listview */
    public void listViewCreate() {
        listView = findViewById(R.id.messages_view);
        arrayList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, R.layout.chat_item, arrayList);

        listView.setAdapter(messageAdapter);
    }

    /* read Database from realtimefirebase
     * get item and add to arraylist's listview.
     * */
    public void readFromFirebase() {
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
    }

}
