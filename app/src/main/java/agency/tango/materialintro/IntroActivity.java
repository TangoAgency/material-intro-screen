package agency.tango.materialintro;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;

public class IntroActivity extends MaterialIntroActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);

        addSlide(new SlideFragmentBuilder()
                .primaryColor(R.color.colorAccent)
                .secondaryColor(R.color.colorPrimary)
                .image(agency.tango.materialintroscreen.R.drawable.ic_next)
                .title("title 1")
                .description("Description 1")
                .messageButtonText("lol")
                .messageButtonClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Toast.makeText(getApplicationContext(), "I really love android", Toast.LENGTH_SHORT).show();
                    }
                })
                .build());

        addSlide(new SlideFragmentBuilder()
                .primaryColor(R.color.colorPrimary)
                .secondaryColor(R.color.colorAccent)
                .title("title 2")
                .description("Description 2")
                .build());

        addSlide(new CustomSlide());

        addSlide(new SlideFragmentBuilder()
                .primaryColor(R.color.colorPrimary)
                .secondaryColor(R.color.colorAccent)
                .possiblePermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS})
                .neededPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
                .image(agency.tango.materialintroscreen.R.drawable.ic_next)
                .messageButtonText("Android")
                .messageButtonClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Toast.makeText(getApplicationContext(), "Hate eating apples.", Toast.LENGTH_SHORT).show();
                    }
                })
                .title("title 3")
                .description("Description 3")
                .build());


        addSlide(new SlideFragmentBuilder()
                .primaryColor(R.color.colorAccent)
                .secondaryColor(R.color.colorPrimary)
                .title("title 4")
                .description("Description 4")
                .build());
    }

}
