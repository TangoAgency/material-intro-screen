package agency.tango.materialintroscreen.fragments;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agency.tango.materialintroscreen.R;
import agency.tango.materialintroscreen.parallax.ParallaxFragment;

public class SlideFragmentBase extends ParallaxFragment {

    private static final int PERMISSIONS_REQUEST_CODE = 15621;

    @ColorRes
    public int backgroundColor() {
        return R.color.mis_default_background_color;
    }

    @ColorRes
    public int buttonsColor() {
        return R.color.mis_default_buttons_color;
    }

    public boolean canMoveFurther() {
        return true;
    }

    public String cantMoveFurtherErrorMessage() {
        return getString(R.string.mis_impassable_slide);
    }

    public String[] possiblePermissions() {
        return new String[0];
    }

    public String[] neededPermissions() {
        return new String[0];
    }

    @StringRes
    public int grantPermissionStringRes() {
        return R.string.mis_grant_permissions;
    }

    @StringRes
    public int grantPermissionErrorStringRes() {
        return R.string.mis_please_grant_permissions;
    }

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
                if (!TextUtils.isEmpty(permission)) {
                    if (ContextCompat.checkSelfPermission(getContext(), permission)
                            != PackageManager.PERMISSION_GRANTED) {
                        notGrantedPermissions.add(permission);
                    }
                }
            }
        }
        if (possiblePermissions() != null) {
            for (String permission : possiblePermissions()) {
                if (!TextUtils.isEmpty(permission)) {
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
                if (!TextUtils.isEmpty(permission)) {
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
}