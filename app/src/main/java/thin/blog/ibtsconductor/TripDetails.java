package thin.blog.ibtsconductor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Fragment is used to Display Trip Details of the current Bus
 */
public class TripDetails extends Fragment {
    public TripDetails() {}

    public static TripDetails newInstance() {
        return new TripDetails();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip_details, container, false);
    }

}
