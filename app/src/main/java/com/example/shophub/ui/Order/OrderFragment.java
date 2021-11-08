package com.example.shophub.ui.Order;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shophub.Checkout_activity;
import com.example.shophub.R;
import com.example.shophub.Sign_in;
import com.example.shophub.ui.home.Itemclass;
import com.example.shophub.ui.home.Mobileadapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {
    RecyclerView order_recycler;
    OrderAdapter adapter;
    List<Order_class> order_list;
    LinearLayoutManager layoutManager;
    View root;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_order, container, false);
        order_recycler=root.findViewById(R.id.orders_recycler);
        FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();

        if (user!=null){
            mobile();
            recycle();
        }
        else {
            Intent intent = new Intent(getActivity(), Sign_in.class);
            intent.putExtra("name","settings");
            startActivity(intent);
        }
        return root;
    }
    private void mobile() {
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        order_list = new ArrayList<>();
        database.getReference().child("users").child(""+uid).child("placed_orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                order_list.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    Order_class item= snapshot1.getValue(Order_class.class);
                    String username= item.getName();
                    String price= item.getPrice();
                    String image= item.getItem_image();
                    String address= item.getAddress();
                    String phone=  item.getPhone();
                    String name= item.getItem_name();
                    String count= item.getCount();
                    String pincode = item.getPincode();
                    order_list.add(new Order_class(""+image,""+name,""+price,""+address,""+phone,""+username,""+count,""+pincode));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void recycle() {
        layoutManager = new LinearLayoutManager(root.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        order_recycler.setLayoutManager(layoutManager);
        adapter = new OrderAdapter(order_list);
        order_recycler.setAdapter(adapter);
    }
    @Override
    public void onStart() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        Itemclass connction = new Itemclass(isConnected);

        if (isConnected == true) {
            String s;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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