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

package com.emre.android.photos.scenes.photolist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.emre.android.photos.R;
import com.emre.android.photos.networkconnection.INetworkStatus;
import com.emre.android.photos.networkconnection.NetworkStatus;

/**
 * @author Emre Üsküplü
 *
 * It shows network message and "try again" button if the user's device not has network connection.
 */
public class NetworkMessageFragment extends Fragment {
    private INetworkStatus mINetworkStatus;
    private Button mTryAgainButton;
    private View.OnClickListener mTryAgainButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mINetworkStatus.isOnlineNetworkConnection(requireActivity())) {
                replaceToPhotoListFragment();
            } else {
                showInternetConnectionMessage();
            }
        }
    };

    public static NetworkMessageFragment newInstance() {
        return new NetworkMessageFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mINetworkStatus = new NetworkStatus();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_network_message, container, false);

        mTryAgainButton = v.findViewById(R.id.try_again_button);
        mTryAgainButton.setOnClickListener(mTryAgainButtonOnClickListener);

        return v;
    }

    private void showInternetConnectionMessage() {
        Toast.makeText(requireActivity(),
                R.string.first_fow,
                Toast.LENGTH_SHORT)
                .show();
    }

    private void replaceToPhotoListFragment() {
        Fragment photoListFragment = PhotoListFragment.newInstance();

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, photoListFragment)
                .commit();
    }
}