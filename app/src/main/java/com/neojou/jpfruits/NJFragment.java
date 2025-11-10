package com.neojou.jpfruits;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.fragment.app.Fragment;


public class NJFragment extends Fragment {
    private static final String TAG="JPFruits:NJFragment";

    public int px2dip(float px) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int)(px / scale + 0.5f );
    }

    public int dip2px(float dp) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

    public DisplayMetrics get_screen_display_metrics() {
        Activity activity = getActivity();
        if (activity == null) {
            Log.e(TAG, "get_screen_display_metrics() : getActivity() is null");
            return null;
        }

        DisplayMetrics displaymetrics = activity.getResources().getDisplayMetrics();
        return displaymetrics;
    }

}
