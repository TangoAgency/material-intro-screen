package agency.tango.materialintroscreen.animations.wrappers;

import android.view.View;
import agency.tango.materialintroscreen.animations.ViewTranslationWrapper;
import agency.tango.materialintroscreen.animations.translations.EnterDefaultTranslation;
import agency.tango.materialintroscreen.animations.translations.ExitDefaultTranslation;

public class BackButtonTranslationWrapper extends ViewTranslationWrapper
{
    public BackButtonTranslationWrapper(View view)
    {
        super(view);

        this.setEnterTranslation(new EnterDefaultTranslation())
                .setExitTranslation(new ExitDefaultTranslation());
    }
}