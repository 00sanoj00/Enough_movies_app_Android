package com.sanoj.devildeveloper.enoughmovies.Config;

import android.graphics.Color;

import com.sanoj.devildeveloper.enoughmovies.R;

public class Config {
//---------------//------------------//--------------------//-----------------//---------------------//----------------//-------------------//----------------//

    public static final String mMainURL = "https://m.imdb.com/feature/genre";
    public static final String mErrorURL = "";
    public static final String mStrimingURl = "https://vidclouds.us/";


    /** Webview Handle and change */
    public static final String[] LINKS_OPENED_IN_EXTERNAL_BROWSER = {
            "target=blank",
            "target=external",
            "play.google.com/store",
            "youtube.com/watch",
            "facebook.com/sharer",
            "twitter.com/share",
            "plus.google.com/share"
    };

    public static final boolean OPEN_LINKS_IN_EXTERNAL_BROWSER = true;

    public static final String[] LINKS_OPENED_IN_INTERNAL_WEBVIEW = {
            "target=webview",
            "target=internal",
            "imdb.com",
            "m.imdb.com",
            "www.2embed.ru",
            "vidembed.cc",
            "sbplay1.com",
            "dood.ws",
            "mixdrop.co"
    };

    public static final String[] DOWNLOAD_FILE_TYPES = {
            ".*zip$", ".*rar$", ".*pdf$", ".*doc$", ".*xls$",
            ".*mp3$", ".*wma$", ".*ogg$", ".*m4a$", ".*wav$",
            ".*avi$", ".*mov$", ".*mp4$", ".*mpg$", ".*3gp$",".*jpg$",".*png$",
            ".*drive.google.com.*download.*",
            ".*dropbox.com/s/.*"
    };

    public static final boolean GEOLOCATION = true;
    /** --------------- END --------------- */

//---------------//------------------//--------------------//-----------------//---------------------//----------------//-------------------//----------------//
    public static int parseColor (String colorString){
        return Color.parseColor(colorString);
    }

}
