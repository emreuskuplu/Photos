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

package com.emre.android.photos.networkconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Emre Üsküplü
 *
 * It checks network connection of device
 */
public class NetworkStatus implements INetworkStatus {
    private static final String TAG = NetworkStatus.class.getSimpleName();

    @Override
    public boolean isOnlineNetworkConnection(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;

        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        } else {
            // If there is not default network then ignore isAvailableNetworkConnection condition
            Log.i(TAG, "There is not default network");
            Toast.makeText(context, "Network information is not found", Toast.LENGTH_SHORT).show();

            return true;
        }

        return networkInfo != null && networkInfo.isConnected();
    }
}