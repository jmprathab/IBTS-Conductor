package thin.blog.ibtsconductor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Fragment is used to Display Details about the Application
 */
public class Help extends Fragment {
    WebView webView;

    public Help() {
    }

    public static Help newInstance() {
        return new Help();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_help, container, false);
    }
}
