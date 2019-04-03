package agency.tango.materialintroscreen.listeners;

import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import agency.tango.materialintroscreen.R;
import agency.tango.materialintroscreen.adapter.SlidesAdapter;
import agency.tango.materialintroscreen.behaviours.MessageButtonBehaviour;
import agency.tango.materialintroscreen.fragments.SlideFragmentBase;

public class MessageButtonBehaviourOnPageSelected implements IPageSelectedListener {
    private final Button messageButton;
    private final SlidesAdapter adapter;
    private final SparseArray<MessageButtonBehaviour> messageButtonBehaviours;

    public MessageButtonBehaviourOnPageSelected(Button messageButton, SlidesAdapter adapter,
                                                SparseArray<MessageButtonBehaviour> messageButtonBehaviours) {
        this.messageButton = messageButton;
        this.adapter = adapter;
        this.messageButtonBehaviours = messageButtonBehaviours;
    }

    @Override
    public void pageSelected(final int position) {
        final SlideFragmentBase slideFragment = adapter.getItem(position);

        if (slideFragment.hasAnyPermissionsToGrant()) {
            showMessageButton(slideFragment);
            messageButton.setText(
                    slideFragment.getString(adapter.getItem(position).grantPermissionStringRes()));
            messageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    slideFragment.askForPermissions();
                }
            });
        } else if (checkIfMessageButtonHasBehaviour(position)) {
            showMessageButton(slideFragment);
            messageButton.setText(messageButtonBehaviours.get(position).getMessageButtonText());
            messageButton
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            messageButtonBehaviours.get(position).getClickListener().onClick(messageButton);
                        }
                    });
        } else if (messageButton.getVisibility() != View.INVISIBLE) {
            messageButton.startAnimation(
                    AnimationUtils.loadAnimation(slideFragment.getContext(), R.anim.mis_fade_out));
            messageButton.setVisibility(View.INVISIBLE);
        }
    }

    private boolean checkIfMessageButtonHasBehaviour(int position) {
        return messageButtonBehaviours.get(position) != null && !TextUtils.isEmpty(
                messageButtonBehaviours.get(position).getMessageButtonText());
    }

    private void showMessageButton(final SlideFragmentBase fragment) {
        if (messageButton.getVisibility() != View.VISIBLE) {
            messageButton.setVisibility(View.VISIBLE);
            if (fragment.getActivity() != null) {
                messageButton.startAnimation(
                        AnimationUtils.loadAnimation(fragment.getActivity(), R.anim.mis_fade_in));

            }
        }
    }
}