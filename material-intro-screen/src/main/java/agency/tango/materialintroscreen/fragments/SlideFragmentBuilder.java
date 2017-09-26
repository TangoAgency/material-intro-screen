package agency.tango.materialintroscreen.fragments;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import static agency.tango.materialintroscreen.fragments.SlideFragment.BACKGROUND_COLOR;
import static agency.tango.materialintroscreen.fragments.SlideFragment.BUTTONS_COLOR;
import static agency.tango.materialintroscreen.fragments.SlideFragment.DESCRIPTION;
import static agency.tango.materialintroscreen.fragments.SlideFragment.GRANT_PERMISSION_ERROR;
import static agency.tango.materialintroscreen.fragments.SlideFragment.GRANT_PERMISSION_MESSAGE;
import static agency.tango.materialintroscreen.fragments.SlideFragment.IMAGE;
import static agency.tango.materialintroscreen.fragments.SlideFragment.NEEDED_PERMISSIONS;
import static agency.tango.materialintroscreen.fragments.SlideFragment.POSSIBLE_PERMISSIONS;
import static agency.tango.materialintroscreen.fragments.SlideFragment.TITLE;

@SuppressWarnings("unused")
public class SlideFragmentBuilder {

    @ColorRes
    private int backgroundColor;

    @ColorRes
    private int buttonsColor;

    @DrawableRes
    private int image;

    @StringRes
    private int grantPermissionMessage;

    @StringRes
    private int grantPermissionError;

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

    public SlideFragmentBuilder grantPermissionMessage(@StringRes int grantPermissionMessage) {
        this.grantPermissionMessage = grantPermissionMessage;
        return this;
    }

    public SlideFragmentBuilder grantPermissionError(int grantPermissionError) {
        this.grantPermissionError = grantPermissionError;
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
        bundle.putString(DESCRIPTION, description);
        bundle.putStringArray(NEEDED_PERMISSIONS, neededPermissions);
        bundle.putStringArray(POSSIBLE_PERMISSIONS, possiblePermissions);
        bundle.putInt(GRANT_PERMISSION_MESSAGE, grantPermissionMessage);
        bundle.putInt(GRANT_PERMISSION_ERROR, grantPermissionError);

        return SlideFragment.createInstance(bundle);
    }
}
