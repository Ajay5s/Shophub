package com.example.shophub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shophub.ui.Order.Order_class;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Checkout_activity extends AppCompatActivity implements PaymentResultListener {
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    Button checkouts;
    Mobileadapter mobileadapter2;
    RecyclerView checkout_recycler;
    List<Itemclass> list;
    LinearLayoutManager layoutManager;
    View root;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    TextView total;
    int totalprice;
    int process=1;
    RadioGroup adress_type;
    EditText editText, name,pincodes, phone;
    RadioButton radioButton, button1, button2;
    String address;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);
        Checkout.preload(getApplicationContext());
        String condition= getIntent().getExtras().getString("name");
        checkouts= (Button)findViewById(R.id.final_checkout);
        checkout_recycler=(RecyclerView)findViewById(R.id.checkout_recycler);
        total= (TextView)findViewById(R.id.final_total_price);
        adress_type=(RadioGroup)findViewById(R.id.radio_group);
        editText = (EditText)findViewById(R.id.user_address);
        name = (EditText)findViewById(R.id.chk_Name);
        pincodes = (EditText)findViewById(R.id.chk_pincode);
        phone = (EditText)findViewById(R.id.chk_phone);
        button1=(RadioButton) findViewById(R.id.radioButton);
        button2=(RadioButton) findViewById(R.id.radioButton2);
        editText.setEnabled(false);



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address_selection();
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address_selection();
            }
        });


        if (condition.equals("buy_now")){
            list= new ArrayList<>();
            process=2;
            String price= getIntent().getExtras().getString("price");
            String name= getIntent().getExtras().getString("item_name");
            String image= getIntent().getExtras().getString("item_image");
            total.setText(""+price);
            list.add(new Itemclass(""+image,""+name,""+price,""));
            recycle();
        }
        else {
            process=1;
            mobile();
            recycle();
        }

        checkouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int address_validation= adress_type.getCheckedRadioButtonId();
                if (address_validation==-1){
                    Toast.makeText(Checkout_activity.this,"Please select address type",Toast.LENGTH_SHORT);
                }
                else if (editText.getText().length()<20){
                    editText.setError("Please enter minimum 20 words" );
                }
                else if (phone.length()!=10){
                    phone.setError("Please enter phone number" );
                }
                else if (name.equals("")){
                    phone.setError("Please enter Your name" );
                }
                else if(pincodes.length()<=5){
                    pincodes.setError("Enter Valid pincode");
                }
                else
                {
                    payment();
                }
            }
        });
    }

    private void address_selection() {
        if (user!=null) {
            i=1;
            int radiogroup= adress_type.getCheckedRadioButtonId();
            radioButton=(RadioButton)findViewById(radiogroup);
            String uid = user.getUid();
            int id= R.id.radioButton;
            if (radiogroup==id) {
                database.getReference("users").child("" + uid).child("details").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            user_details details = snapshot.getValue(user_details.class);
                            String addrress = details.getAddress();
                            String pincode = details.getPincode();
                            String name_= details.getName();
                            String phone_= details.getPhone();
                            if (addrress.isEmpty()||pincode.isEmpty()||name_.isEmpty()||phone_.isEmpty())
                            {
                                Toast.makeText(Checkout_activity.this, "Please fill full details in Profile", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                editText.setText("" + addrress );
                                pincodes.setText(""+pincode);
                                name.setText(""+name_);
                                phone.setText(""+phone_);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                editText.setEnabled(false);
                name.setEnabled(false);
                pincodes.setEnabled(false);
                phone.setEnabled(false);
            }
            else {
                editText.setEnabled(true);
                name.setEnabled(true);
                pincodes.setEnabled(true);
                phone.setEnabled(true);
            }
        }
    }

    private void payment() {
        String femail= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        int amount= Integer.parseInt(total.getText().toString());
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_16LnBhFt4hcCXs");
        checkout.setImage(R.drawable.cloths);
        JSONObject object = new JSONObject();
        try {
            object.put("name", "YOURNAME");
            object.put("description", "modelapp");
            object.put("theme.color", "");
            object.put("currency", "INR");
            object.put("amount", ""+amount*100);
            object.put("prefill.contact", "");
            object.put("prefill.email", ""+femail);
            checkout.open(Checkout_activity.this, object);


        } catch(Exception e) {
            Toast.makeText(Checkout_activity.this,"Success",Toast.LENGTH_SHORT).show();
        }
    }
    private void mobile() {
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        list = new ArrayList<>();
        database.getReference().child("users").child(""+uid).child("cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                totalprice=0;
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    Itemclass itemclass= snapshot1.getValue(Itemclass.class);
                    String name= itemclass.getItem_name();
                    String price= itemclass.getPrice();
                    String image= itemclass.getItem_image();
                    int count= Integer.parseInt(itemclass.getCount());
                    int Price= Integer.parseInt(itemclass.getPrice());
                    totalprice= totalprice + Price*count;
                    list.add(new Itemclass(""+image,""+name,""+price,""+count));
                }
                mobileadapter2.notifyDataSetChanged();
                total.setText(""+totalprice);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void recycle() {
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        checkout_recycler.setLayoutManager(layoutManager);
        mobileadapter2 = new Mobileadapter(list);
        checkout_recycler.setAdapter(mobileadapter2);
    }

    @Override
    public void onPaymentSuccess(String s) {
        int j= 0;
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        Toast.makeText(Checkout_activity.this,"Success",Toast.LENGTH_SHORT).show();
        for (j=0;j<list.size();j++){
            Map<String,Object> taskMap = new HashMap<>();
            taskMap.put("item_name", ""+list.get(j).getItem_name());
            taskMap.put("item_image", ""+list.get(j).getItem_image());
            taskMap.put("price", ""+list.get(j).getPrice());
            taskMap.put("address",""+editText.getText().toString());
            taskMap.put("phone", ""+phone.getText().toString());
            taskMap.put("pincode", ""+pincodes.getText().toString());
            taskMap.put("name", ""+name.getText().toString());
            taskMap.put("count",""+list.get(j).getCount());
            int i = 0;
            database.getReference("users").child(uid).child("placed_orders").child(list.get(j).getItem_name()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Order_class order_class = snapshot.getValue(Order_class.class);
                        String item_name= order_class.getItem_name();
                        int count= Integer.parseInt(order_class.getCount())+1;
                        if (order_class.getAddress().equals(editText.getText().toString())&& order_class.getPhone().equals(phone.getText().toString()) && order_class.getName().equals(name.getText().toString())) {
                            database.getReference("users").child(uid).child("placed_orders").child(item_name).child("count").setValue(""+count);
                        }
                        else {
                            int randome = (int) Math.random();
                            database.getReference("users").child(uid).child("placed_orders").child(item_name + "" + randome).setValue(taskMap);
                        }

                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            database.getReference("users").child(uid).child("placed_orders").child(list.get(j).getItem_name()).setValue(taskMap);
        }
        if(process==1){
            database.getReference("users").child(uid).child("cart").removeValue();
            Toast.makeText(Checkout_activity.this,"Order Placed Successfully",Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            Toast.makeText(Checkout_activity.this,"Order Placed Successfully",Toast.LENGTH_SHORT).show();
            finish();
        }
        /*
        database.getReference().child("users").child(uid).child("details").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    user_details details = snapshot.getValue(user_details.class);
                    String address= details.getAddress();
                    if (address.equals(""));
                    {
                        database.getReference().child("users").child(uid).child("details").child("address").setValue(""+editText.getText().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(Checkout_activity.this,"Failed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(Checkout_activity.this);
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