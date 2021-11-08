package com.example.shophub.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.shophub.R;

import java.util.List;

public class Imageslider extends PagerAdapter {
    List<Integer> imageitems;

    public Imageslider(List<Integer> imageitems) {
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
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.image,container,false);
        ImageView imageView=view.findViewById(R.id.imageView);
        imageView.setImageResource(imageitems.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
