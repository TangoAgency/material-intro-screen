package agency.tango.materialintroscreen.fragments;

import android.os.Bundle;

import agency.tango.materialintroscreen.R;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import static agency.tango.materialintroscreen.fragments.SlideFragment.BACKGROUND_COLOR;
import static agency.tango.materialintroscreen.fragments.SlideFragment.BUTTONS_COLOR;
import static agency.tango.materialintroscreen.fragments.SlideFragment.DESCRIPTION;
import static agency.tango.materialintroscreen.fragments.SlideFragment.GRANT_PERMISSION_ERROR;
import static agency.tango.materialintroscreen.fragments.SlideFragment.GRANT_PERMISSION_MESSAGE;
import static agency.tango.materialintroscreen.fragments.SlideFragment.IMAGE;
import static agency.tango.materialintroscreen.fragments.SlideFragment.MESSAGE_BACKGROUND_COLOR;
import static agency.tango.materialintroscreen.fragments.SlideFragment.MESSAGE_TEXT_COLOR;
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
    private int grantPermissionMessage = R.string.mis_grant_permissions;

    @StringRes
    private int grantPermissionError = R.string.mis_please_grant_permissions;

    @ColorRes
    private int messageButtonTextColor = R.color.mis_default_message_button_text_color;

    @ColorRes
    private int messageButtonColor = R.color.mis_default_message_button_color;

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

    public SlideFragmentBuilder grantPermissionError(@StringRes int grantPermissionError) {
        this.grantPermissionError = grantPermissionError;
        return this;
    }

    public SlideFragmentBuilder messageButtonTextColor(@ColorRes int messageButtonTextColor) {
        this.messageButtonTextColor = messageButtonTextColor;
        return this;
    }

    public SlideFragmentBuilder messageButtonColor(@ColorRes int messageButtonColor) {
        this.messageButtonColor = messageButtonColor;
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
        bundle.putInt(MESSAGE_BACKGROUND_COLOR, messageButtonColor);
        bundle.putInt(MESSAGE_TEXT_COLOR, messageButtonTextColor);

        return SlideFragment.createInstance(bundle);
    }
}
