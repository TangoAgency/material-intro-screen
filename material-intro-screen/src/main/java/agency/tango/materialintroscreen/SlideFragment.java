package agency.tango.materialintroscreen;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agency.tango.materialintroscreen.parallax.ParallaxFragment;

import static android.content.Context.WINDOW_SERVICE;

public class SlideFragment extends ParallaxFragment {
    private final static String BACKGROUND_COLOR = "background_color";
    private static final String BUTTONS_COLOR = "buttons_color";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String NEEDED_PERMISSIONS = "needed_permission";
    private static final String POSSIBLE_PERMISSIONS = "possible_permission";
    private static final String IMAGE = "image";
    private static final int PERMISSIONS_REQUEST_CODE = 15621;

    private int backgroundColor;
    private int buttonsColor;
    private int image;
    private String title;
    private String description;
    private String[] neededPermissions;
    private String[] possiblePermissions;

    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView imageView;

    public static SlideFragment createInstance(SlideFragmentBuilder builder) {
        SlideFragment slideFragment = new SlideFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(BACKGROUND_COLOR, builder.backgroundColor);
        bundle.putInt(BUTTONS_COLOR, builder.buttonsColor);
        bundle.putInt(IMAGE, builder.image);
        bundle.putString(TITLE, builder.title);
        bundle.putString(DESCRIPTION, builder.description);
        bundle.putStringArray(NEEDED_PERMISSIONS, builder.neededPermissions);
        bundle.putStringArray(POSSIBLE_PERMISSIONS, builder.possiblePermissions);

        slideFragment.setArguments(bundle);
        return slideFragment;
    }

    public static boolean isNotNullOrEmpty(String string) {
        return string != null && !string.isEmpty();
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
        backgroundColor = bundle.getInt(BACKGROUND_COLOR);
        buttonsColor = bundle.getInt(BUTTONS_COLOR);
        image = bundle.getInt(IMAGE, 0);
        title = bundle.getString(TITLE);
        description = bundle.getString(DESCRIPTION);
        neededPermissions = bundle.getStringArray(NEEDED_PERMISSIONS);
        possiblePermissions = bundle.getStringArray(POSSIBLE_PERMISSIONS);

        updateViewWithValues();
    }

    public int backgroundColor() {
        return backgroundColor;
    }

    public int buttonsColor() {
        return buttonsColor;
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
        ActivityCompat.requestPermissions(getActivity(), permissionsToGrant, PERMISSIONS_REQUEST_CODE);
        for (String permission : permissionsToGrant) {
            switch (permission) {
                case Manifest.permission.SYSTEM_ALERT_WINDOW:
                    if (!canDrawOverOtherApps()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getContext().getPackageName()));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else if (Build.MANUFACTURER.equals("Xiaomi") && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                            Toast.makeText(getContext(), R.string.grant_permission_xiaomi_draw, Toast.LENGTH_LONG).show();
                            Intent i = new Intent();
                            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            i.addCategory(Intent.CATEGORY_DEFAULT);
                            i.setData(Uri.parse("package:" + getContext().getPackageName()));
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            getContext().startActivity(i);
                        }
                    }
                    break;
                case Manifest.permission.WRITE_SETTINGS:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!Settings.System.canWrite(getContext())) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getContext().getPackageName()));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                    break;
            }
        }
    }

    private boolean canDrawOverlaysUsingReflection() {
        try {
            AppOpsManager manager = (AppOpsManager) getContext().getSystemService(Context.APP_OPS_SERVICE);
            Class clazz = AppOpsManager.class;
            Method dispatchMethod = clazz.getMethod("checkOp", int.class, int.class, String.class);
            int mode = (Integer) dispatchMethod.invoke(manager, 24, Binder.getCallingUid(), getContext().getApplicationContext().getPackageName());
            return AppOpsManager.MODE_ALLOWED == mode;
        } catch (Exception e) {
            return false;
        }

    }

    private boolean canDrawOverOtherApps() {
        if (Build.MANUFACTURER.equals("Xiaomi") && Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return canDrawOverlaysUsingReflection();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return Settings.canDrawOverlays(getContext());
        try {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams(-1, -1, 2003, 65794, -2);
            lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
            View view = new View(getContext());
            ((WindowManager) getContext().getSystemService(WINDOW_SERVICE)).addView(view, lp);
            ((WindowManager) getContext().getSystemService(WINDOW_SERVICE)).removeView(view);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean hasPermissionsToGrant(String[] permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (isNotNullOrEmpty(permission)) {
                    if (permission.equals(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                        return !canDrawOverOtherApps();
                    }
                    if (permission.equals(Manifest.permission.WRITE_SETTINGS)) {
                        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(getContext());
                    }
                    if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
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
}
