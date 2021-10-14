package com.example.chatapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainChatActivity extends Activity {

    private String mDisplayName;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;
    private DatabaseReference mDatabaseRef;
    private chatListAdaptor mAdaptor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        setmDisplayName();
        mDatabaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chat-app-a151f-default-rtdb.firebaseio.com/");

        mInputText = findViewById(R.id.messageInp);
        mSendButton = findViewById(R.id.sendButton);
        mChatListView = findViewById(R.id.chatList);

        mInputText.setOnEditorActionListener((v, actionId, event) -> {
            sendMessage();
            return true;
        });

        mSendButton.setOnClickListener(v -> sendMessage());

    }

    private void sendMessage() {
        Log.d("Admin","Snet sometrthinf");

        String input = mInputText.getText().toString();
        if (!input.equals("")){
            InstantMsg chat = new InstantMsg(input,mDisplayName);
            mDatabaseRef.child("messages").push().setValue(chat);
            mInputText.setText("");
        }
    }

    private void setmDisplayName(){
        SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS,MODE_PRIVATE);
        mDisplayName = prefs.getString(RegisterActivity.DISPLAY_NAME_KEY,null);
        if (mDisplayName == null) mDisplayName="Anonymous";
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdaptor = new chatListAdaptor(this,mDatabaseRef,mDisplayName);
        mChatListView.setAdapter(mAdaptor);
    }

    public void onStop() {
        super.onStop();
        mAdaptor.cleanUp();
    }








}
