package com.example.shophub.ui.Order;

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
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    List<Order_class> order_classes;

    public OrderAdapter(List<Order_class> order_classes) {
        this.order_classes = order_classes;
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.mobile_recycler_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(order_classes.get(position).getItem_image()).placeholder(R.drawable.loadings).fit().into(holder.images);
        holder.name.setText(order_classes.get(position).getItem_name());
        holder.price.setText("Rs. "+order_classes.get(position).getPrice());
        holder.quantity.setText("Quantity : "+order_classes.get(position).getCount());
        holder.quantity.setVisibility(View.VISIBLE);
        holder.delete.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(holder.itemView.getContext(),Order_status.class);
                intent.putExtra("name",""+order_classes.get(position).getItem_name());
                intent.putExtra("address",""+order_classes.get(position).getAddress());
                intent.putExtra("pincode",""+order_classes.get(position).getPincode());
                intent.putExtra("image",""+order_classes.get(position).getItem_image());
                intent.putExtra("username",""+order_classes.get(position).getName());
                intent.putExtra("number",""+order_classes.get(position).getCount());
                intent.putExtra("phone",""+order_classes.get(position).getPhone());
                intent.putExtra("quantity",""+order_classes.get(position).getCount());
                intent.putExtra("price",""+order_classes.get(position).getPrice());
                holder.images.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return order_classes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, quantity;
        ImageView images;
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
