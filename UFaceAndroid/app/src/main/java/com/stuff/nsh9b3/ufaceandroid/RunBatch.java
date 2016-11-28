package com.stuff.nsh9b3.ufaceandroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Random;

public class RunBatch extends AppCompatActivity implements OnAsyncTaskComplete
{
    TextView tvOrigImage;
    TextView tvTestImage;
    TextView tvCount;

    int count = 1;
    int origIndex = 0;
    int testIndex = 0;
    int endOrigIndex = Configurations.origImages.length;
    int endTextIndex = Configurations.testImages.length;

    WebService service;
    String serviceName;
    String serviceAddress;
    String userName;
    int userIndex;
    String origPassword;
    String testPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_batch);

        //tvOrigImage = (TextView)findViewById(R.id.tv_test_image);
        //tvTestImage = (TextView)findViewById(R.id.tv_orig_image);
        tvCount = (TextView)findViewById(R.id.tv_count);
        //tvOrigImage.setText(Configurations.origImages[origIndex]);
        //tvTestImage.setText(Configurations.testImages[testIndex]);

        startRegistering();
    }

    private void startRegistering()
    {
        serviceName = "Bank";
        serviceAddress = "http://192.168.0.12:3001/";
        userIndex = -1;
        Random rand = new Random();
        userName = "user_" + rand.nextInt() + "_" + origIndex;
        origPassword = generatePassword(Configurations.origImages[origIndex]);
        tvCount.setText("Count: " + count++);

        BeginRegistration beginRegistration = new BeginRegistration(this, serviceAddress, userName, serviceName, userIndex);
        beginRegistration.execute();
    }

    private void startAuthenticating()
    {
        testPassword = generatePassword(Configurations.testImages[testIndex]);

        BeginAuthentication beginAuthentication = new BeginAuthentication(this, service);
        beginAuthentication.execute();
    }

    @Override
    public void onTaskCompleted(Object obj)
    {
        JSONObject jObject = (JSONObject) obj;
        String task = "";
        boolean result = false;
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
            case AsyncTaskKeys.REG_USER:
                if(result)
                {
                    try
                    {
                        userIndex = jObject.getInt(AsyncTaskKeys.USER_INDEX);
                        service = new WebService(serviceName, serviceAddress, userName, userIndex);
                    } catch(JSONException e)
                    {
                        e.printStackTrace();
                    }
                    RegisterPassword registerPassword = new RegisterPassword(this, serviceName, userIndex, origPassword, Configurations.LABELS_IN_FEATURE_VECTOR);
                    registerPassword.execute();
                }
                break;
            case AsyncTaskKeys.REG_PASS:
                if(result)
                {
                    AwaitRegistrationResult awaitRegistrationResult = new AwaitRegistrationResult(this, serviceAddress, userName);
                    awaitRegistrationResult.execute();
                }
                break;
            case AsyncTaskKeys.AWAIT_REG_RESULT:
                if(result)
                {
                    BeginAuthentication beginAuthentication = new BeginAuthentication(this, service);
                    beginAuthentication.execute();
                }
                else
                {
                    boolean checkAgain = false;
                    try
                    {
                        checkAgain = jObject.getBoolean(AsyncTaskKeys.CHECK_AGAIN);
                    } catch(JSONException e)
                    {
                        e.printStackTrace();
                    }

                    if(checkAgain)
                    {
                        AwaitRegistrationResult awaitRegistrationResult = new AwaitRegistrationResult(this, serviceAddress, userName);
                        awaitRegistrationResult.execute();
                    }
                    else
                    {
                        // FAIL
                    }
                }
                break;
            case AsyncTaskKeys.AUTH_USER:
                if(result)
                {
                    testPassword = generatePassword(Configurations.testImages[testIndex]);

                    AuthenticatePassword authenticatePassword = new AuthenticatePassword(this, service, testPassword);
                    authenticatePassword.execute();
                }
                break;
            case AsyncTaskKeys.AUTH_PASS:
                if(result)
                {
                    AwaitAuthenticationResult awaitAuthenticationResult = new AwaitAuthenticationResult(this, service);
                    awaitAuthenticationResult.execute();
                }
                break;
            case AsyncTaskKeys.AWAIT_AUTH_RESULT:
                if(result)
                {
                    testIndex++;
                    if(testIndex == endTextIndex)
                    {
                        testIndex = 0;
                        origIndex++;
                        if(origIndex == endOrigIndex)
                        {
                            // DONE
                            tvCount.setText("Done");
                        }
                        else
                        {
                            // New start image
                            startRegistering();
                        }
                    }
                    else
                    {
                        tvCount.setText(count++);
                        // New test image
                        startAuthenticating();
                    }
                }
                else
                {
                    boolean checkAgain = false;
                    try
                    {
                        checkAgain = jObject.getBoolean(AsyncTaskKeys.CHECK_AGAIN);
                    } catch(JSONException e)
                    {
                        e.printStackTrace();
                    }

                    if(checkAgain)
                    {
                        AwaitAuthenticationResult awaitAuthenticationResult = new AwaitAuthenticationResult(this, service);
                        awaitAuthenticationResult.execute();
                    }
                    else
                    {
                        testIndex++;
                        if(testIndex == endTextIndex)
                        {
                            testIndex = 0;
                            origIndex++;
                            if(origIndex == endOrigIndex)
                            {
                                // DONE
                                tvCount.setText("Done");
                            }
                            else
                            {
                                // New start image
                                startRegistering();
                            }
                        }
                        else
                        {
                            tvCount.setText("Count: " + count++);
                            // New test image
                            startAuthenticating();
                        }
                    }
                }
                break;
        }
    }

    private String generatePassword(String imagePath)
    {
        File file = new File(imagePath);
        String password = null;
        if(file.exists())
        {
            Bitmap image = Utilities.resizeImage(imagePath);
            int[][] splitImage = Utilities.splitImageIntoSections(image);
            int[][] intFV = LBP.generateFeatureVector(splitImage);
            int[][] splitFV = Utilities.splitFVForEncryption(intFV);
            byte[][] byteFV = Utilities.createByteFV(splitFV);
            password =  Utilities.encryptFV(byteFV);
        }
        return password;
    }
}
