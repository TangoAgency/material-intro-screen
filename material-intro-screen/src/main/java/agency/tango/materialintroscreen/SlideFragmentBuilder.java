package agency.tango.materialintroscreen;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SlideFragmentBuilder {
    int backgroundColor;
    int titleColor;
    int descriptionColor;
    int buttonsColor;
    String title;
    String description;
    String[] neededPermissions;
    String[] possiblePermissions;
    int image;

    public SlideFragmentBuilder backgroundColor(@ColorRes int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public SlideFragmentBuilder titleColor(@ColorRes int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public SlideFragmentBuilder descriptionColor(@ColorRes int descriptionColor) {
        this.descriptionColor = descriptionColor;
        return this;
    }

    public SlideFragmentBuilder buttonsColor(@ColorRes int buttonsColor) {
        this.buttonsColor = buttonsColor;
        return this;
    }

    public SlideFragmentBuilder title(String title) {
        this.title = title;
        return this;
    }

    public SlideFragmentBuilder description(String description) {
        this.description = description;
        return this;
    }

    public SlideFragmentBuilder neededPermissions(String[] neededPermissions) {
        this.neededPermissions = neededPermissions;
        return this;
    }

    public SlideFragmentBuilder possiblePermissions(String[] possiblePermissions) {
        this.possiblePermissions = possiblePermissions;
        return this;
    }

    public SlideFragmentBuilder image(@DrawableRes int image) {
        this.image = image;
        return this;
    }

    public SlideFragment build() {
        return SlideFragment.createInstance(this);
    }
}
