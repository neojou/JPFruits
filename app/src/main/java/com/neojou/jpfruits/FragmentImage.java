package com.neojou.jpfruits;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import androidx.databinding.DataBindingUtil;

import com.neojou.jpfruits.databinding.FragmentImageBinding;

@startuml
package com.neojou.jpfruits {
    class FragmentImage {
        - static final String TAG = "JPFruits:FragmentImage"
                - FragmentImageBinding binding
        - ImageView main_image
        + View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        + void onViewCreated(View view, Bundle savedInstanceState)
        - void setViewItemsBinding()
        - void set_image_padding()
    }

    FragmentImage ..|> NJFragment
    FragmentImage o-- FragmentImageBinding
    FragmentImage o-- ImageView
    FragmentImage o-- DisplayMetrics
    FragmentImage o-- LayoutInflater
    FragmentImage o-- ViewGroup
    FragmentImage o-- Bundle
    FragmentImage o-- View
    FragmentImage o-- DataBindingUtil
}
@enduml

public class FragmentImage extends NJFragment {
    private static final String TAG="JPFruits:FragmentImage";


    FragmentImageBinding binding;
    ImageView main_image;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image, container, false);
        setViewItemsBinding();
        return binding.getRoot();
    }

    private void setViewItemsBinding() {
        main_image = binding.mainImage;
    }

        @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        set_image_padding();
    }

    private void set_image_padding()
    {
        DisplayMetrics dm = get_screen_display_metrics();
        if (dm == null) {
            Log.e(TAG, "set_image_padding() : DisplayMetrics is null");
            return;
        }
        int screen_width_px = dm.widthPixels;
        final float layout_frame_rim_ratio = 0.08f; // 8% of the width
        int choice_px = (int)((float)(screen_width_px) * layout_frame_rim_ratio);
        //Log.d(TAG, "set_image_padding() : padding px = " + choice_px);
        if (choice_px < 20) {
            Log.e(TAG, "set_image_padding() : choice_px(" + choice_px + ") < 20");
            return;
        }
        main_image.setPadding(choice_px, choice_px, choice_px, choice_px);
    }

}
