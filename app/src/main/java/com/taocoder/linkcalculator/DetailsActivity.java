package com.taocoder.linkcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    //private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //sessionManager = new SessionManager(this);

        if (MainActivity.log.getType() == 1) {
            changeFragment(new ContentFragment(), false);
        }
        else if (MainActivity.log.getType() == 0) {
            changeFragment(new DetailsFragment(), false);
        }
        else {
            Utils.showMessage(this, "Unknown operation");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

//        if (item.getItemId() == R.id.syncMenu) {
//            startActivity(new Intent(this, HelpActivity.class));
//        }

        return super.onOptionsItemSelected(item);
    }

    private void changeFragment(Fragment fragment, boolean backTrack) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.container, fragment);

        if (backTrack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

//    private void savePages() {
//        if (sessionManager.isLoggedIn()) {
//            //display confirm to save dialog
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//            builder.setTitle("Save URL");
//            builder.setMessage("Do  You want to save this website to an online REPO?");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    Toast.makeText(DetailsActivity.this, "URL saved", Toast.LENGTH_LONG).show();
//                }
//            });
//            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//
//                }
//            });
//
//            builder.show();
//        }
//        else {
//            // user should login or create an account
//            startActivity(new Intent(this, PagerActivity.class));
//        }
//    }
}