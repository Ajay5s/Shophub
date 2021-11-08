package com.example.shophub.ui.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shophub.MainActivity;
import com.example.shophub.R;
import com.example.shophub.items_topbar;

import java.util.List;


public class homeadapter extends RecyclerView.Adapter<homeadapter.ViewHolder> {
    List<Itemclass> itemlist;
    public homeadapter(List<Itemclass> itemlist) {
        this.itemlist = itemlist;
    }
    Itemclass itemclass;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.toptab,parent,false);
        return new ViewHolder(view);
    }

    @NonNull


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.item_image.setImageResource(itemlist.get(position).getItem_images());
        holder.item_name.setText(itemlist.get(position).getItem_namest());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (holder.item_name.getText().toString()){
                    case "Mobiles":{
                        Intent intent= new Intent(holder.item_image.getContext(), items_topbar.class);
                        intent.putExtra("name","Mobile");
                        holder.item_image.getContext().startActivity(intent);
                        break;
                    }
                    case "Appliance":{
                        Intent intent= new Intent(holder.item_image.getContext(), items_topbar.class);
                        intent.putExtra("name","Appliance");
                        holder.item_image.getContext().startActivity(intent);
                        break;
                    }
                    case "Cloth":{
                        Intent intent= new Intent(holder.item_image.getContext(), items_topbar.class);
                        intent.putExtra("name","Cloth");
                        holder.item_image.getContext().startActivity(intent);
                        break;
                    }
                    case "Beauty":{
                        Intent intent= new Intent(holder.item_image.getContext(), items_topbar.class);
                        intent.putExtra("name","Beauty");
                        holder.item_image.getContext().startActivity(intent);
                        break;
                    }
                    case "Grocery":{
                        Intent intent= new Intent(holder.item_image.getContext(), items_topbar.class);
                        intent.putExtra("name","Grocery");
                        holder.item_image.getContext().startActivity(intent);
                        break;
                    }
                    case "Watch":{
                        Intent intent= new Intent(holder.item_image.getContext(), items_topbar.class);
                        intent.putExtra("name","Watch");
                        holder.item_image.getContext().startActivity(intent);
                        break;
                    }
                    default:{
                        Toast.makeText(holder.item_image.getContext(),"Hello Javatpoint"+holder.item_name.getText().toString(),Toast.LENGTH_SHORT).show();

                    }
                }

                }
        });
    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_image;
        private TextView item_name;
        private CardView cardView;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_image=itemView.findViewById(R.id.topbar_image);
            item_name=itemView.findViewById(R.id.topbar_text);
            relativeLayout= itemView.findViewById(R.id.item11);

        }
    }
}
