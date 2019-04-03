package com.electriccouriers.bass.data;

public final class Globals {

    public static final String API_BASEURL = "http://borchwerfshuttle.tk/api/v1/";
    public static final String DEFAULT_PREFERENCE_SET = "main_preferences";
    public static final String BIOMETRIC_KEY = "1e130119-7530-460b-9e54-681caf27e922";
    public static final String MAPBOX_API_KEY = "pk.eyJ1IjoidGhvbWFzaG9wOCIsImEiOiJjanQ2NmJzZjQwN2R0NDRwaHM1eDRrMDFzIn0.Ots51rbk3rz7Qhfkvd65Uw";

    public static final class PrefKeys {
        public static final String UTOKEN = "shared_preference_user_token_key";
        public static final String MAIN_USER = "shared_preference_user_key";

        public static final String LROUTE_START = "shared_preference_last_route_start_location";
        public static final String LROUTE_END = "shared_preference_last_route_end_location";
        public static final String CROUTE_ID = "shared_preference_current_route_id";
        public static final String CROUTE_ATIME = "shared_preference_current_route_arrival_time";
    }
}
