package thin.blog.ibtsconductor;

/**
 * Class which has values of all URL address and Constant Values
 */
class Constants {
    public static final String SHARED_PREFS_USER_DATA = "user_data";
    public static final String USER_DATA_OBJECT = "user_data_object";
    public static final String SUCCESSFUL_LOGIN_HISTORY = "successful_login_history";
    public static final String SHARED_PREFS_NUMBER_OF_PASSENGERS = "number_of_passengers";
    public static final String SHARED_PREFS_BUS_LIST = "bus_list";
    public static final String SHARED_PREFS_STOP_LIST = "stop_list";
    public static final String SHARED_PREFS_BUS_SELECTION = "bus_selection";
    public static final String SHARED_PREFS_STOP_SELECTION = "stop_selection";
    public static final String SHARED_PREFS_TRAVEL_LIST = "travel_list";
    public static final String SHARED_PREFS_CURRENT_BUS_NAME = "current_bus_name";
    public static final String SHARED_PREFS_CURRENT_STOP_NAME = "current_stop_name";
    //URL Addresses for Server
    public static final String LOGIN;
    public static final String BUS_LIST;
    public static final String BUS_DETAILS;
    public static final String TRAVEL;
    //for testing
    //set localhost = false for testing from webhost
    private static final Boolean localhost = true;
    private static final String ADDRESS;

    static {
        if (localhost) {
            ADDRESS = "http://192.168.1.5/conductor/";
        } else {
            ADDRESS = "http://www.ibts.conductor.com/";
        }
        LOGIN = ADDRESS + "login.php";
        BUS_LIST = ADDRESS + "buslist.php";
        BUS_DETAILS = ADDRESS + "busdetails.php";
        TRAVEL = ADDRESS + "travel.php";
    }
}
