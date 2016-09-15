package agency.tango.materialintro;

import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();

        TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(new Intent(this, MainActivity.class))
                .addNextIntent(new Intent(this, IntroActivity.class))
                .startActivities();
    }
}