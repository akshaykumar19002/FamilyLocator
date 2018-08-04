package com.kumar.akshay.familylocator;

import com.google.android.gms.maps.model.LatLng;
import java.util.HashMap;

public final class Constants {

    private Constants() { }

    public static final String PACKAGE_NAME = "com.kumar.akshay.geofencingapp";

    public static final String SHARED_PREFERENCES_NAME = PACKAGE_NAME + ".SHARED_PREFERENCES_NAME";

    public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";

    public static final int ADD_GEOFENCE_REQUEST_CODE = 10;

    /**
     * Used to set an expiration time for a geofence. After this amount of time Location Services
     * stops tracking the geofence.
     */
    public static final long GEOFENCE_EXPIRATION_IN_HOURS = 24*365;

    public static final int NUMBER_OF_LANDMARKS = 10;

    /**
     * For this sample, geofences expire after twelve hours.
     */
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;

    public static final float GEOFENCE_RADIUS_IN_METERS = 500; // 1 mile, 1.6 km

    /**
     * Map for storing information about my nearest landmarks.
     */
    public static final HashMap<String, LatLng> LANDMARKS = new HashMap<String, LatLng>();
    static {

        // Hisar, India
        LANDMARKS.put("Phwara Chowk", new LatLng(29.148608, 75.722262));

        //Hansi, India
        LANDMARKS.put("Hansi Bus Stand", new LatLng(29.093870, 75.964670));

        //Village Badli, Haryana, India
        LANDMARKS.put("Badli", new LatLng(28.576437, 76.807678));

        //College PPIMT
        LANDMARKS.put("PPIMT", new LatLng(29.024247, 75.618098));

//        //TCP-2 Hisar Cantt
//        LANDMARKS.put("TCP -2 Hisar", new LatLng(29.114131, 75.817686));

        //Dabra Chowk, Hisar
        LANDMARKS.put("Dabra Chowk", new LatLng(29.138739, 75.736698));

        //Muklan Hisar
        LANDMARKS.put("Muklan Hisar", new LatLng(29.068994, 75.658859));

        //Dhansa Border
        LANDMARKS.put("Dhansa Border", new LatLng(28.563064, 76.841165));

        //Najafgarh, Delhi
        LANDMARKS.put("Najafgarh", new LatLng(28.611917, 76.978646));

        //Dwarka Mor
        LANDMARKS.put("Dwarka Mor", new LatLng(28.619299, 77.033030));

        //Uttam Nagar
        LANDMARKS.put("Uttam Nagar", new LatLng(28.623434, 77.061742));

        //Dhaula Kuan
        LANDMARKS.put("Dhaula Kuan", new LatLng(28.593080, 77.158208));

        //R&R Dental Centre
        LANDMARKS.put("R&R Dental Centre", new LatLng(28.5872063, 77.1554708));

        //Mini Secetariat, Hisar
        LANDMARKS.put("Mini Secetariat, Hisar", new LatLng(29.131874, 75.709845));

        //Rajguru Market, Hisar
        LANDMARKS.put("Rajguru Market", new LatLng(29.162134, 75.721272));

        //Babina, UP
        LANDMARKS.put("Babina, UP", new LatLng(25.248125, 78.447472));

        //Babina Railway Station
        LANDMARKS.put("Babina Railway Station", new LatLng(25.240323, 78.454688));

        //Faridabad Railway Station
        LANDMARKS.put("Faridabad Railway Station", new LatLng(28.410635, 77.307541));

        //Mathura Railway Station
        LANDMARKS.put("Mathura Railway Station", new LatLng(27.479301, 77.673597));

        //Agra Cantt Railway Station
        LANDMARKS.put("Agra Cantt Railway Station", new LatLng(27.157653, 77.989914));

        //Gwalior Railway Station
        LANDMARKS.put("Gwalior Railway Station", new LatLng(26.216177, 78.182054));

        //Dabra Railway Station
        LANDMARKS.put("Dabra Railway Station", new LatLng(25.882043, 78.330341));

        //Datia Railway Station
        LANDMARKS.put("Datia Railway Station", new LatLng(25.641211, 78.4559431));

        //Jhansi Railway Station
        LANDMARKS.put("Jhansi Railway Station", new LatLng(25.445094, 78.552597));

        //New Delhi Railway Station
        LANDMARKS.put("New Delhi Railway Station", new LatLng(28.641338, 77.220892));

    }
}