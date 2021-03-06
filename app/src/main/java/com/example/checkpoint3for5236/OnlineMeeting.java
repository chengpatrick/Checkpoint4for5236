package com.example.checkpoint3for5236;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class OnlineMeeting extends AppCompatActivity {

    private static final  String TAG="OnlineMeeting";

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private FirebaseAuth mAuth;

    String className, title;
    String context;
    String userName;

    private TextView titleText,sendContext,timeView;
    private Button sendBtn,quitBtn;

    Handler mHandler;

    RecyclerView rView;

    private ArrayList<MeetingContext> items;
    private RecyclerView.LayoutManager mLayoutManager;
    private MeetingContextAdapter mAdapter;

    Date time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_meeting);

        this.mHandler = new Handler();
        this.mHandler.postDelayed(m_Runnable,5000);

        mAuth=FirebaseAuth.getInstance();
        rView = findViewById(R.id.recyclerViewOnlineMeeting);
        Bundle bundle = getIntent().getExtras();

        className = bundle.getString("classname");
        title=bundle.getString("title");
        // set title as class name
        titleText=findViewById(R.id.editTextTextPersonName);
        titleText.setText(className+" "+title);
        rView = findViewById(R.id.recyclerViewOnlineMeeting);

        sendBtn=findViewById(R.id.button7);
        sendContext=findViewById(R.id.editTextTextPersonName2);
        sendBtn.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           context=sendContext.getText().toString();
                                           if(context.equals("")){
                                               sendContext.setError("Cannot send empty context!");
                                               sendContext.requestFocus();
                                               return;
                                           }
                                           sendContext();
                                           Log.i(TAG, context);
                                       }
                                   }
        );

        // button to quit meeting
        quitBtn=(Button)findViewById(R.id.button3);
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //destroy
                finish();
            }
        });
    }

    // send context to database
    private void sendContext(){
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Classes").child(className).child("Meetings").child("M: "+className+" meeting, "+title);

        userName= mAuth.getCurrentUser().getUid();
        time= Calendar.getInstance().getTime();

        MeetingContext meetCon=new MeetingContext(userName,context,time);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                reference.child("Reply by "+userName+" at "+time.toString()).setValue(meetCon);
                // empty the context box once sent
                sendContext.setText("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createList() {
        DatabaseReference MeetConRef = FirebaseDatabase.getInstance().getReference("Classes").child(className).child("Meetings").child("M: "+className+" meeting, "+title);

        items = new ArrayList<MeetingContext>();

        MeetConRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dsp: snapshot.getChildren()){
                    if(dsp.hasChildren() && !dsp.getKey().equals("time")){
                        MeetingContext currentContext = dsp.getValue(MeetingContext.class);
                        items.add(currentContext);
                    }
                }
                Collections.sort(items, new CustomComparator());
                buildRecyclerView(items);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void buildRecyclerView(ArrayList<MeetingContext> items) {
        rView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MeetingContextAdapter(items);

        rView.setLayoutManager(mLayoutManager);
        rView.setAdapter(mAdapter);
    }

    public class CustomComparator implements Comparator<MeetingContext> {
        @Override
        public int compare(MeetingContext o1, MeetingContext o2) {
            return o1.getTime().compareTo(o2.getTime());
        }
    }

    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            createList();
            OnlineMeeting.this.mHandler.postDelayed(m_Runnable, 5000);
        }

    };//runnable

    @Override
    protected void onResume() {
        super.onResume();
        createList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(m_Runnable);
        finish();
    }
}