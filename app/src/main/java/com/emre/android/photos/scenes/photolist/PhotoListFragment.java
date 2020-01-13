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

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.emre.android.photos.preferencemanager.IPhotoListFormat;
import com.emre.android.photos.preferencemanager.PhotoListFormat;
import com.emre.android.photos.datatransferobject.PhotoUrlDTO;
import com.emre.android.photos.R;
import com.emre.android.photos.networkconnection.INetworkStatus;
import com.emre.android.photos.networkconnection.NetworkStatus;
import com.emre.android.photos.scenes.photopage.PhotoPageActivity;
import com.emre.android.photos.workerthread.FetchPhotoUrlListTask;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emre Üsküplü
 *
 * It shows photo list
 * If there is no network connection, shows the network message page
 */
public class PhotoListFragment extends Fragment implements IUpdatePhotoUrlList {
    private static final String TAG = PhotoListFragment.class.getSimpleName();

    private int mActiveItemColor;
    private int mDeactivateItemColor;
    private int mPageNumber;
    private boolean mIsStartedWorkerThread;
    private List<PhotoUrlDTO> mPhotoUrlDTOList;
    private RecyclerView mPhotoListRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private LinearLayoutManager mLinearLayoutManager;
    private PhotoListAdapter mPhotoListAdapter;
    private ProgressBar mPhotoListProgressBar;
    private MenuItem mGridViewItem;
    private MenuItem mListViewItem;
    private IPhotoListFormat mIPhotoListFormat;
    private INetworkStatus mINetworkStatus;
    private RecyclerView.OnScrollListener mPhotoListOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (mPhotoListRecyclerView.getLayoutManager() == mGridLayoutManager) {
                int visibleItemCount = mGridLayoutManager.getChildCount();
                int totalItemCount = mGridLayoutManager.getItemCount();
                int firstVisibleItemPosition = mGridLayoutManager.findFirstVisibleItemPosition();

                if (!mIsStartedWorkerThread) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        if (mINetworkStatus.isOnlineNetworkConnection(getContext())) {
                            mPageNumber++;
                            fetchPhotoUrlList(mPageNumber);
                        }
                    }
                }

            } else if (mPhotoListRecyclerView.getLayoutManager() == mLinearLayoutManager) {
                int visibleItemCount = mLinearLayoutManager.getChildCount();
                int totalItemCount = mLinearLayoutManager.getItemCount();
                int firstVisibleItemPosition = mLinearLayoutManager.findFirstVisibleItemPosition();

                if (!mIsStartedWorkerThread) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        if (mINetworkStatus.isOnlineNetworkConnection(getContext())) {
                            mPageNumber++;
                            fetchPhotoUrlList(mPageNumber);
                        }
                    }
                }
            }
        }
    };

    public static PhotoListFragment newInstance() {
        return new PhotoListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        int spanCount = getSpanCountDependingScreenWidthDp();
        mPageNumber = 0;
        mIsStartedWorkerThread = false;
        mGridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mIPhotoListFormat = new PhotoListFormat();
        mINetworkStatus = new NetworkStatus();
        mPhotoUrlDTOList = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_list, viewGroup, false);

        mPhotoListRecyclerView = v.findViewById(R.id.photo_list_recycler_view);
        mPhotoListProgressBar = v.findViewById(R.id.photo_list_progress_bar);
        mPhotoListRecyclerView.addOnScrollListener(mPhotoListOnScrollListener);

        if (!mINetworkStatus.isOnlineNetworkConnection(getContext())) {
            replaceToNetworkMessageFragment();

        } else {
            updatePhotoListLayoutManager();
            setupPhotoListAdapter();
            fetchPhotoUrlList(mPageNumber);
        }

        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_photo_list, menu);

        mActiveItemColor = getResources().getColor(R.color.colorActive);
        mDeactivateItemColor = getResources().getColor(R.color.colorDeactivate);

        mGridViewItem = menu.findItem(R.id.menu_item_grid_view);
        mListViewItem = menu.findItem(R.id.menu_item_list_view);

        if (mIPhotoListFormat.getPrefListFormat(getContext()).equals("grid_view")) {
            mGridViewItem.getIcon().setColorFilter(mActiveItemColor, PorterDuff.Mode.SRC_IN);
        } else if (mIPhotoListFormat.getPrefListFormat(getContext()).equals("list_view")) {
            mListViewItem.getIcon().setColorFilter(mActiveItemColor, PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Grid view menu icon
        if (item.getItemId() == R.id.menu_item_grid_view) {
            if (!mIPhotoListFormat.getPrefListFormat(getContext()).equals("grid_view")) {
                item.getIcon().setColorFilter(mActiveItemColor, PorterDuff.Mode.SRC_IN);

                mIPhotoListFormat.setPrefUnitsFormat(getContext(), "grid_view");
                mListViewItem.getIcon().setColorFilter(mDeactivateItemColor, PorterDuff.Mode.SRC_IN);

                Log.i(TAG, "Grid view menu icon is clicked.");

                updatePhotoListLayoutManager();
                mPhotoListRecyclerView.setAdapter(mPhotoListAdapter);

                return true;
            }
            // List view menu icon
        } else if (item.getItemId() == R.id.menu_item_list_view) {
            if (!mIPhotoListFormat.getPrefListFormat(getContext()).equals("list_view")) {
                item.getIcon().setColorFilter(mActiveItemColor, PorterDuff.Mode.SRC_IN);

                mIPhotoListFormat.setPrefUnitsFormat(getContext(), "list_view");
                mGridViewItem.getIcon().setColorFilter(mDeactivateItemColor, PorterDuff.Mode.SRC_IN);

                Log.i(TAG, "List view menu icon is clicked.");

                updatePhotoListLayoutManager();
                mPhotoListRecyclerView.setAdapter(mPhotoListAdapter);

                return true;
            }
        } else {
            Log.e(TAG, "Menu item is not found.");

            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    private void replaceToNetworkMessageFragment(){
        Fragment networkMessageFragment = NetworkMessageFragment.newInstance();

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, networkMessageFragment)
                .commit();
    }

    /**
     * One total width space of image in grid view list is 119dp
     * @return span count for grid view list
     */
    private int getSpanCountDependingScreenWidthDp() {
        Configuration configuration = getResources().getConfiguration();
        return configuration.screenWidthDp / 119;
    }

    private void setupPhotoListAdapter() {
        mPhotoListAdapter = new PhotoListAdapter(mPhotoUrlDTOList);
        mPhotoListRecyclerView.setAdapter(mPhotoListAdapter);
    }

    private void updatePhotoListLayoutManager() {
        mPageNumber = 0;
        if (mIPhotoListFormat.getPrefListFormat(getContext()).equals("grid_view")) {
            mPhotoListRecyclerView.setLayoutManager(mGridLayoutManager);
        } else if (mIPhotoListFormat.getPrefListFormat(getContext()).equals("list_view")) {
            mPhotoListRecyclerView.setLayoutManager(mLinearLayoutManager);
        }

    }

    private void fetchPhotoUrlList(int pageNumber) {
        mPhotoListProgressBar.setVisibility(View.VISIBLE);
        String stringPageNumber = Integer.toString(pageNumber);
        new FetchPhotoUrlListTask(this).execute(stringPageNumber);
        mIsStartedWorkerThread = true;
    }

    @Override
    public void updatePhotoUrlList(List<PhotoUrlDTO> photoUrlDTOList) {
        mIsStartedWorkerThread = false;
        mPhotoUrlDTOList.addAll(photoUrlDTOList);
        mPhotoListAdapter.notifyDataSetChanged();
        mPhotoListProgressBar.setVisibility(View.GONE);
    }

    private class PhotoListHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private ImageView mPhotoImage;

        private PhotoListHolder(@NonNull LayoutInflater inflater, ViewGroup viewGroup, int layoutListItem) {
            super(inflater.inflate(layoutListItem, viewGroup, false));
            itemView.setOnClickListener(this);

            if (mIPhotoListFormat.getPrefListFormat(getContext()).equals("grid_view")) {
                mPhotoImage = itemView.findViewById(R.id.item_photo_grid_view);
            } else if (mIPhotoListFormat.getPrefListFormat(getContext()).equals("list_view")) {
                mPhotoImage = itemView.findViewById(R.id.item_photo_list_view);
            }
        }

        @Override
        public void onClick(View view) {
            String photoUrl = mPhotoUrlDTOList.get(getAdapterPosition()).getPhotoUrl();
            Intent intent = PhotoPageActivity.newIntent(getActivity(), photoUrl);
            startActivity(intent);
        }
    }

    private class PhotoListAdapter extends RecyclerView.Adapter<PhotoListHolder> {

        private PhotoListAdapter(List<PhotoUrlDTO> photoUrlDTOList) {
            mPhotoUrlDTOList = photoUrlDTOList;
        }

        @NonNull
        @Override
        public PhotoListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            if (mIPhotoListFormat.getPrefListFormat(getContext()).equals("grid_view")) {
                return new PhotoListHolder(layoutInflater, parent, R.layout.list_item_photo_grid_view);
            } else if (mIPhotoListFormat.getPrefListFormat(getContext()).equals("list_view")) {
                return new PhotoListHolder(layoutInflater, parent, R.layout.list_item_photo_list_view);
            } else {
                Log.e(TAG, "List format preference is not found.");
                // Default list format
                return new PhotoListHolder(layoutInflater, parent, R.layout.list_item_photo_grid_view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoListHolder holder, int position) {
            String photoUrl = mPhotoUrlDTOList.get(position).getPhotoUrl();
            Glide.with(requireActivity())
                    .load(photoUrl)
                    .centerCrop()
                    .into(holder.mPhotoImage);
        }

        @Override
        public int getItemCount() {
            return mPhotoUrlDTOList.size();
        }
    }
}