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

package com.emre.android.photos.workerthread;

import android.os.AsyncTask;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.emre.android.photos.dataaccessobject.PhotoUrlDAO;
import com.emre.android.photos.datatransferobject.PhotoUrlDTO;
import com.emre.android.photos.scenes.photolist.IUpdatePhotoUrlList;
import com.emre.android.photos.scenes.photolist.PhotoListFragment;

import java.util.List;

/**
 * @author Emre Üsküplü
 */
public class FetchPhotoUrlListTask extends AsyncTask<String, Void, List<PhotoUrlDTO>> {
    private static final String TAG = FetchPhotoUrlListTask.class.getSimpleName();

    private Fragment mPhotoListFragment;

    public FetchPhotoUrlListTask(Fragment photoListFragment) {
        mPhotoListFragment = photoListFragment;
    }

    @Override
    protected List<PhotoUrlDTO> doInBackground(String... strings) {
        Log.i(TAG, " execute started.");
        return new PhotoUrlDAO().getPhotoUrlList(strings[0]);
    }

    @Override
    protected void onPostExecute(List<PhotoUrlDTO> photoUrlDTOList) {
        Log.i(TAG, " is executed.");

        if (!photoUrlDTOList.isEmpty()) {
            IUpdatePhotoUrlList iUpdatePhotoUrlList = (PhotoListFragment) mPhotoListFragment;
            iUpdatePhotoUrlList.updatePhotoUrlList(photoUrlDTOList);
        }

        super.onPostExecute(photoUrlDTOList);
    }
}
