package agency.tango.materialintroscreen.parallax;

import androidx.annotation.FloatRange;

public interface Parallaxable {
    void setOffset(@FloatRange(from = -1.0, to = 1.0) float offset);
}