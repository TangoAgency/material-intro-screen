package agency.tango.materialintroscreen.animations;

import android.view.View;

import androidx.annotation.FloatRange;

public interface IViewTranslation {
    void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage);
}
