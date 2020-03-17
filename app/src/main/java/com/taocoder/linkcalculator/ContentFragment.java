package com.taocoder.linkcalculator;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment {


    public ContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content, container, false);

        TextView host = (TextView) view.findViewById(R.id.txtHost);
        host.setText(getResources().getString(R.string.host, MainActivity.log.getHost()));

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(getResources().getString(R.string.title, MainActivity.log.getTitle()));

        TextView links = (TextView) view.findViewById(R.id.links);
        links.setText(getResources().getString(R.string.total, String.valueOf(MainActivity.log.getAllLinks())));

        TextView con = (TextView) view.findViewById(R.id.text);
        final MaterialButton button = (MaterialButton) view.findViewById(R.id.open);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open();
            }
        });

        int sum = (MainActivity.log.getForeignLinks() + MainActivity.log.getNoLinks() + MainActivity.log.getNullLinks());

        ThreatLog log = MainActivity.log;
        int divisor = 0;
        divisor = log.getForeigns().size() + log.getNulls().size() + log.getInners().size();
        double ans = 0;

        if (divisor != 0) {
            ans = sum / divisor;
        }

        ans = ans * 100;

        if (ans > 60) {
            con.setText("With the Threshold of 60 percent, the Link you are trying to check is Save for browsing");
        }
        else {
            con.setText("With the Threshold of 60 percent, the Link you are trying to check is may contain some malicious content");
            button.setEnabled(false);
        }

        return view;
    }

    private void open() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage("Do you wish to open this URL in your browser?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + MainActivity.log.getHost()));

                Log.i("RES", MainActivity.log.getHost());
                startActivity(intent);
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }
}