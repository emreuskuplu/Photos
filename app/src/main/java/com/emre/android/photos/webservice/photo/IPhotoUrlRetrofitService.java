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

import com.emre.android.photos.datatransferobject.PhotoUrlDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author Emre Üsküplü
 *
 * It builds api url.
 */
interface IPhotoUrlRetrofitService {

    @GET("services/rest/?")
    Call<List<PhotoUrlDTO>> getPhotoUrlList(
            @Query("api_key") String apiKey,
            @Query("method") String method,
            @Query("format") String format,
            @Query("nojsoncallback") String noJsonCallback,
            @Query("extras") String extras,
            @Query("per_page") String perPage,
            @Query("page") String page
    );

}