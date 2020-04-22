package agency.tango.materialintroscreen.parallax;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.LinkedList;
import java.util.Queue;

public class ParallaxFragment extends Fragment implements Parallaxable {
    @Nullable
    private Parallaxable parallaxLayout;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        parallaxLayout = findParallaxLayout(view);
    }

    public Parallaxable findParallaxLayout(View root) {
        Queue<View> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            View child = queue.remove();

            if (child instanceof Parallaxable) {
                return (Parallaxable) child;
            } else if (child instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) child;
                for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
                    queue.add(viewGroup.getChildAt(i));
                }
            }
        }
        return null;
    }

    @Override
    public void setOffset(@FloatRange(from = -1.0, to = 1.0) float offset) {
        if (parallaxLayout != null) {
            parallaxLayout.setOffset(offset);
        }
    }
}
