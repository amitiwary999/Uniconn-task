package com.example.amit.uniconnexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by amit on 8/12/16.
 */

public class Chatstart extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    FirebaseUser user;
    String msg;
    EditText message;
    Bundle bundle;
    String key;
   private DatabaseReference newMessage,newMesage,newReply;
    private RecyclerView mChat;
    ImageButton send;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startchat);
        auth=FirebaseAuth.getInstance();
        bundle=getIntent().getExtras();
        if(bundle.getString("chat")!=null)
            key=bundle.getString("chat");
        mChat=(RecyclerView)findViewById(R.id. rv_chat_feed);
        send=(ImageButton)findViewById(R.id.btnSend);
        message=(EditText)findViewById(R.id.et_message);
        msg=message.getText().toString();
        newReply=FirebaseDatabase.getInstance().getReference().child("message").child(key).child(auth.getCurrentUser().getUid());
        mDatabase= FirebaseDatabase.getInstance().getReference().child("message");
        mChat.setLayoutManager(new LinearLayoutManager(this));
        final String msge=msg;
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Chatstart.this,msg,Toast.LENGTH_LONG).show();
                if(msge.length()!=0) {
                    DatabaseReference newMessage = mDatabase.child(auth.getCurrentUser().getUid()).child(key).push();
                    newMessage.child("msg1").setValue(msge);
                    DatabaseReference newMesage = mDatabase.child(key).child(auth.getCurrentUser().getUid()).push();
                    newMesage.child("msg2").setValue(msge);
                    message.setText(" ");

                    //  new computeThread().start();
                    computeothermessage();
                }
            }
        });

    }

    public synchronized void computeothermessage(){
        FirebaseRecyclerAdapter<Chatstartmodel,Chatstartviewholder> firebaserecycleradapter=new FirebaseRecyclerAdapter<Chatstartmodel, Chatstartviewholder>(
                Chatstartmodel.class,
                R.layout.activity_startchatitem,
                Chatstartviewholder.class,
                newReply
        ) {
            @Override
            protected void populateViewHolder(final Chatstartviewholder viewHolder, final Chatstartmodel model, int position) {
                //  viewHolder.bindData(model);
                newReply.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("msg2")) {
                            viewHolder.setMsg1(model.getMsg1());
                        }  if(dataSnapshot.hasChild("msg1")) {
                            viewHolder.setMsg2(model.getMsg2());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        mChat.setAdapter(firebaserecycleradapter);
    }

    public static class Chatstartviewholder extends RecyclerView.ViewHolder{
        View mView;
        DatabaseReference newReply;
        FirebaseAuth auth;
        public Chatstartviewholder(View itemView) {
            super(itemView);
            mView=itemView;
            auth=FirebaseAuth.getInstance();
            newReply=FirebaseDatabase.getInstance().getReference().child("message").child(" ").child(auth.getCurrentUser().getUid());
        }

        public void setMsg1(String msg){
            TextView sMessage=(TextView)mView.findViewById(R.id.sMessage);
            sMessage.setText(msg);
        }

        public void setMsg2(String msg){
            TextView rMessage=(TextView)mView.findViewById(R.id.rMessage);
            rMessage.setText(msg);
        }
     /*   public void bindData(final Chatstartmodel model){
          final  TextView sMessage=(TextView)mView.findViewById(R.id.sMessage);
           final TextView rMessage=(TextView)mView.findViewById(R.id.rMessage);
           // if(newReply.has)
            newReply.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild("2")){
                        sMessage.setText(model.getMsg1());
                    }else if(dataSnapshot.hasChild("1")){
                        rMessage.setText(model.getMsg2());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }*/
    }
    private class computeThread extends Thread {
        public void run() {
            computeothermessage();
        }
    }


}
