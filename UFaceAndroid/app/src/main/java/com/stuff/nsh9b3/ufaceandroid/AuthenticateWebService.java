package com.stuff.nsh9b3.ufaceandroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthenticateWebService extends AppCompatActivity implements OnAsyncTaskComplete, View.OnClickListener
{
    Button btnAuthenticate;

    WebService webService;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate_web_service);

        Intent passedIntent = getIntent();
        Bundle extras = passedIntent.getExtras();

        webService = new WebService(extras.getString(IntentKeys.SERVICE_NAME),
                extras.getString(IntentKeys.SERVICE_ADDRESS),
                extras.getString(IntentKeys.USER_NAME),
                extras.getInt(IntentKeys.USER_INDEX));

        TextView textViewService = (TextView)findViewById(R.id.tv_registered_service_name);
        textViewService.setText(webService.serviceName);

        TextView textViewUser = (TextView)findViewById(R.id.tv_registered_username);
        textViewUser.setText(webService.userName);

        btnAuthenticate = (Button)findViewById(R.id.btn_auth_user);
        btnAuthenticate.setOnClickListener(this);

        BeginAuthentication beginAuthentication = new BeginAuthentication(this, webService);
        beginAuthentication.execute();
    }

    @Override
    public void onClick(View view)
    {
        imagePath = Utilities.takePhoto(this);
    }

    @Override
    public void onTaskCompleted(Object obj)
    {
        String task = "";
        boolean result = false;
        JSONObject jObject = (JSONObject) obj;
        try
        {
            task = jObject.getString(AsyncTaskKeys.GET_TASK);
            result = jObject.getBoolean(AsyncTaskKeys.GET_RESULT);
        } catch(JSONException e)
        {
            e.printStackTrace();
        }
        switch(task)
        {
            case AsyncTaskKeys.AUTH_USER:
                if(result)
                {
                    btnAuthenticate.setEnabled(true);
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Can't begin authentication!", Toast.LENGTH_LONG).show();
                }
                break;
            case AsyncTaskKeys.AUTH_PASS:
                if(result)
                {
                    Toast.makeText(getBaseContext(), "Awaiting Response!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Couldn't Transmit Authentication Password!", Toast.LENGTH_LONG).show();
                }
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
                Bitmap image = Utilities.resizeImage(imagePath);
                int[][] splitImage = Utilities.splitImageIntoSections(image);
                int[][] intFV = LBP.generateFeatureVector(splitImage);
                int[][] splitFV = Utilities.splitFVForEncryption(intFV);
                byte[][] byteFV = Utilities.createByteFV(splitFV);
                String password = Utilities.encryptFV(byteFV);

                AuthenticatePassword authenticatePassword = new AuthenticatePassword(this, webService, password);
                authenticatePassword.execute();
            }
        }
    }
}
