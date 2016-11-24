package com.stuff.nsh9b3.ufaceandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class RegisterWebService extends AppCompatActivity implements TextWatcher, View.OnClickListener, OnAsyncTaskComplete
{
    private Button btnRegister;
    private Button btnValidate;
    private EditText etUserID;
    private ProgressBar pbValidMark;

    private String webServiceAddress;
    private String webServiceName;
    private Map<String, Integer> userIDs;
    private String userID;
    private int userIndex;

    boolean checkName = false;
    boolean validName = false;
    boolean registerUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_web_service);

        Intent passedIntent = getIntent();
        Bundle extras = passedIntent.getExtras();
        webServiceAddress = extras.getString(IntentKeys.SERVICE_ADDRESS);
        webServiceName = extras.getString(IntentKeys.SERVICE_NAME);

        userIDs = new HashMap<>();

        btnRegister = (Button)findViewById(R.id.btn_register_take_photo);
        btnRegister.setOnClickListener(this);

        btnValidate = (Button)findViewById(R.id.btn_register_check_name);
        btnValidate.setOnClickListener(this);

        TextView tvServiceName = (TextView)findViewById(R.id.tv_register_service_name);
        tvServiceName.setText(webServiceName);

        etUserID = (EditText)findViewById(R.id.et_register_user_id);
        etUserID.addTextChangedListener(this);

        pbValidMark = (ProgressBar)findViewById(R.id.pb_register_is_valid);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {
        pbValidMark.setVisibility(View.GONE);
        btnRegister.setEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable editable)
    {

    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.btn_register_check_name:
                userID = etUserID.getText().toString();

                // If it's in the list already, it's already been checked
                if (userIDs.containsKey(userID))
                {
                    btnRegister.setEnabled(true);
                    Toast.makeText(getBaseContext(), "The name %s has already been verified as valid.", Toast.LENGTH_LONG).show();
                } else
                {
                    // Otherwise, check the name to see if it's valid
                    checkName = true;

                    CheckValidName checkValidName = new CheckValidName(this, webServiceAddress, userID, webServiceName, userIndex, validName);
                    checkValidName.execute();
                }
                break;
            case R.id.btn_register_take_photo:
                registerUser = true;
                userIndex = userIDs.get(userID);
                Utilities.takePhoto(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IntentKeys.REQUEST_TAKE_PHOTO)
        {
            if(resultCode == RESULT_OK)
            {
                // TODO: shrink photo
                // TODO: create fv
                // TODO: encrypt
                // TODO: send info off
            }
        }
    }


    @Override
    public void onTaskCompleted(Object obj)
    {
        if(checkName)
        {
            if(validName)
            {
                btnRegister.setEnabled(true);
                userIDs.put(userID, userIndex);
            }
            else
            {
                userIDs.remove(userID);
                Toast.makeText(getBaseContext(), String.format("The name %s is already in use.", userID), Toast.LENGTH_LONG).show();
            }
            validName = false;
        }
        else
        {
            if(registerUser)
            {

            }
            else
            {

            }
            registerUser = false;
        }
    }
}
