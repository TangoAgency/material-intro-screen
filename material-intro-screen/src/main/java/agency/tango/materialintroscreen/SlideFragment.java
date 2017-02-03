package agency.tango.materialintroscreen;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
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

public class SlideFragment extends SlideFragmentBase {

    public static final String BACKGROUND_COLOR = "background_color";
    public static final String BUTTONS_COLOR = "buttons_color";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String NEEDED_PERMISSIONS = "needed_permission";
    public static final String POSSIBLE_PERMISSIONS = "possible_permission";
    public static final String IMAGE = "image";
    private static final int PERMISSIONS_REQUEST_CODE = 15621;

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

    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView imageView;

    public static SlideFragment createInstance(Bundle bundle) {
        SlideFragment slideFragment = new SlideFragment();
        slideFragment.setArguments(bundle);
        return slideFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mis_fragment_slide, container, false);
        titleTextView = (TextView) view.findViewById(R.id.txt_title_slide);
        descriptionTextView = (TextView) view.findViewById(R.id.txt_description_slide);
        imageView = (ImageView) view.findViewById(R.id.image_slide);
        initializeView();
        return view;
    }

    @Override
    @ColorRes
    public int backgroundColor() {
        return backgroundColor;
    }

    @Override
    @ColorRes
    public int buttonsColor() {
        return buttonsColor;
    }

    @Override
    public boolean canMoveFurther() {
        return true;
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return getString(R.string.mis_impassable_slide);
    }

    public boolean hasAnyPermissionsToGrant() {
        boolean hasPermissionToGrant = hasPermissionsToGrant(neededPermissions);
        if (!hasPermissionToGrant) {
            hasPermissionToGrant = hasPermissionsToGrant(possiblePermissions);
        }
        return hasPermissionToGrant;
    }

    public boolean hasNeededPermissionsToGrant() {
        return hasPermissionsToGrant(neededPermissions);
    }

    @SuppressWarnings({"PMD.CollapsibleIfStatements"})
    public void askForPermissions() {
        ArrayList<String> notGrantedPermissions = new ArrayList<>();

        if (neededPermissions != null) {
            for (String permission : neededPermissions) {
                if (isNotNullOrEmpty(permission)) {
                    if (ContextCompat.checkSelfPermission(getContext(), permission)
                            != PackageManager.PERMISSION_GRANTED) {
                        notGrantedPermissions.add(permission);
                    }
                }
            }
        }
        if (possiblePermissions != null) {
            for (String permission : possiblePermissions) {
                if (isNotNullOrEmpty(permission)) {
                    if (ContextCompat.checkSelfPermission(getContext(), permission)
                            != PackageManager.PERMISSION_GRANTED) {
                        notGrantedPermissions.add(permission);
                    }
                }
            }
        }

        String[] permissionsToGrant = removeEmptyAndNullStrings(notGrantedPermissions);
        ActivityCompat
                .requestPermissions(getActivity(), permissionsToGrant, PERMISSIONS_REQUEST_CODE);
    }

    private void initializeView() {
        Bundle bundle = getArguments();
        backgroundColor = bundle.getInt(BACKGROUND_COLOR);
        buttonsColor = bundle.getInt(BUTTONS_COLOR);
        image = bundle.getInt(IMAGE, 0);
        title = bundle.getString(TITLE);
        description = bundle.getString(DESCRIPTION);
        neededPermissions = bundle.getStringArray(NEEDED_PERMISSIONS);
        possiblePermissions = bundle.getStringArray(POSSIBLE_PERMISSIONS);

        updateViewWithValues();
    }

    private void updateViewWithValues() {
        titleTextView.setText(title);
        descriptionTextView.setText(description);

        if (image != 0) {
            imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), image));
            imageView.setVisibility(View.VISIBLE);
        }
    }

    @SuppressWarnings({"PMD.CollapsibleIfStatements"})
    private boolean hasPermissionsToGrant(String[] permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (isNotNullOrEmpty(permission)) {
                    if (ContextCompat.checkSelfPermission(getContext(), permission)
                            != PackageManager.PERMISSION_GRANTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private String[] removeEmptyAndNullStrings(final ArrayList<String> permissions) {
        List<String> list = new ArrayList<>(permissions);
        list.removeAll(Collections.singleton(null));
        return list.toArray(new String[list.size()]);
    }

    public static boolean isNotNullOrEmpty(String string) {
        return string != null && !string.isEmpty();
    }
}