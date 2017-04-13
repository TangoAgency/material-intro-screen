package agency.tango.materialintroscreen.fragments;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StyleRes;

import static agency.tango.materialintroscreen.fragments.SlideFragment.BACKGROUND_COLOR;
import static agency.tango.materialintroscreen.fragments.SlideFragment.BUTTONS_COLOR;
import static agency.tango.materialintroscreen.fragments.SlideFragment.DESCRIPTION;
import static agency.tango.materialintroscreen.fragments.SlideFragment.DESCRIPTION_STYLE;
import static agency.tango.materialintroscreen.fragments.SlideFragment.IMAGE;
import static agency.tango.materialintroscreen.fragments.SlideFragment.NEEDED_PERMISSIONS;
import static agency.tango.materialintroscreen.fragments.SlideFragment.POSSIBLE_PERMISSIONS;
import static agency.tango.materialintroscreen.fragments.SlideFragment.TITLE;
import static agency.tango.materialintroscreen.fragments.SlideFragment.TITLE_STYLE;

@SuppressWarnings("unused")
public class SlideFragmentBuilder {

    @ColorRes
    private int backgroundColor;

    @ColorRes
    private int buttonsColor;

    @DrawableRes
    private int image;

    @StyleRes
    private int titleStyle;

    @StyleRes
    private int descriptionStyle;

    private String title;
    private String description;
    private String[] neededPermissions;
    private String[] possiblePermissions;

    public SlideFragmentBuilder backgroundColor(@ColorRes int backgroundColor) {
        this.backgroundColor = backgroundColor;
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

    public SlideFragmentBuilder titleStyle(@StyleRes int titleStyle) {
        this.titleStyle = titleStyle;
        return this;
    }

    public SlideFragmentBuilder description(String description) {
        this.description = description;
        return this;
    }

    public SlideFragmentBuilder descriptionStyle(@StyleRes int descriptionStyle) {
        this.descriptionStyle = descriptionStyle;
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
        String missing = "";
        if (backgroundColor == 0) {
            missing += " backgroundColor";
        }
        if (buttonsColor == 0) {
            missing += " buttonsColor";
        }
        if (!missing.isEmpty()) {
            throw new IllegalStateException(
                    "Missing required properties in SlideFragmentBuilder:" + missing);
        }

        Bundle bundle = new Bundle();
        bundle.putInt(BACKGROUND_COLOR, backgroundColor);
        bundle.putInt(BUTTONS_COLOR, buttonsColor);
        bundle.putInt(IMAGE, image);
        bundle.putString(TITLE, title);
        bundle.putInt(TITLE_STYLE, titleStyle);
        bundle.putString(DESCRIPTION, description);
        bundle.putInt(DESCRIPTION_STYLE, descriptionStyle);
        bundle.putStringArray(NEEDED_PERMISSIONS, neededPermissions);
        bundle.putStringArray(POSSIBLE_PERMISSIONS, possiblePermissions);

        return SlideFragment.createInstance(bundle);
    }
}
