package agency.tango.materialintro;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;


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