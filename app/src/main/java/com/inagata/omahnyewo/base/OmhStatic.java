package com.inagata.omahnyewo.base;

public class OmhStatic {
    //Application key to server
	public static final String PLACE_NAME = "nama";
	public static final String PLACE_PRICE = "harga";
	public static final String CATEGORY = "cat";
	public static final String KEY = "key";
	public static final String LOCATION = "lokasi";
	public static final String DESCRIPTION = "deskripsi";
	public static final String LAT = "lat";
	public static final String LONG = "long";
	public static final String IMAGE = "gambar";
	public static final String COORDINAT = "koordinat";
	
	//link url
	public static final String URL_WS = "http://www.nafian-labs.tk/omahnyewo/servicelist.php?type=";
	public static final String IMAGE_URL_WS = "http://www.nafian-labs.tk/omahnyewo/res/";
	
    //back pressed properties
    public static long back_pressed;
    
	public static final int MILLISECONDS_PER_SECOND = 1000;
	public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
	public static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;
	public static final int FASTEST_INTERVAL_IN_SECONDS = 1;
	public static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND
			* FASTEST_INTERVAL_IN_SECONDS;
	
	public static final String ARG_PAGE = "page";
	public static final String FRAME_DATA = "frameData";

}
