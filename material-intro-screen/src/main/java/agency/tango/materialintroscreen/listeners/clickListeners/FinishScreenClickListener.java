package agency.tango.materialintroscreen.listeners.clickListeners;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import android.view.View;

public class FinishScreenClickListener implements View.OnClickListener {
    private MaterialIntroActivity activity;

    public FinishScreenClickListener(MaterialIntroActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        activity.onFinish();
        activity.finish();
    }
}