package com.example.shophub.ui.Order;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shophub.R;
import com.example.shophub.ui.Settings.UserSettingsFragment;
import com.squareup.picasso.Picasso;

public class Order_status extends AppCompatActivity {
    String name, address, pincode, phone, number, item_image, price,item_name;
    TextView itemname,details, Price;
    ImageView images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_status);
        item_name = getIntent().getExtras().getString("name");
        name = getIntent().getExtras().getString("username");
        address = getIntent().getExtras().getString("address");
        pincode = getIntent().getExtras().getString("pincode");
        phone = getIntent().getExtras().getString("phone");
        number = getIntent().getExtras().getString("quantity");
        item_image = getIntent().getExtras().getString("image");
        price = getIntent().getExtras().getString("price");
        itemname= (TextView)findViewById(R.id.order_name);
        details= (TextView)findViewById(R.id.details);
        Price=(TextView)findViewById(R.id.Price);
        images=(ImageView)findViewById(R.id.order_image);
        itemname.setText(""+item_name);
        details.setText("Name  : "+name+"\nPhone Number :  "+phone+"\n"+"Address :"+address+"\n"+pincode);
        Picasso.get().load(""+item_image).into(images);
        int count= Integer.parseInt(number)*Integer.parseInt(price);
        Price.setText("Rs."+count);

    }
}