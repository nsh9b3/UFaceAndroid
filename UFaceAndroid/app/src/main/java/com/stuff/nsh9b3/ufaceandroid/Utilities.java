package com.stuff.nsh9b3.ufaceandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nick on 11/23/16.
 */

public class Utilities
{
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static String takePhoto(Activity activity)
    {
        // Create the File where the photo should go
        File photoFile = null;

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getBaseContext().getPackageManager()) != null)
        {
            try
            {
                photoFile = Utilities.createImageFile(activity.getBaseContext());
            } catch (IOException ex)
            {
                // Error occurred while creating the File
            }

            // Continue only if the File was successfully created
            if (photoFile.exists())
            {
                // Take a picture and place the information in the newly created file
                Uri photoURI = FileProvider.getUriForFile(activity.getBaseContext().getApplicationContext(),
                        "com.stuff.nsh9b3.ufaceandroid.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(takePictureIntent, IntentKeys.REQUEST_TAKE_PHOTO);
            }
        }
        return photoFile.getAbsolutePath();
    }

    // Creates a temporary image
    public static File createImageFile(Context that) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = that.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static int[][] convertImageToFV(Bitmap bitmap)
    {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int gridSize = Configurations.GRID_SIZE;
        int gridWidth = Configurations.GRID_COLS;
        int gridHeight = Configurations.GRID_ROWS;
        int sectionWidth = bitmapWidth/gridWidth;
        int sectionHeight = bitmapHeight/gridHeight;
        int[][] pixelMap = new int[gridSize][sectionWidth*sectionHeight];

        for(int i = 0; i < gridSize; i++)
        {
            int[] secPixels = new int[sectionWidth*sectionHeight];
            bitmap.getPixels(secPixels, 0, sectionWidth, (i % Configurations.GRID_COLS) * sectionWidth, (i / Configurations.GRID_ROWS) * sectionHeight, sectionWidth, sectionHeight);
            pixelMap[i] = secPixels;
        }

        return pixelMap;
    }

    public static Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public static Bitmap resizeImage(String imagePath)
    {
        File file = new File(imagePath);
        Bitmap origBitmap = BitmapFactory.decodeFile(imagePath);
        Bitmap grayBitmap = toGrayscale(origBitmap);
        Bitmap resizeBitmap = Bitmap.createScaledBitmap(grayBitmap, Configurations.IMAGE_PIXEL_COLS, Configurations.IMAGE_PIXEL_ROWS, true);

        return resizeBitmap;
    }
}
