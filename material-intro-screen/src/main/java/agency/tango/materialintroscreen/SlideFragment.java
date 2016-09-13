package agency.tango.materialintroscreen;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agency.tango.materialintroscreen.parallax.ParallaxFragment;

public class SlideFragment extends ParallaxFragment {
    private final static String PRIMARY_COLOR = "primary_color";
    private static final String SECONDARY_COLOR = "secondary_color";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String NEEDED_PERMISSIONS = "needed_permission";
    private static final String POSSIBLE_PERMISSIONS = "possible_permission";
    private static final String IMAGE = "image";
    private static final String MESSAGE_BTN_TEXT = "message_btn_text";
    private static final int PERMISSIONS_REQUEST_CODE = 15621;

    private int primaryColor;
    private int secondaryColor;
    private int image;
    private String title;
    private String description;
    private String messageButtonText;
    private String[] neededPermissions;
    private String[] possiblePermissions;

    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView imageView;
    private View.OnClickListener messageButtonOnClickListener;

    public static SlideFragment createInstance(SlideFragmentBuilder builder) {
        SlideFragment slideFragment = new SlideFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(PRIMARY_COLOR, builder.primaryColor);
        bundle.putInt(SECONDARY_COLOR, builder.secondaryColor);
        bundle.putInt(IMAGE, builder.image);
        bundle.putString(TITLE, builder.title);
        bundle.putString(DESCRIPTION, builder.description);
        bundle.putString(MESSAGE_BTN_TEXT, builder.messageButtonText);
        bundle.putStringArray(NEEDED_PERMISSIONS, builder.neededPermissions);
        bundle.putStringArray(POSSIBLE_PERMISSIONS, builder.possiblePermissions);
        slideFragment.setMessageButtonOnClickListener(builder.messageButtonClickListener);

        slideFragment.setArguments(bundle);
        return slideFragment;
    }

    public static boolean isNotNullOrEmpty(String string) {
        return string != null && string.isEmpty() == false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slide, container, false);
        titleTextView = (TextView) view.findViewById(R.id.txt_title_slide);
        descriptionTextView = (TextView) view.findViewById(R.id.txt_description_slide);
        imageView = (ImageView) view.findViewById(R.id.image_slide);
        initializeView();
        return view;
    }

    public void initializeView() {
        Bundle bundle = getArguments();
        primaryColor = bundle.getInt(PRIMARY_COLOR);
        secondaryColor = bundle.getInt(SECONDARY_COLOR);
        image = bundle.getInt(IMAGE, 0);
        title = bundle.getString(TITLE);
        description = bundle.getString(DESCRIPTION);
        messageButtonText = bundle.getString(MESSAGE_BTN_TEXT);
        neededPermissions = bundle.getStringArray(NEEDED_PERMISSIONS);
        possiblePermissions = bundle.getStringArray(POSSIBLE_PERMISSIONS);

        updateViewWithValues();
    }

    public int primaryColor() {
        return primaryColor;
    }

    public int secondaryColor() {
        return secondaryColor;
    }

    public String messageButtonText() {
        return messageButtonText;
    }

    public View.OnClickListener messageButtonClickListener() {
        return messageButtonOnClickListener;
    }

    public boolean hasAnyPermissionsToGrant() {
        boolean hasPermissionToGrant = hasPermissionsToGrant(neededPermissions);
        if (hasPermissionToGrant == false) {
            hasPermissionToGrant = hasPermissionsToGrant(possiblePermissions);
        }
        return hasPermissionToGrant;
    }

    public boolean hasNeededPermissionsToGrant() {
        return hasPermissionsToGrant(neededPermissions);
    }

    public boolean canMoveFurther() {
        return true;
    }

    public String cantMoveFurtherErrorMessage() {
        return getString(R.string.impassable_slide);
    }

    private void updateViewWithValues() {
        titleTextView.setText(title);
        descriptionTextView.setText(description);

        if (image != 0) {
            imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), image));
            imageView.setVisibility(View.VISIBLE);
        }
    }

    public void askForPermissions() {
        ArrayList<String> notGrantedPermissions = new ArrayList<>();

        if (neededPermissions != null) {
            for (String permission : neededPermissions) {
                if (isNotNullOrEmpty(permission)) {
                    if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                        notGrantedPermissions.add(permission);
                    }
                }
            }
        }
        if (possiblePermissions != null) {
            for (String permission : possiblePermissions) {
                if (isNotNullOrEmpty(permission)) {
                    if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                        notGrantedPermissions.add(permission);
                    }
                }
            }
        }

        String[] permissionsToGrant = removeEmptyAndNullStrings(notGrantedPermissions);
        ActivityCompat.requestPermissions(this.getActivity(), permissionsToGrant, PERMISSIONS_REQUEST_CODE);
    }

    private boolean hasPermissionsToGrant(String[] permissions) {
        if (permissions != null) {
            for (int i = 0; i < permissions.length; i++) {
                if (isNotNullOrEmpty(permissions[i])) {
                    if (ContextCompat.checkSelfPermission(getContext(), permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private String[] removeEmptyAndNullStrings(final ArrayList<String> permissions) {
        List<String> list = new ArrayList<>(permissions);
        list.removeAll(Collections.singleton(null));
        return list.toArray(new String[list.size()]);
    }

    private void setMessageButtonOnClickListener(View.OnClickListener messageButtonOnClickListener) {
        this.messageButtonOnClickListener = messageButtonOnClickListener;
    }
}
