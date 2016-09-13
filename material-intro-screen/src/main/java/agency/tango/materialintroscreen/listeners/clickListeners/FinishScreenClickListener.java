package agency.tango.materialintroscreen.listeners.clickListeners;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class FinishScreenClickListener implements View.OnClickListener {
    private AppCompatActivity activity;

    public FinishScreenClickListener(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        activity.finish();
    }
}