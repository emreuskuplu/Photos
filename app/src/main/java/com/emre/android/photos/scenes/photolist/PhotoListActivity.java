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

import androidx.fragment.app.Fragment;

import com.emre.android.photos.scenes.fragmentmanager.PhotoFragmentManager;

/**
 * @author Emre Üsküplü
 */
public class PhotoListActivity extends PhotoFragmentManager {

    @Override
    public Fragment createFragment() {
        return PhotoListFragment.newInstance();
    }
}