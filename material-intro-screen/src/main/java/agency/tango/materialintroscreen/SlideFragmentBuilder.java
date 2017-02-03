package agency.tango.materialintroscreen;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

@SuppressWarnings("unused")
public class SlideFragmentBuilder {

    @ColorRes
    private int backgroundColor;

    @ColorRes
    private int buttonsColor;

    @DrawableRes
    private int image;

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

    int backgroundColor() {
        return backgroundColor;
    }

    int buttonsColor() {
        return buttonsColor;
    }

    int image() {
        return image;
    }

    String title() {
        return title;
    }

    String description() {
        return description;
    }

    String[] neededPermissions() {
        return neededPermissions;
    }

    String[] possiblePermissions() {
        return possiblePermissions;
    }

    public SlideFragment build() {
        return SlideFragment.createInstance(this);
    }
}
