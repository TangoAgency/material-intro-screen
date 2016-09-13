package agency.tango.materialintroscreen.animations.wrappers;

import android.view.View;

import agency.tango.materialintroscreen.animations.ViewTranslationWrapper;
import agency.tango.materialintroscreen.animations.translations.AlphaTranslation;
import agency.tango.materialintroscreen.animations.translations.DefaultAlphaTranslation;

public class ViewPagerTranslationWrapper extends ViewTranslationWrapper {
    public ViewPagerTranslationWrapper(View view) {
        super(view);

        this.setDefaultTranslation(new DefaultAlphaTranslation())
                .setExitTranslation(new AlphaTranslation());
    }
}