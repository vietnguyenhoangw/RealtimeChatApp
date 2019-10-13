package com.example.realtimechat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter {

    int layout;
    Context context;
    ArrayList<Message> arrayList;

    TextView editUserName, editMessage;

    public MessageAdapter(Context context, int layout, ArrayList<Message> arrayList) {
        super(context, layout, arrayList);
        this.context = context;
        this.layout = layout;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(layout, null);

        editUserName = convertView.findViewById(R.id.edtUserName);
        editMessage = convertView.findViewById(R.id.edtMessage);

        Message message = arrayList.get(position);

        editUserName.setText(message.getUser());
        editMessage.setText(message.getMessage());

        return convertView;
    }
}
