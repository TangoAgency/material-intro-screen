package agency.tango.materialintroscreen.animations;

import androidx.annotation.FloatRange;
import android.view.View;

public interface IViewTranslation {
    void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage);
}
