package com.example.checkpoint3for5236;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DiscussionBoardActivity extends AppCompatActivity {

    private static final  String TAG="DiscussionBoardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_discussion_board);

        Button back = (Button) findViewById(R.id.button2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DiscussionBoardActivity.this, ChooseDiscussionBoardActivity.class));
            }
        });
    }
}