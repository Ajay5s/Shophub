package com.example.shophub.ui.Cart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
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
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CartFragment extends Fragment {

    Mobileadapter mobileadapter;
    RecyclerView cartrecycler;
    List<Itemclass> itemclass_other;
    LinearLayoutManager layoutManager;
    View root;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    TextView total;
    int totalprice;
    Button checkout;
    ImageButton delete;
    ProgressDialog prog;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_cart, container, false);
        checkout = root.findViewById(R.id.checkout);
        cartrecycler= root.findViewById(R.id.cart_recycler);
        total= root.findViewById(R.id.total_price);
        prog = new ProgressDialog(root.getContext());
        prog.setContentView(R.layout.progressbar_);
        prog.setCancelable(false);
        prog.setMessage("Loading please wait.....");
        prog.setIndeterminate(true);
        prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            prog.show();
            mobile();
            recycle();
            prog.hide();
        }
        else {
            Intent intent = new Intent(getActivity(), Sign_in.class);
            intent.putExtra("name","cart");
            startActivity(intent);
        }
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemclass_other.size()==0){
                    Toast.makeText(getActivity(),"Please add some items to cart",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(getActivity(), Checkout_activity.class);
                    intent.putExtra("name","add_tocart");
                    startActivity(intent);
                }
            }
        });
        return root;
    }


    private void mobile() {
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        itemclass_other = new ArrayList<>();
        database.getReference().child("users").child(""+uid).child("cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemclass_other.clear();
                totalprice=0;
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    Itemclass itemclass= snapshot1.getValue(Itemclass.class);
                    String name= itemclass.getItem_name();
                    String price= itemclass.getPrice();
                    String image= itemclass.getItem_image();
                    int count= Integer.parseInt(itemclass.getCount());
                    int Price= Integer.parseInt(itemclass.getPrice());
                    totalprice= totalprice+Price*count;
                    itemclass_other.add(new Itemclass(""+image,""+name,""+price,""+count));
                }
                mobileadapter.notifyDataSetChanged();
                total.setText(""+totalprice);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void recycle() {
        layoutManager = new LinearLayoutManager(root.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        cartrecycler.setLayoutManager(layoutManager);
        mobileadapter = new Mobileadapter(itemclass_other);
        cartrecycler.setAdapter(mobileadapter);
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