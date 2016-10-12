package agency.tango.materialintroscreen.animations.wrappers;

import android.view.View;

import agency.tango.materialintroscreen.R;
import agency.tango.materialintroscreen.animations.ViewTranslationWrapper;
import agency.tango.materialintroscreen.animations.translations.DefaultPositionTranslation;
import agency.tango.materialintroscreen.animations.translations.ExitDefaultTranslation;

public class NextButtonTranslationWrapper extends ViewTranslationWrapper {
    public NextButtonTranslationWrapper(View view) {
        super(view);

        setExitTranslation(new ExitDefaultTranslation())
                .setDefaultTranslation(new DefaultPositionTranslation())
                .setErrorAnimation(R.anim.shake_it);
    }
}