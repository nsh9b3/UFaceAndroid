package com.stuff.nsh9b3.ufaceandroid;

/**
 * Created by nick on 11/21/16.
 */

public interface Configurations
{
    int BIT_SIZE_PER_INT = 13;
    int BITS_FOR_ENCRYPTION = 1024;

    boolean USE_EXTRA_SPACE_IN_BIG_INTS = false;

    String UFACE_DATA_ADDRESS = "";
    String UFACE_KEY_ADDRESS = "";

    String UFACE_PUBLIC_KEY = "public_key";
    String UFACE_PUBLIC_KEY_NAME = "Public";
    String UFACE_SERVICE_LIST = "service_list";
    String UFACE_SERVICE_LIST_NAME = "Services";

    int GRID_ROWS = 4;
    int GRID_COLS = GRID_ROWS;
    int GRID_SIZE = GRID_ROWS * GRID_COLS;

    int PIXEL_ROWS = 256;
    int PIXEL_COLS = PIXEL_ROWS;
    int PIXEL_SIZE = PIXEL_ROWS * PIXEL_COLS;

    String IMG1 = "";
    String IMG2 = "";
}
