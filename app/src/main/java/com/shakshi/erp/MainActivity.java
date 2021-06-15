package com.shakshi.erp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    SpaceNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView =findViewById(R.id.space);

        navigationView.initWithSaveInstanceState(savedInstanceState);

        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_library_books_24));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_notifications_24));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_shopping_cart_24));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_account_circle_24));

        navigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Intent home = new Intent(MainActivity.this, MainActivity.class);
                startActivity(home);
                navigationView.setCentreButtonSelectable(true);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                switch(itemIndex) {
                    case 0:
                        Intent lib = new Intent(MainActivity.this, Library.class);
                        startActivity(lib);
                        break;
                    case 1:
                        Intent notification = new Intent(MainActivity.this, Notification.class);
                        startActivity(notification);
                        break;
                    case 2:
                        Intent cart = new Intent(MainActivity.this, Cart.class);
                        startActivity(cart);
                        break;
                    case 3:
                        Intent profile = new Intent(MainActivity.this, Profile.class);
                        startActivity(profile);
                        break;
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                switch(itemIndex) {
                    case 0:
                        Intent lib = new Intent(MainActivity.this, Library.class);
                        startActivity(lib);
                        break;
                    case 1:
                        Intent notification = new Intent(MainActivity.this, Notification.class);
                        startActivity(notification);
                        break;
                    case 2:
                        Intent cart = new Intent(MainActivity.this, Cart.class);
                        startActivity(cart);
                        break;
                    case 3:
                        Intent profile = new Intent(MainActivity.this, Profile.class);
                        startActivity(profile);
                        break;
                }
            }
        });

    }

    public void showPopup(View view){
        PopupMenu popup  = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.app_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.aboutUs:
                startActivity(new Intent(this, About_Us.class));
                return true;
            case R.id.ourService:
                startActivity(new Intent(this, OurService.class));
                return true;
            case R.id.newsLetter:
                startActivity(new Intent(this, Newsletter.class));
                return true;
            case R.id.conTact:
                startActivity(new Intent(this, Contact.class));
                return true;
            case R.id.logOut:
                startActivity(new Intent(this, LogIn.class));
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

}