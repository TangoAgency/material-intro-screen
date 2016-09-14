package agency.tango.materialintroscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LastEmptySlideFragment extends SlideFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.empty_fragment_slide, container, false);
    }

    @Override
    public int backgroundColor() {
        return R.color.transparent;
    }

    @Override
    public int buttonsColor() {
        return R.color.transparent;
    }
}