package agency.tango.materialintro;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import agency.tango.materialintroscreen.fragments.SlideFragmentBase;

public class CustomSlide extends SlideFragmentBase {

    private CheckBox checkBox;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_custom_slide, container, false);
        checkBox = view.findViewById(R.id.checkBox);
        return view;
    }

    @Override
    public boolean canMoveFurther() {
        return checkBox.isChecked();
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return getString(R.string.error_message);
    }

    @Override
    public int messageButtonColor() {
        return R.color.custom_slide_background;
    }
}