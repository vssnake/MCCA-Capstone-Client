package com.vssnake.potlach.main;

/**
 * Created by vssnake on 05/11/2014.
 */
public class SData {
    public static final int NEW_GIFT_NOTIFICATION = 1;
    public static final String NEW_GIFT_EXTRA ="Extra_key";


    public static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1001;
    public static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1002;
    public static final int REQUEST_CODE_TAKE_PHOTO_CAMERA = 1003;
    public static final int REQUEST_CODE_TAKE_PHOTO_SD = 1004;

    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;


    public enum Fragments{
        LoginFragment,
        UserFragment,
        GiftCreatorFragment,
        GiftListFragment,
        GiftViewerFragment,
        SpecialInfoFragment,

    }


}
