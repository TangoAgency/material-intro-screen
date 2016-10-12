package agency.tango.materialintroscreen.animations.wrappers;

import android.view.View;

import agency.tango.materialintroscreen.animations.ViewTranslationWrapper;
import agency.tango.materialintroscreen.animations.translations.DefaultPositionTranslation;
import agency.tango.materialintroscreen.animations.translations.ExitDefaultTranslation;

public class PageIndicatorTranslationWrapper extends ViewTranslationWrapper {
    public PageIndicatorTranslationWrapper(View view) {
        super(view);

        setDefaultTranslation(new DefaultPositionTranslation())
                .setExitTranslation(new ExitDefaultTranslation());
    }
}