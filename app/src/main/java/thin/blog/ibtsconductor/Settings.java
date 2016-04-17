package thin.blog.ibtsconductor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Fragment to modify various settings of the Application
 */
public class Settings extends Fragment {

    public Settings() {
    }


    public static Settings newInstance() {
        return new Settings();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
}
