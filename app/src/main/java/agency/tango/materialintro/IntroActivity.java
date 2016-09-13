package agency.tango.materialintro;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;

public class IntroActivity extends MaterialIntroActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);

        addSlide(new SlideFragmentBuilder()
                .primaryColor(R.color.first_slide_primary)
                .secondaryColor(R.color.first_slide_secondary)
                .image(R.drawable.img_office)
                .title("Organize your time with us")
                .description("Would you try?")
                .messageButtonText("Work with love")
                .messageButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Love with work", Toast.LENGTH_SHORT).show();
                    }
                })
                .build());

        addSlide(new SlideFragmentBuilder()
                .primaryColor(R.color.second_slide_primary)
                .secondaryColor(R.color.second_slide_secondary)
                .title("Want more?")
                .description("Go on")
                .build());

        addSlide(new CustomSlide());

        addSlide(new SlideFragmentBuilder()
                .primaryColor(R.color.third_slide_primary)
                .secondaryColor(R.color.third_slide_secondary)
                .possiblePermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS})
                .neededPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
                .image(R.drawable.img_equipment)
                .messageButtonText("Tools")
                .messageButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Try us", Toast.LENGTH_SHORT).show();
                    }
                })
                .title("We provide best tools")
                .description("ever")
                .build());


        addSlide(new SlideFragmentBuilder()
                .primaryColor(R.color.colorAccent)
                .secondaryColor(R.color.colorPrimary)
                .title("That's it")
                .description("Would you join us?")
                .build());
    }
}
