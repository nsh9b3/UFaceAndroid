package com.stuff.nsh9b3.ufaceandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    // List of Buttons (services) a user can select
    public static ArrayList<Button> buttonList;

    // List of layouts (rows of services) to place new services
    private ArrayList<LinearLayout> layoutList;

    // These are offset values so btns and layouts have different IDs
    private final static int btnIDOffset = 1000;
    private final static int layIDOffset = 100;

    public static ArrayList<WebService> serviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the add button and set the listener to this activity (which is a clickListener)
        final Button addButton = (Button)findViewById(R.id.btn_add);
        addButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        // First figure out what was pressed
        switch(view.getId())
        {
            // If the add button was pressed, register a new web service
            case R.id.btn_add:
                Intent newServiceIntent = new Intent(this, SelectNewService.class);
                break;
            // Otherwise, authenticate a user on a specific web service
            default:
                break;
        }
    }

    private void getServices()
    {
        buttonList = new ArrayList<>();
        layoutList = new ArrayList<>();
        serviceList = new ArrayList<>();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Set<String> servList = sharedPref.getStringSet(SharedPrefKeys.SERVICE_LIST, new HashSet<String>());

        for (Iterator<String> it = servList.iterator(); it.hasNext(); ) {
            // Get the name
            String service = it.next();

            // Get the object from the name
            Gson gson = new Gson();
            String json = sharedPref.getString(service, "");
            WebService webService = gson.fromJson(json, WebService.class);

            // Add to list of services
            serviceList.add(webService);

            // Create an icon on the home screen
            makeNewServiceIcon(service);
        }
    }

    private void makeNewServiceIcon(String serviceName)
    {
        LinearLayout parentLayout = (LinearLayout)findViewById(R.id.ll_parent);

        int col = buttonList.size() % 3;

        LinearLayout childLayout;

        // Make a new row to place the button
        if(col == 0)
        {
            childLayout = new LinearLayout(this);
            childLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            childLayout.setLayoutParams(layoutParams);
            childLayout.setWeightSum(3);

            childLayout.setId(layoutList.size() + layIDOffset);

            parentLayout.addView(childLayout);
            layoutList.add(childLayout);
        }
        // Grab an existing location for the button
        else
        {
            childLayout = (LinearLayout)findViewById(layoutList.get((layoutList.size() - 1)).getId());
        }

        Button newServiceBtn = new Button(this);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(new GridView.LayoutParams(0, (int)(getResources().getDisplayMetrics().density * 100 + 0.5f)));
        btnParams.setMargins((int)getResources().getDimension(R.dimen.activity_horizontal_margin),
                (int)getResources().getDimension(R.dimen.activity_vertical_margin),
                (int)getResources().getDimension(R.dimen.activity_horizontal_margin),
                (int)getResources().getDimension(R.dimen.activity_vertical_margin));
        btnParams.weight = 1;

        newServiceBtn.setLayoutParams(btnParams);
        newServiceBtn.setText(serviceName);// + " - " + userID + " - " + userIndex);
        newServiceBtn.setId(buttonList.size() + btnIDOffset);
        newServiceBtn.setOnClickListener(this);

        childLayout.addView(newServiceBtn);
        buttonList.add(newServiceBtn);
    }
}
