package agency.tango.materialintroscreen;


import android.support.annotation.ColorRes;

import agency.tango.materialintroscreen.parallax.ParallaxFragment;

public abstract class SlideFragmentBase extends ParallaxFragment {

    @ColorRes
    public abstract int backgroundColor();

    @ColorRes
    public abstract int buttonsColor();

    public abstract boolean canMoveFurther();

    public abstract String cantMoveFurtherErrorMessage();
}
