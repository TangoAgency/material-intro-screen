package agency.tango.materialintroscreen.listeners;

import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import agency.tango.materialintroscreen.R;
import agency.tango.materialintroscreen.SlideFragment;
import agency.tango.materialintroscreen.adapter.SlidesAdapter;

import static agency.tango.materialintroscreen.SlideFragment.isNotNullOrEmpty;

public class MessageButtonBehaviourOnPageSelected implements IPageSelectedListener {
    private Button messageButton;
    private SlidesAdapter adapter;

    public MessageButtonBehaviourOnPageSelected(Button messageButton, SlidesAdapter adapter) {
        this.messageButton = messageButton;
        this.adapter = adapter;
    }

    @Override
    public void pageSelected(int position) {
        final SlideFragment fragment = adapter.getItem(position);

        if (fragment.hasAnyPermissionsToGrant()) {
            showMessageButton(fragment);
            messageButton.setText(fragment.getActivity().getString(R.string.grant_permissions));
            messageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment.askForPermissions();
                }
            });
        } else if (isNotNullOrEmpty(fragment.messageButtonText())) {
            showMessageButton(fragment);
            messageButton.setText(fragment.messageButtonText());
            messageButton.setOnClickListener(fragment.messageButtonClickListener());
        } else {
            if (messageButton.getVisibility() != View.INVISIBLE) {
                messageButton.startAnimation(AnimationUtils.loadAnimation(fragment.getContext(), R.anim.fade_out));
                messageButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void showMessageButton(SlideFragment fragment) {
        if (messageButton.getVisibility() != View.VISIBLE) {
            messageButton.setVisibility(View.VISIBLE);
            messageButton.startAnimation(AnimationUtils.loadAnimation(fragment.getActivity(), R.anim.fade_in));
        }
    }
}