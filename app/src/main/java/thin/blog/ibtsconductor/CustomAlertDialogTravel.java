package thin.blog.ibtsconductor;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Custom Alert Dialog Class used to display travel details to Customer
 */
public class CustomAlertDialogTravel extends Dialog implements View.OnClickListener {
    ImageView statusImage;
    TextView statusMessage;
    Button ok;

    protected CustomAlertDialogTravel(Activity activity, int icon, String message) {
        super(activity);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_alert_dialog_travel);
        ok = (Button) findViewById(R.id.button_dialog);
        statusMessage = (TextView) findViewById(R.id.status_message);
        statusImage = (ImageView) findViewById(R.id.status_image);
        statusImage.setImageResource(icon);
        statusMessage.setText(message);
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_dialog) {
            dismiss();
        }
    }
}
