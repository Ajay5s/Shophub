package com.example.shophub.Item_Details;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shophub.Checkout_activity;
import com.example.shophub.R;
import com.example.shophub.Sign_in;
import com.example.shophub.ui.home.Itemclass;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class item_details extends AppCompatActivity {
    ProgressDialog prog;
    ViewPager viewPager;
    TextView name, price, plus,minus, counts;
    String Name,Price,Image;
    Button buy,add_to_cart;
    FirebaseAuth mauth;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        Image= getIntent().getExtras().getString("item_image");
        Name= getIntent().getExtras().getString("item_name");
        Price= getIntent().getExtras().getString("item_price");
        viewPager=(ViewPager)findViewById(R.id.item_detailed_images);
        name= (TextView) findViewById(R.id.item_detailed_name);
        price= (TextView) findViewById(R.id.item_detailed_price);
        plus= (TextView) findViewById(R.id.add);
        minus= (TextView) findViewById(R.id.minus);
        counts= (TextView) findViewById(R.id.count);
        add_to_cart=(Button)findViewById(R.id.add_cart);
        buy=(Button)findViewById(R.id.buy);
        name.setText(Name);
        price.setText("Rs."+Price);
        viewPagers();
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int final_count= Integer.parseInt(counts.getText().toString())+1;
                counts.setText(""+final_count);
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int final_count= Integer.parseInt(counts.getText().toString());
                if (final_count!=1){
                    int count= final_count-1;
                    counts.setText(""+count);
                }
            }
        });
        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item_count=counts.getText().toString();
                int icount= Integer.parseInt(counts.getText().toString());
                if (user!=null){
                    String path= mauth.getInstance().getCurrentUser().getUid();

                    database.getReference().child("users").child(""+path).child("cart").child(""+Name).child("count").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                int count=  Integer.parseInt(snapshot.getValue().toString())+icount;
                                database.getReference().child("users").child(""+path).child("cart").child(""+Name).child("count").setValue(""+count);
                                Toast.makeText(item_details.this,count+" "+Name+" added into Cart",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Map<String,Object> taskMap = new HashMap<>();
                                taskMap.put("item_name", ""+Name);
                                taskMap.put("item_image", ""+Image);
                                taskMap.put("price", ""+Price);
                                taskMap.put("count",""+item_count);
                                database.getReference().child("users").child(""+path).child("cart").child(""+Name).setValue(taskMap);
                                Toast.makeText(item_details.this,Name+" added into Cart",Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else {
                    Intent intent = new Intent(item_details.this, Sign_in.class);
                    intent.putExtra("name", "item_details");
                    startActivity(intent);
                }
            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item_count=counts.getText().toString();
                if (user!=null){
                    String image= getIntent().getExtras().getString("item_image");
                    String name= getIntent().getExtras().getString("item_name");
                    String price= getIntent().getExtras().getString("item_price");
                    Intent intent= new Intent(item_details.this, Checkout_activity.class);
                    intent.putExtra("name","buy_now");
                    intent.putExtra("item_name",""+name);
                    intent.putExtra("item_image",""+ image);
                    intent.putExtra("price",""+price);
                    intent.putExtra("count",""+item_count);
                    startActivity(intent);
                }
                else {
                    Intent intent= new Intent(item_details.this, Sign_in.class);
                    intent.putExtra("name","item_details");
                    startActivity(intent);
                }
            }
        });
    }
    private void viewPagers() {
        List<String> imagelist= new ArrayList<>();
        imagelist.add(Image);
        imagelist.add(Image);
        imagelist.add(Image);
        item_details_slider image= new item_details_slider(imagelist);
        viewPager.setAdapter(image);
    }
    @Override
    protected void onStart() {
        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        Itemclass connction= new Itemclass(isConnected);

        if(isConnected==true){
            String s;
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(item_details.this);
            builder.setMessage("Please connect your internet connectivity");
            builder.setNegativeButton("Refresh", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onStart();
                }
            });
            builder.show();
        }
        super.onStart();
    }
}