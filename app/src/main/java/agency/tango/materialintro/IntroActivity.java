package agency.tango.materialintro;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.animations.IViewTranslation;
import agency.tango.materialintroscreen.behaviours.MessageButtonBehaviour;
import agency.tango.materialintroscreen.fragments.SlideFragmentBuilder;
import agency.tango.materialintroscreen.listeners.click.MessageButtonClickListener;
import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;

public class IntroActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);
        setSkipButtonVisible();

        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view,
                                          @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.first_slide_background)
                        .buttonsColor(R.color.first_slide_buttons)
                        .image(R.drawable.img_office)
                        .title("Organize your time with us")
                        .description("Would you try?")
                        .messageButtonColor(R.color.first_slide_buttons)
                        .messageButtonTextColor(R.color.white)
                        .build(),
                new MessageButtonBehaviour(new MessageButtonClickListener() {
                    @Override
                    public void onClick(Button messageButton) {
                        messageButton.setText("Click me once again!");
                        showMessage("We provide solutions to make you love your work");
                    }
                }, "Work with love"));

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.second_slide_background)
                .buttonsColor(R.color.second_slide_buttons)
                .title("Want more?")
                .description("Go on")
                .messageButtonColor(R.color.second_slide_buttons)
                .messageButtonTextColor(R.color.white)
                .build(),
                new MessageButtonBehaviour(new MessageButtonClickListener() {
                    @Override
                    public void onClick(Button messageButton) {
                        //That button should do nothing at click
                    }
                }, "Let's go!"));

        addSlide(new CustomSlide());

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.third_slide_background)
                        .buttonsColor(R.color.third_slide_buttons)
                        .possiblePermissions(
                                new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS})
                        .neededPermissions(new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION})
                        .image(R.drawable.img_equipment)
                        .grantPermissionMessage(R.string.txt_pls_grant_permission)
                        .grantPermissionError(R.string.txt_grant_permission_error)
                        .messageButtonColor(R.color.third_slide_buttons)
                        .messageButtonTextColor(R.color.white)
                        .title("We provide best tools")
                        .description("ever")
                        .build(),
                new MessageButtonBehaviour(new MessageButtonClickListener() {
                    @Override
                    public void onClick(Button messageButton) {
                        showMessage("Try us!");
                    }
                }, "Tools"));

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.fourth_slide_background)
                .buttonsColor(R.color.fourth_slide_buttons)
                .title("That's it")
                .description("Would you join us?")
                .build());
    }

    @Override
    public void onLastSlidePassed() {
        Toast.makeText(this, "Try this library in your project! :)", Toast.LENGTH_SHORT).show();
    }
}