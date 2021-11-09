package com.example.shophub.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.shophub.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    homeadapter homeadapter;
    RecyclerView recyclerView, recyclerView2, homrrecycle;
    LinearLayoutManager layoutManager;
    private List<Itemclass> itemlist;
    ViewPager viewPager;
    View root;
    EditText search;
    ItemAdapter itemAdapter;
    int i;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        ScrollView scrollView= root.findViewById(R.id.scroll);
        search=root.findViewById(R.id.searchs);
        recyclerView2= root.findViewById(R.id.search_recycle);
        recyclerView = root.findViewById(R.id.topbar);
        viewPager  = root.findViewById(R.id.item_detailed_images);
        homrrecycle= root.findViewById(R.id.home_recycle);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                itemlist.clear();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")){
                    scrollView.setVisibility(View.VISIBLE);
                    recyclerView2.setVisibility(View.GONE);
                    home_fragment();
                    recycler();
                    homeadapter.notifyDataSetChanged();
                }
                else{
                    scrollView.setVisibility(View.GONE);
                    recyclerView2.setVisibility(View.VISIBLE);
                    search_progress(s.toString());
                    recyclelist();
                }
            }
        });
        home_fragment();
        recycler();
        recyclelist();
        return root;
    }

    private void recyclelist() {
        itemlist = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("items").child("offers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemlist.clear();
                for (DataSnapshot snapshots:snapshot.getChildren()){
                    Itemclass items_list= snapshots.getValue(Itemclass.class);
                    String _name= items_list.getItem_name();
                    String _price= items_list.getPrice();
                    String _image= items_list.getItem_image();
                    itemlist.add(new Itemclass(""+_image,""+_name,""+_price,""));
                    itemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        layoutManager = new LinearLayoutManager(root.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        homrrecycle.setLayoutManager(layoutManager);
        itemAdapter = new ItemAdapter(itemlist);
        homrrecycle.setAdapter(itemAdapter);
    }

    private void search_progress(String search) {
        itemlist= new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    String item=""+snapshot1.getKey();

                    if (item.toUpperCase().startsWith(search.toUpperCase())){
                        FirebaseDatabase.getInstance().getReference("items").child(""+item).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                for (DataSnapshot snapshots:snapshot2.getChildren()){
                                    Itemclass items_list= snapshots.getValue(Itemclass.class);
                                    String _name= items_list.getItem_name();
                                    String _price= items_list.getPrice();
                                    String _image= items_list.getItem_image();
                                    itemlist.add(new Itemclass(""+_image,""+_name,""+_price,""));
                                    itemAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    FirebaseDatabase.getInstance().getReference("items").child(""+item).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                            for (DataSnapshot snapshots:snapshot2.getChildren()){
                                Itemclass items_list= snapshots.getValue(Itemclass.class);
                                String _name= items_list.getItem_name();
                                String _price= items_list.getPrice();
                                String _image= items_list.getItem_image();
                                if (_name.toUpperCase().startsWith(search.toUpperCase())){
                                    itemlist.add(new Itemclass(""+_image,""+_name,""+_price,""));
                                    itemAdapter.notifyDataSetChanged();
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
        layoutManager = new LinearLayoutManager(root.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView2.setLayoutManager(layoutManager);
        itemAdapter = new ItemAdapter(itemlist);
        recyclerView2.setAdapter(itemAdapter);
    }


    private void recycler() {
        layoutManager = new LinearLayoutManager(root.getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        homeadapter = new homeadapter(itemlist);
        recyclerView.setAdapter(homeadapter);

    }

    private void home_fragment() {
        i= 0;
        viewPagers();
        itemlist= new ArrayList<>();
        itemlist.add(new Itemclass(R.drawable.mobiles_,"Mobiles"));
        itemlist.add(new Itemclass(R.drawable.appliance,"Appliance"));
        itemlist.add(new Itemclass(R.drawable.cloths,"Cloth"));
        itemlist.add(new Itemclass(R.drawable.beauty,"Beauty"));
        itemlist.add(new Itemclass(R.drawable.grocery,"Grocery"));
        itemlist.add(new Itemclass(R.drawable.watches,"Watch"));
    }

    private void viewPagers() {
        List<Integer> imagelist= new ArrayList<>();
        imagelist.add(R.drawable.grocery);
        imagelist.add(R.drawable.grocery);
        imagelist.add(R.drawable.grocery);
        imagelist.add(R.drawable.grocery);
        imagelist.add(R.drawable.grocery);
        Imageslider imageslider= new Imageslider(imagelist);
        viewPager.setAdapter(imageslider);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        Itemclass connction = new Itemclass(isConnected);

        if (isConnected == true) {
            String s;
            recyclelist();
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