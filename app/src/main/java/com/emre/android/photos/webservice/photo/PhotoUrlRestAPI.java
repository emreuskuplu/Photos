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

package com.emre.android.photos.webservice.photo;

import android.util.Log;

import com.emre.android.photos.datatransferobject.PhotoUrlDTO;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * @author Emre Üsküplü
 */
public class PhotoUrlRestAPI {
    private static final String TAG = PhotoUrlRestAPI.class.getSimpleName();

    private static final String API_KEY = "550be48f323d48b91745061ffea60a24";
    private static final String METHOD = "flickr.photos.getRecent";
    private static final String FORMAT = "json";
    private static final String NO_JSON_CALLBACK = "1";
    private static final String EXTRAS = "url_s";
    private static final String PER_PAGE = "100";
    private String mPageNumber;

    public PhotoUrlRestAPI(String pageNumber) {
        mPageNumber = pageNumber;
    }

    public List<PhotoUrlDTO> fetchPhotoUrlList() {
        IPhotoUrlRetrofitService iPhotoUrlRetrofitService = PhotoUrlClient
                .getWeatherRetrofit(new TypeToken<List<PhotoUrlDTO>>() {}.getType(), new PhotoUrlDeserializer())
                .create(IPhotoUrlRetrofitService.class);

        Call<List<PhotoUrlDTO>> photoUrlListCall = iPhotoUrlRetrofitService.getPhotoUrlList(
                API_KEY,
                METHOD,
                FORMAT,
                NO_JSON_CALLBACK,
                EXTRAS,
                PER_PAGE,
                mPageNumber
        );

        Log.i(TAG, "Photo url list rest api: " + photoUrlListCall.request().url().toString());

        try {
            if (photoUrlListCall.execute().isSuccessful()) {
                return photoUrlListCall.clone().execute().body();
            } else {
                Log.e(TAG, "Photo url list execute error: " + photoUrlListCall.clone().execute().code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}