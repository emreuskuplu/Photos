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

package com.emre.android.photos;

import com.emre.android.photos.dataaccessobject.IPhotoUrlDAO;
import com.emre.android.photos.dataaccessobject.PhotoUrlDAO;
import com.emre.android.photos.datatransferobject.PhotoUrlDTO;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emre Üsküplü
 */
public class PhotoUrlDAOTest {

    private List<PhotoUrlDTO> mPhotoUrlDTOList;
    private IPhotoUrlDAO mIPhotoUrlDAO;

    @Test
    public void getUrlListFromPhotoUrlDAO() {
        givenPhotoUrlListAndPhotoUrlDAO();
        whenGetsPhotoUrlListFromPhotoUrlDAO();
        thenVerifyPhotoUrlList();
    }

    private void givenPhotoUrlListAndPhotoUrlDAO() {
        mPhotoUrlDTOList = new ArrayList<>();
        mIPhotoUrlDAO = new PhotoUrlDAO();
    }

    private void whenGetsPhotoUrlListFromPhotoUrlDAO() {
        mPhotoUrlDTOList = mIPhotoUrlDAO.getPhotoUrlList("5");
    }

    private void thenVerifyPhotoUrlList() {
        for (PhotoUrlDTO photoUrlDTO : mPhotoUrlDTOList) {
            String photoUrl = photoUrlDTO.getPhotoUrl();
            System.out.println(photoUrl);
            Assert.assertNotNull("Url is null", photoUrl);
        }
    }
}
