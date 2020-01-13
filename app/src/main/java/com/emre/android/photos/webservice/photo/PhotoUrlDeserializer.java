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
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Emre Üsküplü
 *
 * Gets photo urls with deserializer
 */
class PhotoUrlDeserializer implements JsonDeserializer<List<PhotoUrlDTO>> {
    @Override
    public List<PhotoUrlDTO> deserialize(JsonElement json, Type typeOfT,
                                         JsonDeserializationContext context) throws JsonParseException {
        List<PhotoUrlDTO> photoUrlList = new ArrayList<>();

        if (json != null && json.isJsonObject() && !json.isJsonNull()) {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonObject photos = jsonObject.getAsJsonObject("photos");

            for (int i = 0; i < 100; i++) {
                if (photos.get("photo") != null && photos.get("photo").isJsonArray()
                        && !photos.get("photo").isJsonNull()) {
                    JsonArray photo = photos.get("photo").getAsJsonArray();

                    if (photo.get(i) != null && photo.get(i).isJsonObject()
                            && !photo.get(i).isJsonNull()) {
                        JsonObject listIndex = photo.get(i).getAsJsonObject();

                        if (listIndex.get("url_s") != null && listIndex.get("url_s").isJsonPrimitive()
                                && !listIndex.get("url_s").isJsonNull()) {
                            String urlS = listIndex.get("url_s").getAsString();

                            photoUrlList.add(new PhotoUrlDTO());
                            photoUrlList.get(photoUrlList.size() - 1).setPhotoUrl(urlS);
                        }
                    }
                }
            }
        }

        return photoUrlList;
    }
}