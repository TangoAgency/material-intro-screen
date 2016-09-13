package agency.tango.materialintroscreen.animations.translations;

import android.support.annotation.FloatRange;
import android.view.View;

import agency.tango.materialintroscreen.animations.IViewTranslation;

public class NoTranslation implements IViewTranslation {
    @Override
    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
    }
}