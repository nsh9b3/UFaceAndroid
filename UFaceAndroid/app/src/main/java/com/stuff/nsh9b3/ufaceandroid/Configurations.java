package com.stuff.nsh9b3.ufaceandroid;

/**
 * Created by nick on 11/21/16.
 */

public interface Configurations
{
    int BITS_FOR_ENCRYPTION = 1024;

    boolean USE_EXTRA_SPACE_IN_BIG_INTS = false;

    String UFACE_DATA_ADDRESS = "192.168.0.12:3000";
    String UFACE_KEY_ADDRESS = "192.168.0.12:3002";

    String UFACE_PUBLIC_KEY = "public_key";
    String UFACE_PUBLIC_KEY_NAME = "Public";
    String UFACE_SERVICE_LIST = "service_list";
    String UFACE_SERVICE_LIST_NAME = "Services";
    String SERVICE_ADD_USER = "add_user";
    String SERVICE_ADD_USER_RESULT = "add_user_result_client";
    String SERVICE_USER_KEY = "User";
    String SERVICE_USER_INDEX_KEY = "Index";
    String SERVICE_SERVICE_KEY = "Service";
    String PASSWORD_SIZE_KEY = "Size";
    String PASSWORD_KEY = "Password";
    String UFACE_REGISTRATION_PASSWORD = "add_password";
    String UFACE_AUTHENTICATE_USER = "authenticate_user";
    String UFACE_AUTHENTICATE_PASSWORD = "authenticate_password";
    String UFACE_AUTHENTICATION_RESULT = "authentication_result_client";

    int GRID_ROWS = 4;
    int GRID_COLS = GRID_ROWS;
    int GRID_SIZE = GRID_ROWS * GRID_COLS;

    int IMAGE_PIXEL_ROWS = 256;
    int IMAGE_PIXEL_COLS = IMAGE_PIXEL_ROWS;
    int IMAGE_PIXEL_SIZE = IMAGE_PIXEL_ROWS * IMAGE_PIXEL_COLS;
    
    int SECTION_PIXEL_ROWS = IMAGE_PIXEL_ROWS / GRID_ROWS;
    int SECTION_PIXEL_COLS = IMAGE_PIXEL_COLS / GRID_COLS;
    int SECTION_PIXEL_SIZE = SECTION_PIXEL_ROWS * SECTION_PIXEL_COLS;

    int BINS = 59;
    int NEEDED_BINS = BINS * GRID_SIZE;

    int BITS_ALLOWED_PER_INT = 13;
    int BITS_NEEDED_PER_INT = (int) (Math.ceil(Math.log(SECTION_PIXEL_SIZE) / Math.log(2))) + 1;
    int ZERO_BITS_PER_INT = BITS_ALLOWED_PER_INT - BITS_NEEDED_PER_INT;
    int BYTES_PER_BIG_INT = BITS_FOR_ENCRYPTION / 8;
    int INTS_PER_BIG_INT = BITS_FOR_ENCRYPTION / BITS_ALLOWED_PER_INT;
    int ZERO_BITS_PER_BIG_INT = BITS_FOR_ENCRYPTION % BITS_ALLOWED_PER_INT;
    int LABELS_IN_FEATURE_VECTOR = IMAGE_PIXEL_SIZE - (2 * SECTION_PIXEL_COLS * GRID_COLS) - (2 * SECTION_PIXEL_ROWS * GRID_ROWS) + 4;
    int BIG_INTS_IN_FEATURE_VECTOR = (int)Math.ceil(NEEDED_BINS / (double)INTS_PER_BIG_INT);

    String[] origImages = new String[]{
            "/storage/emulated/0/Android/data/com.stuff.nsh9b3.ufaceandroid/files/Pictures/Nick-Close-1.jpg",
            "/storage/emulated/0/Android/data/com.stuff.nsh9b3.ufaceandroid/files/Pictures/Sahi-Close-1.jpg"
    };
    String[] testImages = new String[]{
            "/storage/emulated/0/Android/data/com.stuff.nsh9b3.ufaceandroid/files/Pictures/Nick-Close-2.jpg",
            "/storage/emulated/0/Android/data/com.stuff.nsh9b3.ufaceandroid/files/Pictures/Nick-Zoom-1.jpg",
            "/storage/emulated/0/Android/data/com.stuff.nsh9b3.ufaceandroid/files/Pictures/Nick-Zoom-2.jpg",
            "/storage/emulated/0/Android/data/com.stuff.nsh9b3.ufaceandroid/files/Pictures/Sahi-Close-2.jpg",
            "/storage/emulated/0/Android/data/com.stuff.nsh9b3.ufaceandroid/files/Pictures/Sahi-Zoom-1.jpg",
            "/storage/emulated/0/Android/data/com.stuff.nsh9b3.ufaceandroid/files/Pictures/Sahi-Zoom-2.jpg"
    };
}
