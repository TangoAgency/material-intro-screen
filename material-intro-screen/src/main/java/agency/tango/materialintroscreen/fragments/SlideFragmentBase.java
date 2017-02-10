package agency.tango.materialintroscreen.fragments;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agency.tango.materialintroscreen.parallax.ParallaxFragment;

public abstract class SlideFragmentBase extends ParallaxFragment {

    private static final int PERMISSIONS_REQUEST_CODE = 15621;

    @ColorRes
    public abstract int backgroundColor();

    @ColorRes
    public abstract int buttonsColor();

    public abstract boolean canMoveFurther();

    public abstract String cantMoveFurtherErrorMessage();

    public abstract String[] possiblePermissions();

    public abstract String[] neededPermissions();

    public boolean hasAnyPermissionsToGrant() {
        if (!isAndroidVersionSupportingPermissions()) {
            return false;
        }

        boolean hasPermissionToGrant = hasPermissionsToGrant(neededPermissions());
        if (!hasPermissionToGrant) {
            hasPermissionToGrant = hasPermissionsToGrant(possiblePermissions());
        }
        return hasPermissionToGrant;
    }

    public boolean hasNeededPermissionsToGrant() {
        return hasPermissionsToGrant(neededPermissions());
    }

    @SuppressWarnings({"PMD.CollapsibleIfStatements"})
    public void askForPermissions() {
        ArrayList<String> notGrantedPermissions = new ArrayList<>();

        if (neededPermissions() != null) {
            for (String permission : neededPermissions()) {
                if (isNotNullOrEmpty(permission)) {
                    if (ContextCompat.checkSelfPermission(getContext(), permission)
                            != PackageManager.PERMISSION_GRANTED) {
                        notGrantedPermissions.add(permission);
                    }
                }
            }
        }
        if (possiblePermissions() != null) {
            for (String permission : possiblePermissions()) {
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

    @SuppressWarnings({"PMD.CollapsibleIfStatements"})
    private boolean hasPermissionsToGrant(String[] permissions) {
        if (!isAndroidVersionSupportingPermissions()) {
            return false;
        }

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

    private boolean isAndroidVersionSupportingPermissions() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isNotNullOrEmpty(String string) {
        return string != null && !string.isEmpty();
    }
}