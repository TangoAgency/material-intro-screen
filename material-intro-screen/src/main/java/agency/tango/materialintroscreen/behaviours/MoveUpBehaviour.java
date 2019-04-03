package agency.tango.materialintroscreen.behaviours;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

@SuppressWarnings("unused")
public class MoveUpBehaviour extends CoordinatorLayout.Behavior<LinearLayout> {
    public MoveUpBehaviour(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull LinearLayout child, @NonNull View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull LinearLayout child,
                                          @NonNull View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }
}