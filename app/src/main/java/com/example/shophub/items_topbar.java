package com.example.shophub;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shophub.ui.home.Itemclass;
import com.example.shophub.ui.home.Mobileadapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class items_topbar extends AppCompatActivity {
    Mobileadapter mobileadapter;
    RecyclerView recyclerView2;
    List<Itemclass> itemclass_other;
    LinearLayoutManager layoutManager;
    String val;
    EditText search2;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mobile);
        val= getIntent().getExtras().getString("name").toLowerCase();
        recyclerView2 = (RecyclerView) findViewById(R.id.mobile_recycler);
        search2=(EditText) findViewById(R.id.searching);
        search2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")){
                }
                else{
                    search_progress(s.toString());
                    recycle();
                }
            }
        });
        if (search2.getText().toString().isEmpty()){
            mobile();
            recycle();
        }
    }
    private void search_progress(String search) {
        itemclass_other = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String item = "" + snapshot1.getKey();
                    if (item.toUpperCase().startsWith(search.toUpperCase())) {
                        FirebaseDatabase.getInstance().getReference("items").child("" + item).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                for (DataSnapshot snapshots : snapshot2.getChildren()) {
                                    Itemclass items_list = snapshots.getValue(Itemclass.class);
                                    String _name = items_list.getItem_name();
                                    String _price = items_list.getPrice();
                                    String _image = items_list.getItem_image();
                                    itemclass_other.add(new Itemclass("" + _image, "" + _name, "" + _price,""));
                                    mobileadapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    FirebaseDatabase.getInstance().getReference("items").child("" + item).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                            for (DataSnapshot snapshots : snapshot2.getChildren()) {
                                Itemclass items_list = snapshots.getValue(Itemclass.class);
                                String _name = items_list.getItem_name();
                                String _price = items_list.getPrice();
                                String _image = items_list.getItem_image();
                                if (_name.toUpperCase().startsWith(search.toUpperCase())) {
                                    itemclass_other.add(new Itemclass("" + _image, "" + _name, "" + _price,""));
                                    mobileadapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void recycle() {
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView2.setLayoutManager(layoutManager);
        mobileadapter = new Mobileadapter(itemclass_other);
        recyclerView2.setAdapter(mobileadapter);
    }

    private void mobile() {
        itemclass_other = new ArrayList<>();
        database.getReference().child("items").child(""+val).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    Itemclass itemclass= snapshot1.getValue(Itemclass.class);
                    String name= itemclass.getItem_name();
                    String price= itemclass.getPrice();
                    String image= itemclass.getItem_image();
                    itemclass_other.add(new Itemclass(""+image,""+name,""+price,""));
                }
                mobileadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
            AlertDialog.Builder builder = new AlertDialog.Builder(items_topbar.this);
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
