package com.taocoder.linkcalculator;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.safetynet.SafeBrowsingThreat;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.safetynet.SafetyNetStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Url;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements Validator.ValidationListener {


    @NotEmpty
    @Url
    private AppCompatEditText editQuery;

    private Validator validator;
    private ProgressDialog progressDialog;

    private BottomSheetDialog dialog;

    private static final String KEY = "AIzaSyDIgiIXLDL1HDpMYQfHXQX6J8QSAg844us";
    private String url = "subtitleseeker.com";

    private ProgressBar progressBar;

    public static int foreignLinks = 0;
    public static int nullLinks = 0;
    public static int noLinks = 0;

    private MainActivity activity;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        validator = new Validator(this);
        validator.setValidationListener(this);
        progressDialog = new ProgressDialog(getContext());
        activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        editQuery = (AppCompatEditText) view.findViewById(R.id.editQuery);

        progressBar = (ProgressBar) view.findViewById(R.id.loading);
        final MaterialButton check = (MaterialButton) view.findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        return view;
    }

    @Override
    public void onValidationSucceeded() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.INTERNET}, 20);

        url = editQuery.getText().toString();

        View view = getLayoutInflater().inflate(R.layout.layout_sheet, null);

        List<String> list = new ArrayList<>();
        list.add("Safe Browsing");
        list.add("Content Analysis");
        list.add("Cancel");

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new InnerAdapter(getContext(), list));

        dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(view);
        dialog.show();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        Toast.makeText(getContext(), "Enter valid URL", Toast.LENGTH_LONG).show();
    }

    public void queryURL(String url) {

        progressDialog.show();
        String server = "https://endpoint.apivoid.com/threatlog/v1/pay-as-you-go/?key=6884fa7711ea7633f29f5688a017d1996e9c58ef&host=";
        String URL = server + url;

        progressDialog.setMessage("Please wait...");

        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                Log.i("HOME: ", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    boolean success = jsonObject.getBoolean("success");

                    if (success) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject log = data.getJSONObject("threatlog");

                        ThreatLog threatLog = ThreatLog.getInstance();
                        threatLog.setHost(log.getString("host"));
                        threatLog.setDetected(log.getBoolean("detected"));
                        threatLog.setScanTime(log.getDouble("scantime"));

                        editQuery.setText("");

                        startActivity(new Intent(getContext(), DetailsActivity.class));
                    }
                }
                catch (JSONException e) {

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();
                        Log.i("ERROR HOME: ", error.getMessage());
                    }
                });

        Controller.getInstance().addRequestQueue(stringRequest);
    }

    private void safeBrowse(final String url) {

        SafetyNet.getClient(getContext()).lookupUri(url, KEY, SafeBrowsingThreat.TYPE_POTENTIALLY_HARMFUL_APPLICATION,
                SafeBrowsingThreat.TYPE_SOCIAL_ENGINEERING).addOnSuccessListener(new OnSuccessListener<SafetyNetApi.SafeBrowsingResponse>() {
            @Override
            public void onSuccess(SafetyNetApi.SafeBrowsingResponse safeBrowsingResponse) {
                if (safeBrowsingResponse.getDetectedThreats().isEmpty()) {
                    Utils.showMessage(getContext(), "No Threat");
                }
                else {
                    Utils.showMessage(getContext(), "Wahala wa o");
                }

                for(SafeBrowsingThreat threat: safeBrowsingResponse.getDetectedThreats()) {
                    Log.i("THREAT", threat.getThreatType() + " Type");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ApiException) {

                    ApiException apiException = (ApiException) e;
                    if (apiException.getStatusCode() == SafetyNetStatusCodes.SAFE_BROWSING_API_NOT_INITIALIZED) {
                        Utils.showMessage(getContext(), "Sorry, API error. Try again.");
                    }
                    else {
                        Utils.showMessage(getContext(), e.getMessage());
                    }
                }
                else {
                    Utils.showMessage(getContext(), "Try again.");
                }
            }
        });
    }

    private void parseURL() {

        final String real = url;

        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                int counter = 0;

                String title = "";
                List<String> inner = new ArrayList<>();
                List<String> fore = new ArrayList<>();
                List<String> nulls = new ArrayList<>();

                try {

                    Document document = Jsoup.parse(response);

                    title = document.title();

                    Elements anchors = document.select("a");

                    if (anchors.size() > 0) {

                        for (Element anchor: anchors) {

                            String href = anchor.attr("href");

                            if (href.contains(real.toLowerCase())) {
                                inner.add(href);
                            }
                            else if (href.indexOf(",") == 0 || href.indexOf("#") == 0 || href.indexOf("/") == 0) {
                                nullLinks = 1;
                                nulls.add(href);
                            }
                            else {
                                fore.add(href);
                                foreignLinks = 1;
                            }

                            counter++;
                        }
                    }
                    else {
                        noLinks = 0;
                    }

                    MainActivity.log.setNoLinks(noLinks);
                    MainActivity.log.setForeignLinks(foreignLinks);
                    MainActivity.log.setNullLinks(nullLinks);
                    MainActivity.log.setAllLinks(counter);
                    MainActivity.log.setHost(real);
                    MainActivity.log.setTitle(title);
                    MainActivity.log.setType(1);

                    MainActivity.log.setForeigns(fore);
                    MainActivity.log.setInners(inner);
                    MainActivity.log.setNulls(nulls);

                    activity.showDialog();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Utils.showMessage(getContext(), "Error: The Webpage return Error 505");
                }

                progressBar.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressBar.setVisibility(View.GONE);

                error.printStackTrace();
                Utils.showMessage(getContext(), "Error: The Web page you are trying to analyse is BAD");
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Controller.getInstance().addRequestQueue(stringRequest);
    }


    class InnerAdapter extends RecyclerView.Adapter<InnerAdapter.ListHolder> {

        List<String> strings;

        public InnerAdapter(Context context, List<String> list) {
            strings = list;
        }

        public class ListHolder extends RecyclerView.ViewHolder {

            public TextView textView;
            public ListHolder(View view) {
                super(view);
                textView = (TextView) view.findViewById(R.id.textView);
            }
        }

        @NonNull
        @Override
        public InnerAdapter.ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list, parent, false);
            return new ListHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull InnerAdapter.ListHolder holder, final int position) {
            holder.textView.setText(strings.get(position));
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (strings.get(position).equalsIgnoreCase("cancel")) {
                        dialog.dismiss();
                    }

                    if (strings.get(position).equalsIgnoreCase("safe browsing")) {
                        safeBrowse(url);
                        dialog.dismiss();
                    }

                    if (strings.get(position).equalsIgnoreCase("content analysis")) {
                        parseURL();
                        dialog.dismiss();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return strings.size();
        }
    }
}
