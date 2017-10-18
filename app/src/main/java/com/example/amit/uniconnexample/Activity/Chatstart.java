/*
    This source code is result of my hardwork. If you want to use it, drop me a message.
 */

package com.example.amit.uniconnexample.Activity;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amit.uniconnexample.Chatstartmodel;
import com.example.amit.uniconnexample.Message_model;
import com.example.amit.uniconnexample.Notifmsgmodel;
import com.example.amit.uniconnexample.R;
import com.example.amit.uniconnexample.UserData;
import com.example.amit.uniconnexample.utils.Utils;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;

import timber.log.Timber;

/**
 * Created by amit on 8/12/16.
 */

public class Chatstart extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    FirebaseUser user;
    String msg,name;
    EditText message;
    Bundle bundle;
   public static String key,spic,txt;
    TextView text;
    Toolbar toolbar;
    UserData userData;
    ActionBar actionbar;
    ImageView src; MediaPlayer song;
   private DatabaseReference newMessage,newMesage,newReply,newSend,newSnd,checkBack,pdata,newrply,newnotifChat,newnotifchat;
    private RecyclerView mChat;
    ImageButton send;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startchat);
        src=(ImageView)findViewById(R.id.src);
        text=(TextView)findViewById(R.id.text);
        auth=FirebaseAuth.getInstance();
        bundle=getIntent().getExtras();
        final Handler handler=new Handler();
        pdata=FirebaseDatabase.getInstance().getReference();
        userData=new UserData();
        actionbar=getSupportActionBar();
        if(bundle.getString("chat")!=null)
            key=bundle.getString("chat");
        setTitle("");
        mChat=(RecyclerView)findViewById(R.id. rv_chat_feed);
        send=(ImageButton)findViewById(R.id.btnSend);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     startActivity(new Intent(Chatstart.this,Tabs.class));
                finish();
            }
        });
        //Utils.setUpToolbarBackButton(this, toolbar);
        message=(EditText)findViewById(R.id.et_message);
        msg=message.getText().toString();
        newnotifChat=FirebaseDatabase.getInstance().getReference().child("notificationdata").child("chat").child(key).child(auth.getCurrentUser().getUid());
        newnotifchat=FirebaseDatabase.getInstance().getReference().child("notificationdata").child("chat").child(auth.getCurrentUser().getUid()).child(key);

        newSnd=FirebaseDatabase.getInstance().getReference().child("Smessage").child(auth.getCurrentUser().getUid()).child(key);
        newrply=FirebaseDatabase.getInstance().getReference().child("Smessage").child(key).child(auth.getCurrentUser().getUid());
        checkBack=FirebaseDatabase.getInstance().getReference().child("message");
      //  actionbar.setDisplayHomeAsUpEnabled(true);
       // Utils.setUpToolbarBackButton(Chatstart.this,toolbar);
        newMesage=FirebaseDatabase.getInstance().getReference().child("Userdetail").child(key).child("photo");
        newMesage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                spic=dataSnapshot.getValue(String.class);
                src.setImageBitmap(Utils.decodeBase64(dataSnapshot.getValue(String.class)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        pdata.child("Userdetail").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userData = dataSnapshot.getValue(UserData.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Timber.d("oncancelled");
            }
        });
        newMessage=FirebaseDatabase.getInstance().getReference().child("Userdetail").child(key).child("name");
        newMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               name= dataSnapshot.getValue(String.class);
                text.setText(name);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        newReply=FirebaseDatabase.getInstance().getReference().child("message").child(key).child(auth.getCurrentUser().getUid());
        newSend=FirebaseDatabase.getInstance().getReference().child("message").child(auth.getCurrentUser().getUid()).child(key);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("message");
        newReply.keepSynced(true);
        mDatabase.keepSynced(true);
        newnotifchat.keepSynced(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mChat.setLayoutManager(linearLayoutManager);
        mChat.setHasFixedSize(false);
        mChat.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    mChat.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int scrollTo = mChat.getAdapter().getItemCount() - 1;
                            scrollTo = scrollTo >= 0 ? scrollTo : 0;
                            mChat.scrollToPosition(scrollTo);
                        }
                    }, 10);
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String msge=message.getText().toString();
              //  Toast.makeText(Chatstart.this,msge,Toast.LENGTH_LONG).show();
                if(msge.length()!=0) {
                    DatabaseReference newMessage = mDatabase.child(auth.getCurrentUser().getUid()).child(key).push();
                    newMessage.child("msg1").setValue(msge);
                    newMessage.child("time1").setValue(getCurrentTime());
                    DatabaseReference newMesage = mDatabase.child(key).child(auth.getCurrentUser().getUid()).push();
                    newMesage.child("msg2").setValue(msge);
                    newMesage.child("time2").setValue(getCurrentTime());
                    message.setText("");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            newnotifChat.setValue(new Notifmsgmodel(msge,userData.name));
                            newSnd.setValue(new Message_model(spic,msge,name,key));
                            newrply.setValue(new Message_model(userData.photo,msge,userData.name,auth.getCurrentUser().getUid()));
                        }
                    },200);

              /*  FirebaseRecyclerAdapter<Chatreceivermodel,Chatreceiverholder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Chatreceivermodel, Chatreceiverholder>(
                        Chatreceivermodel.class,
                        R.layout.activity_startchatitem,
                        Chatreceiverholder.class,
                        newSend
                ) {
                    @Override
                    protected void populateViewHolder(Chatreceiverholder viewHolder, Chatreceivermodel model, int position) {
                        viewHolder.setMsg1(model.getMsg1());
                    }
                } ;*/
                //    mChat.setAdapter(firebaseRecyclerAdapter);
                    //  new computeThread().start();
                    computeothermessage();
                }
            }
        });

    }

  /*  public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public void notification(){
        song=MediaPlayer.create(this,R.raw.internetfriends0);
        song.start();
        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(this.NOTIFICATION_SERVICE);
        Notification n= new Notification.Builder(this).setContentTitle("Location notifier notice")
                .setContentText(" Just 1 km away from destination")
                .setSmallIcon(R.drawable.uniconn).setAutoCancel(true).build();
        notificationManager.notify(0,n);*/
       /* song= MediaPlayer.create(getApplicationContext(),R.raw.internetfriends0);
        song.start();
        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        NotificationCompat.Builder n= new NotificationCompat.Builder(this).setContentTitle("Location notifier notice")
                .setContentText(" Just 1 km away from destination")
                .setSmallIcon(R.drawable.no).setAutoCancel(true).build();

        notificationManager.notify(0,n.build());*/
  //  }

    @Override
    protected void onStart() {
        super.onStart();
      /*  newnotifChat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newnotifChat.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        newnotifchat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                dataSnapshot.getRef().setValue(null);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
     /*   newnotifchat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

      /* newSend.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(DataSnapshot dataSnapshot, String s) {
             /*  if(!Foreground.get().isForeground()){
                   notification();
               }*/
              /* if(dataSnapshot.hasChild("msg1")) {
                    txt = dataSnapshot.child("msg1").getValue(String.class);
                //   Toast.makeText(Chatstart.this, txt, Toast.LENGTH_LONG).show();
               }else if(dataSnapshot.hasChild("msg2")){
                    txt = dataSnapshot.child("msg2").getValue(String.class);
                //   Toast.makeText(Chatstart.this, txt, Toast.LENGTH_LONG).show();
               }
               newSnd.child("image").setValue(spic);
               newSnd.child("name").setValue(name);
               newSnd.child("key").setValue(auth.getCurrentUser().getUid());
               newSnd.child("msg").setValue(txt);
             //  DatabaseReference newmg=newSnd.push();
             //  newmg.setValue(new Message_model(spic,name,txt,key));
           }

           @Override
           public void onChildChanged(DataSnapshot dataSnapshot, String s) {

           }

           @Override
           public void onChildRemoved(DataSnapshot dataSnapshot) {

           }

           @Override
           public void onChildMoved(DataSnapshot dataSnapshot, String s) {

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

        newReply.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(!Foreground.get().isForeground()){
                    notification();
                }
                if(dataSnapshot.hasChild("msg1")) {
                    txt = dataSnapshot.child("msg1").getValue(String.class);
                    //   Toast.makeText(Chatstart.this, txt, Toast.LENGTH_LONG).show();
                }else if(dataSnapshot.hasChild("msg2")){
                    txt = dataSnapshot.child("msg2").getValue(String.class);
                    //   Toast.makeText(Chatstart.this, txt, Toast.LENGTH_LONG).show();
                }
                newrply.child("image").setValue(userData.photo);
                newrply.child("name").setValue(userData.name);
                newrply.child("key").setValue(key);
                newrply.child("msg").setValue(txt);
                //  DatabaseReference newmg=newSnd.push();
                //  newmg.setValue(new Message_model(spic,name,txt,key));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        computeothermessage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    public  void computeothermessage(){
      /*  if(isAppIsInBackground(this)){
            notification();
        }*/
        newnotifchat.setValue(null);
        FirebaseRecyclerAdapter<Chatstartmodel,Chatstartviewholder> firebaserecycleradapter=new FirebaseRecyclerAdapter<Chatstartmodel, Chatstartviewholder>(
                Chatstartmodel.class,
                R.layout.activity_chating_item,
                Chatstartviewholder.class,
                newReply
        ) {
            @Override
            protected void populateViewHolder(final Chatstartviewholder viewHolder, final Chatstartmodel model, int position) {
                //  viewHolder.bindData(model);
               // if(model.getMsg1().length()!=0)
                newnotifchat.setValue(null);
                viewHolder.setMsg1(model.getMsg1());
                viewHolder.setTime1(model.getTime1());
             //   if(model.getMsg2().length()!=0)
                viewHolder.setMsg2(model.getMsg2());
                viewHolder.setTime2(model.getTime2());
              /*  newReply.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("msg2")) {

                        }  if(dataSnapshot.hasChild("msg1")) {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/
            }



        };
        mChat.setAdapter(firebaserecycleradapter);
    }

    public static class Chatreceiverholder extends RecyclerView.ViewHolder{
          View mView;
        public Chatreceiverholder(View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setMsg1(String msg){
            TextView rMessage=(TextView)mView.findViewById(R.id.rMessage);
            rMessage.setText(msg);
        }
    }

    public static class Chatstartviewholder extends RecyclerView.ViewHolder{
        View mView;
        DatabaseReference newnotifchat;
        FirebaseAuth auth;
        public Chatstartviewholder(View itemView) {
            super(itemView);
            mView=itemView;
            auth=FirebaseAuth.getInstance();
            newnotifchat=FirebaseDatabase.getInstance().getReference().child("notificationdata").child("chat").child(auth.getCurrentUser().getUid()).child(key);

         //   newReply=FirebaseDatabase.getInstance().getReference().child("message").child(" ").child(auth.getCurrentUser().getUid());
        }

        public void setMsg1(String msg){
            TextView sMessage=(TextView)mView.findViewById(R.id.rMessage);
            LinearLayout receiver=(LinearLayout)mView.findViewById(R.id.receiver);
            receiver.setVisibility(View.GONE);
            sMessage.setVisibility(View.GONE);
            if(msg!=null)
                receiver.setVisibility(View.VISIBLE);
                sMessage.setVisibility(View.VISIBLE);
            newnotifchat.setValue(null);
            sMessage.setText(msg);
        }
        public void setTime1(String time){
            TextView time1=(TextView)mView.findViewById(R.id.rTime);
            time1.setVisibility(View.GONE);
            if(time!=null){
                time1.setVisibility(View.VISIBLE);
                time1.setText(time);
            }
        }

        public void setMsg2(String msg){
            TextView sMessage=(TextView)mView.findViewById(R.id.sMessage);
            LinearLayout sender=(LinearLayout)mView.findViewById(R.id.sender);
            sender.setVisibility(View.GONE);
            sMessage.setVisibility(View.GONE);
            if(msg!=null)
                sender.setVisibility(View.VISIBLE);
                sMessage.setVisibility(View.VISIBLE);
            newnotifchat.setValue(null);
            sMessage.setText(msg);
        }
        public void setTime2(String time){
            TextView time1=(TextView)mView.findViewById(R.id.sTime);
            time1.setVisibility(View.GONE);
            if(time!=null){
                time1.setVisibility(View.VISIBLE);
                time1.setText(time);
            }
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
    public static String getCurrentTime() {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
    }
    private class computeThread extends Thread {
        public void run() {
            computeothermessage();
        }
    }


}
