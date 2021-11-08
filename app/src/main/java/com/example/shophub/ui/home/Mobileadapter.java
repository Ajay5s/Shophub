package com.example.shophub.ui.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shophub.R;
import com.example.shophub.Item_Details.item_details;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Mobileadapter extends RecyclerView.Adapter<Mobileadapter.ViewHolder> {
    List<Itemclass> mobile_list;

    public Mobileadapter(List<Itemclass> mobile_list) {
        this.mobile_list = mobile_list;
    }

    @NonNull
    @Override
    public Mobileadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.mobile_recycler_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Mobileadapter.ViewHolder holder, int position) {
        Picasso.get().load(mobile_list.get(position).getItem_image()).fit().into(holder.images);
        holder.name.setText(mobile_list.get(position).getItem_name());
        holder.price.setText("Rs. "+mobile_list.get(position).getPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(holder.itemView.getContext(), item_details.class);
                intent.putExtra("item_name",""+mobile_list.get(position).getItem_name());
                intent.putExtra("item_image",""+mobile_list.get(position).getItem_image());
                intent.putExtra("item_price",""+mobile_list.get(position).getPrice());
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                int count= Integer.parseInt(mobile_list.get(position).getCount());
                int final_count= count-1;
                if (final_count==0){
                    FirebaseDatabase.getInstance().getReference("users").child(""+uid).child("cart").child(""+mobile_list.get(position).getItem_name()).removeValue();
                    notifyDataSetChanged();

                }
                else {
                    FirebaseDatabase.getInstance().getReference("users").child(""+uid).child("cart").child(""+mobile_list.get(position).getItem_name()).child("count").setValue(""+final_count);

                }
                }
        });
        if (mobile_list.get(position).getCount().equals("")){
            holder.delete.setVisibility(View.GONE);
            holder.quantity.setVisibility(View.GONE);
        }
        else
        {
            holder.quantity.setVisibility(View.VISIBLE);
            holder.quantity.setText(" Quantity: "+mobile_list.get(position).getCount());
            holder.delete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mobile_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView images;
        TextView name, price, quantity;
        ImageButton delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            images= itemView.findViewById(R.id.item_item_image);
            name= itemView.findViewById(R.id.item_item_name);
            price= itemView.findViewById(R.id.price);
            quantity=itemView.findViewById(R.id.quantity);
            delete=itemView.findViewById(R.id.delete);
        }
    }
}
