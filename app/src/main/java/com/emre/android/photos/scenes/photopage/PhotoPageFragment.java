/*
 * Copyright 2020 Emre Üsküplü
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.emre.android.photos.scenes.photopage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.emre.android.photos.R;

/**
 * @author Emre Üsküplü
 *
 * It gets photo from list and shows to full screen.
 */
public class PhotoPageFragment extends Fragment {
    private static final String TAG = PhotoPageFragment.class.getSimpleName();
    private static final String ARG_PHOTO_URL = "photoUrlDTO";

    private String mPhotoUrl;
    private ImageView mPhotoImage;
    private ImageButton mCloseButton;
    private View.OnClickListener mCloseButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            requireActivity().finish();
        }
    };

    static PhotoPageFragment newInstance(String photoUrl) {
        Bundle args = new Bundle();
        args.putString(ARG_PHOTO_URL, photoUrl);

        PhotoPageFragment photoPageFragment = new PhotoPageFragment();
        photoPageFragment.setArguments(args);
        return photoPageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPhotoUrl = getArguments().getString(ARG_PHOTO_URL);
        } else {
            Log.e(TAG, "getArgument is null");

            showErrorShowingPhotoToast();
        }
    }

    private void showErrorShowingPhotoToast() {
        Toast.makeText(requireContext(),
                R.string.error_showing_photo,
                Toast.LENGTH_SHORT)
                .show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_page, container, false);

        mPhotoImage = v.findViewById(R.id.item_photo_full_screen);
        mCloseButton = v.findViewById(R.id.close_button);

        mCloseButton.setImageResource(R.drawable.ic_close_button);
        mCloseButton.setOnClickListener(mCloseButtonOnClickListener);

        Glide.with(requireActivity())
                .load(mPhotoUrl)
                .into(mPhotoImage);

        return v;
    }
}