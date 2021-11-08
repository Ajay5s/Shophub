package com.example.shophub.Item_Details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.shophub.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class item_details_slider extends PagerAdapter {
    List<String> imageitems;

    public item_details_slider(List<String> imageitems) {
        this.imageitems = imageitems;
    }

    @Override
    public int getCount() {
        return imageitems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view ==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_details,container,false);
        ImageView imageView=view.findViewById(R.id.item_details_image);
        Picasso.get().load(imageitems.get(position)).into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
