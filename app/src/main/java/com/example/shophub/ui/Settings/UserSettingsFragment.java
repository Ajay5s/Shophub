package com.example.shophub.ui.Settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shophub.MainActivity;
import com.example.shophub.R;
import com.example.shophub.Sign_in;
import com.example.shophub.ui.home.Itemclass;
import com.example.shophub.user_details;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class UserSettingsFragment extends Fragment {

    private SettingViewModel mViewModel;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    TextView name,emailid,phone,address,pincode;
    String uid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {

        View view= inflater.inflate(R.layout.main_fragment, container, false);
        name= view.findViewById(R.id.user_name);
        emailid= view.findViewById(R.id.emailid);
        phone= view.findViewById(R.id.phone);
        address= view.findViewById(R.id.address);
        pincode= view.findViewById(R.id.user_pincode);
        if (user!=null){
            uid= user.getUid();
            getdata();
            name.setEnabled(false);
            emailid.setEnabled(false);
            address.setEnabled(false);
            phone.setEnabled(false);
            pincode.setEnabled(false);
        }
        else {
            Intent intent = new Intent(getActivity(), Sign_in.class);
            intent.putExtra("name","settings");
            startActivity(intent);
        }
         return view;
    }

    private void getdata() {
        FirebaseDatabase.getInstance().getReference("users").child(""+uid).child("details").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    user_details details = snapshot.getValue(user_details.class);
                    name.setText(""+details.getName());
                    address.setText(""+details.getAddress());
                    phone.setText(""+details.getPhone());
                    pincode.setText(""+details.getPincode());
                    emailid.setText(""+details.getEmailid());
                }
                else {
                    name.setText(""+user.getDisplayName());
                    emailid.setText(""+user.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        MenuItem menuItem= menu.findItem(R.id.logout);
        MenuItem edit= menu.findItem(R.id.edit);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finishAffinity();
                return false;
            }
        });
        edit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                CharSequence Title= item.getTitle();
                if (Title.equals("edit")){
                    if (address.getText().length()<20){
                        address.setError("Please enter mininum 20 words" );
                    }
                    item.setIcon(R.drawable.ic_baseline_mode_edit_24);
                    item.setTitle("done");
                    name.setEnabled(false);
                    emailid.setEnabled(false);
                    address.setEnabled(false);
                    phone.setEnabled(false);
                    pincode.setEnabled(false);
                    Map<String,Object> taskMap = new HashMap<>();
                    taskMap.put("name", ""+name.getText().toString());
                    taskMap.put("emailid", ""+emailid.getText().toString());
                    taskMap.put("phone", ""+phone.getText().toString());
                    taskMap.put("address", ""+address.getText().toString());
                    taskMap.put("pincode", ""+pincode.getText().toString());
                    FirebaseDatabase.getInstance().getReference("users").child(""+uid).child("details").setValue(taskMap);
                    return false;
                }
                {
                    item.setIcon(R.drawable.ic_baseline_done_24);
                    item.setTitle("edit");
                    name.setEnabled(true);
                    emailid.setEnabled(true);
                    phone.setEnabled(true);
                    address.setEnabled(true);
                    pincode.setEnabled(true);
                    return false;
                }
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        return super.onOptionsItemSelected(item);
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