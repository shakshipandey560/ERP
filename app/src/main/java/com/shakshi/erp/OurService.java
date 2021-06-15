package com.shakshi.erp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

//import com.shakshi.notes.ui.login.LoginActivity;

public class OurService extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_service);

        ListView myListView = findViewById(R.id.myListView);
        final ArrayList<String> ourService = new ArrayList<String>();
        ourService.add("My Cart");
        ourService.add("Login");
        ourService.add("Wishlist");
        ourService.add("Checkout");
        ourService.add("Contact_Us");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ourService);

        myListView.setAdapter(arrayAdapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i) {
                    case 0:
                        Intent cart = new Intent(com.shakshi.erp.OurService.this, com.shakshi.erp.Cart.class);
                        startActivity(cart);
                        break;
                    case 1:
                        Intent login = new Intent(com.shakshi.erp.OurService.this, LogIn.class);
                        startActivity(login);
                        break;
                    case 2 :
                        Intent wishlist  = new Intent(com.shakshi.erp.OurService.this, com.shakshi.erp.Wishlist.class);
                        startActivity(wishlist);
                        break;
                    /*case 3:
                        Intent checkout = new Intent(com.shakshi.erp.OurService.this, com.shakshi.erp.Checkout.class);
                        startActivity(checkout);
                        break;*/
                    case 3:
                        Intent contactus = new Intent(com.shakshi.erp.OurService.this, Contact.class);
                        startActivity(contactus);
                        break;
                }
            }
        });
    }
}