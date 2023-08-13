/*
 * Copyright (C) 2016 Pedro Paulo de Amorim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.folioreader.ui.folio.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.folioreader.Config;
import com.folioreader.Constants;
import com.folioreader.FolioReader;
import com.folioreader.R;
import com.folioreader.model.HighlightImpl;
import com.folioreader.model.ReadPosition;
import com.folioreader.model.event.AnchorIdEvent;
import com.folioreader.model.event.ClearSearchEvent;
import com.folioreader.model.event.MediaOverlayHighlightStyleEvent;
import com.folioreader.model.event.MediaOverlayPlayPauseEvent;
import com.folioreader.model.event.MediaOverlaySpeedEvent;
import com.folioreader.model.event.SearchEvent;
import com.folioreader.model.event.WebViewPosition;
import com.folioreader.ui.folio.adapter.FolioPageFragmentAdapter;
import com.folioreader.ui.folio.fragment.FolioPageFragment;
import com.folioreader.ui.folio.presenter.MainMvpView;
import com.folioreader.ui.folio.presenter.MainPresenter;
import com.folioreader.util.AppUtil;
import com.folioreader.util.FileUtil;
import com.folioreader.util.UiUtil;
import com.folioreader.view.ConfigBottomSheetDialogFragment;
import com.folioreader.view.DirectionalViewpager;
import com.folioreader.view.ObservableWebView;
import com.folioreader.view.StyleableTextView;

import org.greenrobot.eventbus.EventBus;
import org.readium.r2_streamer.model.container.Container;
import org.readium.r2_streamer.model.container.EpubContainer;
import org.readium.r2_streamer.model.publication.EpubPublication;
import org.readium.r2_streamer.model.publication.link.Link;
import org.readium.r2_streamer.model.searcher.SearchQueryResults;
import org.readium.r2_streamer.server.EpubServer;
import org.readium.r2_streamer.server.EpubServerSingleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.folioreader.Constants.CHAPTER_SELECTED;
import static com.folioreader.Constants.HIGHLIGHT_SELECTED;
import static com.folioreader.Constants.SELECTED_CHAPTER_POSITION;
import static com.folioreader.Constants.TYPE;

public class FolioActivity
        extends AppCompatActivity
        implements FolioPageFragment.FolioPageFragmentCallback,
        ObservableWebView.ToolBarListener,
        ConfigBottomSheetDialogFragment.ConfigDialogCallback,
        MainMvpView {

    private static final String TAG = "FolioActivity";

    public static final String INTENT_EPUB_SOURCE_PATH = "com.folioreader.epub_asset_path";
    public static final String INTENT_EPUB_SOURCE_TYPE = "epub_source_type";
    public static final String INTENT_HIGHLIGHTS_LIST = "highlight_list";
    public static final String EXTRA_READ_POSITION = "com.folioreader.extra.READ_POSITION";
    private boolean isNightMode;

    public enum EpubSourceType {
        RAW,
        ASSETS,
        SD_CARD
    }

    private boolean isOpen = true;

    public static final int ACTION_CONTENT_HIGHLIGHT = 77;
    private String bookFileName;
    private static final String HIGHLIGHT_ITEM = "highlight_item";

    public boolean mIsActionBarVisible;
    public boolean mIsSearchSectionVisible = false;
    public boolean isForSearch = true;
    private DirectionalViewpager mFolioPageViewPager;
    private Toolbar mToolbar;
    private RelativeLayout searchSection;
    private AppCompatImageView searchImage;
    private SearchImageClickListener searchImageClickListener;
    private EditText mSearchText;

    private static final int SEARCH_ICON = 1;
    private static final int DOWN_ARROW_ICON = 2;

    private int mChapterPosition;
    private FolioPageFragmentAdapter mFolioPageFragmentAdapter;
    private ReadPosition entryReadPosition;
    private ConfigBottomSheetDialogFragment mConfigBottomSheetDialogFragment;
    private TextView title;

    private List<Link> mSpineReferenceList = new ArrayList<>();
    private EpubServer mEpubServer;

    private Animation slide_down;
    private Animation slide_up;
    private boolean mIsNightMode;
    private Config mConfig;
    private String mBookId;
    private String mEpubFilePath;
    private EpubSourceType mEpubSourceType;
    int mEpubRawId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folio_activity);

        mBookId = getIntent().getStringExtra(FolioReader.INTENT_BOOK_ID);
        mEpubSourceType = (EpubSourceType)
                getIntent().getExtras().getSerializable(FolioActivity.INTENT_EPUB_SOURCE_TYPE);
        if (mEpubSourceType.equals(EpubSourceType.RAW)) {
            mEpubRawId = getIntent().getExtras().getInt(FolioActivity.INTENT_EPUB_SOURCE_PATH);
        } else {
            mEpubFilePath = getIntent().getExtras()
                    .getString(FolioActivity.INTENT_EPUB_SOURCE_PATH);
        }


        isNightMode = getIntent().getBooleanExtra("Night", false);

        setConfig();

        if (!mConfig.isShowTts()) {
            findViewById(R.id.btn_speaker).setVisibility(View.GONE);
        }

        title = (TextView) findViewById(R.id.lbl_center);
        slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);

        initColors();
        if(Build.VERSION.SDK_INT>= 33){
            setupBook();
        }else {
            if (ContextCompat.checkSelfPermission(FolioActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(FolioActivity.this, Constants.getWriteExternalStoragePerms(), Constants.WRITE_EXTERNAL_STORAGE_REQUEST);
            } else {
                setupBook();
            }
        }

        initAudioView();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        ///////////////////search inits /////////////////////

        searchSection = (RelativeLayout) findViewById(R.id.search_section);
        searchImage = (AppCompatImageView) findViewById(R.id.search_img);
        searchImage.setTag(SEARCH_ICON);
        mSearchText = (EditText) findViewById(R.id.search_query);
        searchImageClickListener = new SearchImageClickListener();
        searchImage.setOnClickListener(searchImageClickListener);
        searchAnimateHide();

        ///////////////////search inits /////////////////////

        findViewById(R.id.btn_drawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FolioActivity.this, ContentHighlightActivity.class);
                intent.putExtra(CHAPTER_SELECTED, mSpineReferenceList.get(mChapterPosition).href);
                intent.putExtra(FolioReader.INTENT_BOOK_ID, mBookId);
                intent.putExtra(Constants.BOOK_TITLE, bookFileName);
                startActivityForResult(intent, ACTION_CONTENT_HIGHLIGHT);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

        // speaker = (ImageView) findViewById(R.id.btn_speaker);
        findViewById(R.id.btn_speaker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    audioContainer.startAnimation(slide_up);
                    audioContainer.setVisibility(View.VISIBLE);
                    shade.setVisibility(View.VISIBLE);
                } else {
                    audioContainer.startAnimation(slide_down);
                    audioContainer.setVisibility(View.INVISIBLE);
                    shade.setVisibility(View.GONE);
                }
                isOpen = !isOpen;
            }
        });

        // search
        findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        //cancel search
        findViewById(R.id.cancel_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearSearchSection();
                clearSearchHighlights();
                isForSearch = true;
            }
        });

        mIsNightMode = mConfig.isNightMode();
        if (mIsNightMode) {
            mToolbar.setBackgroundColor(ContextCompat.getColor(FolioActivity.this, R.color.black));
            title.setTextColor(ContextCompat.getColor(FolioActivity.this, R.color.white));
            audioContainer.setBackgroundColor(ContextCompat.getColor(FolioActivity.this, R.color.night));
        }
    }

    private void initBook(String mEpubFileName, int mEpubRawId, String mEpubFilePath, EpubSourceType mEpubSourceType) {
        try {
            int portNumber = getIntent().getIntExtra(Config.INTENT_PORT, Constants.PORT_NUMBER);
            mEpubServer = EpubServerSingleton.getEpubServerInstance(portNumber);
            mEpubServer.start();
            String path = FileUtil.saveEpubFileAndLoadLazyBook(FolioActivity.this, mEpubSourceType, mEpubFilePath,
                    mEpubRawId, mEpubFileName);
            addEpub(path);

            String urlString = Constants.LOCALHOST + bookFileName + "/manifest";
            new MainPresenter(this).parseManifest(urlString);

        } catch (IOException e) {
            Log.e(TAG, "initBook failed", e);
        }
    }

    private void addEpub(String path) throws IOException {
        Container epubContainer = new EpubContainer(path);
        mEpubServer.addEpub(epubContainer, "/" + bookFileName);
        getEpubResource();
    }

    private void getEpubResource() {
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        configDrawerLayoutButtons();
    }

    @Override
    public void onOrientationChange(int orentation) {
        if (orentation == 0) {
            mFolioPageViewPager.setDirection(DirectionalViewpager.Direction.VERTICAL);
            mFolioPageFragmentAdapter =
                    new FolioPageFragmentAdapter(getSupportFragmentManager(),
                            mSpineReferenceList, bookFileName, mBookId);
            mFolioPageViewPager.setAdapter(mFolioPageFragmentAdapter);
            mFolioPageViewPager.setOffscreenPageLimit(1);
            mFolioPageViewPager.setCurrentItem(mChapterPosition);

        } else {
            mFolioPageViewPager.setDirection(DirectionalViewpager.Direction.HORIZONTAL);
            mFolioPageFragmentAdapter =
                    new FolioPageFragmentAdapter(getSupportFragmentManager(),
                            mSpineReferenceList, bookFileName, mBookId);
            mFolioPageViewPager.setAdapter(mFolioPageFragmentAdapter);
            mFolioPageViewPager.setCurrentItem(mChapterPosition);
        }
    }

    private void configFolio() {
        mFolioPageViewPager = findViewById(R.id.folioPageViewPager);
        mFolioPageViewPager.setOnPageChangeListener(new DirectionalViewpager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                EventBus.getDefault().post(new MediaOverlayPlayPauseEvent(mSpineReferenceList.get(mChapterPosition).href, false, true));
                mPlayPauseBtn.setImageDrawable(ContextCompat.getDrawable(FolioActivity.this, R.drawable.play_icon));
                mChapterPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == DirectionalViewpager.SCROLL_STATE_IDLE) {
                    title.setText(mSpineReferenceList.get(mChapterPosition).bookTitle);
                }
            }
        });

        if (mSpineReferenceList != null) {
            mFolioPageFragmentAdapter = new FolioPageFragmentAdapter(getSupportFragmentManager(), mSpineReferenceList, bookFileName, mBookId);
            mFolioPageViewPager.setAdapter(mFolioPageFragmentAdapter);

            entryReadPosition = getIntent().getParcelableExtra(FolioActivity.EXTRA_READ_POSITION);
            mFolioPageViewPager.setCurrentItem(getChapterIndex(entryReadPosition));

            if (FolioReader.readTotalPagesListener != null) {
                FolioReader.readTotalPagesListener.saveTotalPages(String.valueOf(mSpineReferenceList.size()));
            }


        }
    }


    /**
     * Returns the index of the chapter by following priority -
     * 1. id
     * 2. href
     * 3. index
     *
     * @param readPosition Last read position
     * @return index of the chapter
     */
    private int getChapterIndex(ReadPosition readPosition) {

        if (readPosition == null) {
            return 0;

        } else if (!TextUtils.isEmpty(readPosition.getChapterId())) {
            return getChapterIndex("id", readPosition.getChapterId());

        } else if (!TextUtils.isEmpty(readPosition.getChapterHref())) {
            return getChapterIndex("href", readPosition.getChapterHref());

        } else if (readPosition.getChapterIndex() > -1
                && readPosition.getChapterIndex() < mSpineReferenceList.size()) {
            return readPosition.getChapterIndex();
        }

        return 0;
    }

    private int getChapterIndex(String caseString, String value) {

        for (int i = 0; i < mSpineReferenceList.size(); i++) {
            switch (caseString) {
                case "id":
                    if (mSpineReferenceList.get(i).getId().equals(value))
                        return i;
                case "href":
                    if (mSpineReferenceList.get(i).getOriginalHref().equals(value))
                        return i;
            }
        }
        return 0;
    }

    private void configDrawerLayoutButtons() {
        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btn_config).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConfigBottomSheetDialogFragment = new ConfigBottomSheetDialogFragment();
                mConfigBottomSheetDialogFragment.show(getSupportFragmentManager(), mConfigBottomSheetDialogFragment.getTag());
            }
        });
    }

    @Override
    public void hideOrshowToolBar() {
        if (mIsActionBarVisible) {
            toolbarAnimateHide();
        } else {
            toolbarAnimateShow();
        }
    }

    @Override
    public void hideToolBarIfVisible() {
        if (mIsActionBarVisible) {
            toolbarAnimateHide();
        }
    }

    @Override
    public void setPagerToPosition(String href) {
    }

    @Override
    public ReadPosition getEntryReadPosition() {

        if (entryReadPosition != null) {
            ReadPosition tempReadPosition = entryReadPosition;
            entryReadPosition = null;
            return tempReadPosition;
        }
        return null;
    }

    @Override
    public void goToChapter(String href) {
        href = href.substring(href.indexOf(bookFileName + "/") + bookFileName.length() + 1);
        for (Link spine : mSpineReferenceList) {
            if (spine.href.contains(href)) {
                mChapterPosition = mSpineReferenceList.indexOf(spine);
                mFolioPageViewPager.setCurrentItem(mChapterPosition);
                title.setText(spine.getChapterTitle());
                break;
            }
        }
    }

    private void toolbarAnimateShow() {
        if (!mIsActionBarVisible) {
            mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            searchSection.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            mIsActionBarVisible = true;
        }
    }

    private void toolbarAnimateHide() {
        mIsActionBarVisible = false;
        mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
        searchSection.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2))
                .start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void toolbarSetElevation(float elevation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setElevation(elevation);
        }
    }

    public HighlightImpl setCurrentPagerPostion(HighlightImpl highlightImpl) {
//        highlight.setCurrentPagerPostion(mFolioPageViewPager.getCurrentItem());
        return highlightImpl;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTION_CONTENT_HIGHLIGHT && resultCode == RESULT_OK && data.hasExtra(TYPE)) {

            String type = data.getStringExtra(TYPE);
            if (type.equals(CHAPTER_SELECTED)) {
                String selectedChapterHref = data.getStringExtra(SELECTED_CHAPTER_POSITION);
                for (Link spine : mSpineReferenceList) {
                    if (selectedChapterHref.contains(spine.href)) {
                        mChapterPosition = mSpineReferenceList.indexOf(spine);
                        mFolioPageViewPager.setCurrentItem(mChapterPosition);
                        title.setText(data.getStringExtra(Constants.BOOK_TITLE));
                        EventBus.getDefault().post(new AnchorIdEvent(selectedChapterHref));
                        break;
                    }
                }
            } else if (type.equals(HIGHLIGHT_SELECTED)) {
                HighlightImpl highlightImpl = data.getParcelableExtra(HIGHLIGHT_ITEM);
                int position = highlightImpl.getPageNumber();
                mFolioPageViewPager.setCurrentItem(position);
                EventBus.getDefault().post(new WebViewPosition(mSpineReferenceList.get(mChapterPosition).href, highlightImpl.getRangy()));
            }
        }
    }

    @Override
    public void onShowSearchResults(SearchQueryResults results) {

        isForSearch = false;
        String query = results.getSearchResultList().get(0).getSearchQuery();
        searchImageClickListener.clearIndexes();
        searchImageClickListener.query = query;
        searchImageClickListener.indexes = getSearchIndexes(results);
        changeSearchIcon(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mEpubServer != null) {
            mEpubServer.stop();
        }
    }

    @Override
    public int getChapterPosition() {
        return mChapterPosition;
    }

    @Override
    public void onLoadPublication(EpubPublication publication) {
        mSpineReferenceList.addAll(publication.spines);


        if (publication.metadata.title != null) {
            title.setText(publication.metadata.title);
        }

        if (mBookId == null) {
            if (publication.metadata.identifier != null) {
                mBookId = publication.metadata.identifier;
            } else {
                if (publication.metadata.title != null) {
                    mBookId = String.valueOf(publication.metadata.title.hashCode());
                } else {
                    mBookId = String.valueOf(bookFileName.hashCode());
                }
            }
        }
        configFolio();
    }

    private void setConfig() {
        if (AppUtil.getSavedConfig(this) != null) {
            mConfig = AppUtil.getSavedConfig(this);

            if (isNightMode) {
                mConfig.setNightMode(isNightMode);
            } else {
                mConfig.setNightMode(isNightMode);
            }

            AppUtil.saveConfig(this, mConfig);

        } else if (getIntent().getParcelableExtra(Config.INTENT_CONFIG) != null) {
            mConfig = getIntent().getParcelableExtra(Config.INTENT_CONFIG);

            if (isNightMode) {
                mConfig.setNightMode(isNightMode);
            } else {
                mConfig.setNightMode(isNightMode);
            }


            AppUtil.saveConfig(this, mConfig);
        } else {
            mConfig = new Config.ConfigBuilder().setContext(this).nightmode(isNightMode).build();
            AppUtil.saveConfig(this, mConfig);
        }
    }


    //*************************************************************************//
    //                           AUDIO PLAYER                                  //
    //*************************************************************************//
    private StyleableTextView mHalfSpeed, mOneSpeed, mTwoSpeed, mOneAndHalfSpeed;
    private StyleableTextView mBackgroundColorStyle, mUnderlineStyle, mTextColorStyle;
    private RelativeLayout audioContainer;
    private boolean mIsSpeaking;
    private ImageButton mPlayPauseBtn, mPreviousButton, mNextButton;
    private RelativeLayout shade;

    private void initAudioView() {
        mHalfSpeed = findViewById(R.id.btn_half_speed);
        mOneSpeed = findViewById(R.id.btn_one_x_speed);
        mTwoSpeed = findViewById(R.id.btn_twox_speed);
        audioContainer = findViewById(R.id.container);
        shade = findViewById(R.id.shade);
        mOneAndHalfSpeed = findViewById(R.id.btn_one_and_half_speed);
        mPlayPauseBtn = findViewById(R.id.play_button);
        mPreviousButton = findViewById(R.id.prev_button);
        mNextButton = findViewById(R.id.next_button);
        mBackgroundColorStyle = findViewById(R.id.btn_backcolor_style);
        mUnderlineStyle = findViewById(R.id.btn_text_undeline_style);
        mTextColorStyle = findViewById(R.id.btn_text_color_style);
        mIsSpeaking = false;

        final Context mContext = mHalfSpeed.getContext();
        mOneAndHalfSpeed.setText(Html.fromHtml(mContext.getString(R.string.one_and_half_speed)));
        mHalfSpeed.setText(Html.fromHtml(mContext.getString(R.string.half_speed_text)));
        String styleUnderline =
                mHalfSpeed.getContext().getResources().getString(R.string.style_underline);
        mUnderlineStyle.setText(Html.fromHtml(styleUnderline));

        setupColors(mContext);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            findViewById(R.id.playback_speed_Layout).setVisibility(View.GONE);
        }

        shade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    audioContainer.startAnimation(slide_up);
                    audioContainer.setVisibility(View.VISIBLE);
                    shade.setVisibility(View.VISIBLE);
                } else {
                    audioContainer.startAnimation(slide_down);
                    audioContainer.setVisibility(View.INVISIBLE);
                    shade.setVisibility(View.GONE);
                }
                isOpen = !isOpen;
            }
        });

        mPlayPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsSpeaking) {
                    EventBus.getDefault().post(new MediaOverlayPlayPauseEvent(mSpineReferenceList.get(mChapterPosition).href, false, false));
                    mPlayPauseBtn.setImageDrawable(ContextCompat.getDrawable(FolioActivity.this, R.drawable.play_icon));
                    UiUtil.setColorToImage(mContext, mConfig.getThemeColor(), mPlayPauseBtn.getDrawable());
                } else {
                    EventBus.getDefault().post(new MediaOverlayPlayPauseEvent(mSpineReferenceList.get(mChapterPosition).href, true, false));
                    mPlayPauseBtn.setImageDrawable(ContextCompat.getDrawable(FolioActivity.this, R.drawable.pause_btn));
                    UiUtil.setColorToImage(mContext, mConfig.getThemeColor(), mPlayPauseBtn.getDrawable());
                }
                mIsSpeaking = !mIsSpeaking;
            }
        });

        mHalfSpeed.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                mHalfSpeed.setSelected(true);
                mOneSpeed.setSelected(false);
                mOneAndHalfSpeed.setSelected(false);
                mTwoSpeed.setSelected(false);
                EventBus.getDefault().post(new MediaOverlaySpeedEvent(MediaOverlaySpeedEvent.Speed.HALF));
            }
        });

        mOneSpeed.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                mHalfSpeed.setSelected(false);
                mOneSpeed.setSelected(true);
                mOneAndHalfSpeed.setSelected(false);
                mTwoSpeed.setSelected(false);
                EventBus.getDefault().post(new MediaOverlaySpeedEvent(MediaOverlaySpeedEvent.Speed.ONE));
            }
        });
        mOneAndHalfSpeed.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                mHalfSpeed.setSelected(false);
                mOneSpeed.setSelected(false);
                mOneAndHalfSpeed.setSelected(true);
                mTwoSpeed.setSelected(false);
                EventBus.getDefault().post(new MediaOverlaySpeedEvent(MediaOverlaySpeedEvent.Speed.ONE_HALF));
            }
        });
        mTwoSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHalfSpeed.setSelected(false);
                mOneSpeed.setSelected(false);
                mOneAndHalfSpeed.setSelected(false);
                mTwoSpeed.setSelected(true);
                EventBus.getDefault().post(new MediaOverlaySpeedEvent(MediaOverlaySpeedEvent.Speed.TWO));
            }
        });

        mBackgroundColorStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackgroundColorStyle.setSelected(true);
                mUnderlineStyle.setSelected(false);
                mTextColorStyle.setSelected(false);
                EventBus.getDefault().post(new MediaOverlayHighlightStyleEvent(MediaOverlayHighlightStyleEvent.Style.DEFAULT));
            }
        });

        mUnderlineStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackgroundColorStyle.setSelected(false);
                mUnderlineStyle.setSelected(true);
                mTextColorStyle.setSelected(false);
                EventBus.getDefault().post(new MediaOverlayHighlightStyleEvent(MediaOverlayHighlightStyleEvent.Style.UNDERLINE));

            }
        });

        mTextColorStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackgroundColorStyle.setSelected(false);
                mUnderlineStyle.setSelected(false);
                mTextColorStyle.setSelected(true);
                EventBus.getDefault().post(new MediaOverlayHighlightStyleEvent(MediaOverlayHighlightStyleEvent.Style.BACKGROUND));
            }
        });

    }

    private void setupColors(Context context) {
        mHalfSpeed.setTextColor(UiUtil.getColorList(context, mConfig.getThemeColor(), R.color.grey_color));
        mOneAndHalfSpeed.setTextColor(UiUtil.getColorList(context, mConfig.getThemeColor(), R.color.grey_color));
        mTwoSpeed.setTextColor(UiUtil.getColorList(context, mConfig.getThemeColor(), R.color.grey_color));
        mOneSpeed.setTextColor(UiUtil.getColorList(context, mConfig.getThemeColor(), R.color.grey_color));
        mUnderlineStyle.setTextColor(UiUtil.getColorList(context, mConfig.getThemeColor(), R.color.grey_color));
        mBackgroundColorStyle.setTextColor(UiUtil.getColorList(context, R.color.white, R.color.grey_color));
        mBackgroundColorStyle.setBackgroundDrawable(UiUtil.convertColorIntoStateDrawable(this, mConfig.getThemeColor(), android.R.color.transparent));
        mTextColorStyle.setTextColor(UiUtil.getColorList(context, mConfig.getThemeColor(), R.color.grey_color));
        UiUtil.setColorToImage(context, mConfig.getThemeColor(), mPlayPauseBtn.getDrawable());
        UiUtil.setColorToImage(context, mConfig.getThemeColor(), mNextButton.getDrawable());
        UiUtil.setColorToImage(context, mConfig.getThemeColor(), mPreviousButton.getDrawable());
    }

    @Override
    public void onError() {
    }


    public void initColors() {
        UiUtil.setColorToImage(this, mConfig.getThemeColor(), ((ImageView) findViewById(R.id.btn_close)).getDrawable());
        UiUtil.setColorToImage(this, mConfig.getThemeColor(), ((ImageView) findViewById(R.id.btn_search)).getDrawable());
        UiUtil.setColorToImage(this, mConfig.getThemeColor(), ((ImageView) findViewById(R.id.btn_drawer)).getDrawable());
        UiUtil.setColorToImage(this, mConfig.getThemeColor(), ((ImageView) findViewById(R.id.btn_config)).getDrawable());
        UiUtil.setColorToImage(this, mConfig.getThemeColor(), ((ImageView) findViewById(R.id.btn_speaker)).getDrawable());
    }

    private void setupBook() {
        bookFileName = FileUtil.getEpubFilename(this, mEpubSourceType, mEpubFilePath, mEpubRawId);
        initBook(bookFileName, mEpubRawId, mEpubFilePath, mEpubSourceType);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(Build.VERSION.SDK_INT>= 33){
            setupBook();
        }else{
            switch (requestCode) {

                case Constants.WRITE_EXTERNAL_STORAGE_REQUEST:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        setupBook();
                    } else {
                        Toast.makeText(this, getString(R.string.cannot_access_epub_message), Toast.LENGTH_LONG).show();
                        finish();
                    }
                    break;
            }
        }

    }

    /////////////////////////////////////////////SEARCH SECTION////////////////////////////////////////////////////////

    private ArrayList getSearchIndexes(SearchQueryResults results) {
        ArrayList<Integer> searchQueryIndexes = new ArrayList<>();
        for (int i = 0; i < results.getSearchResultList().size(); i++) {
            for (int j = 0; j < mSpineReferenceList.size(); j++) {
                if (mSpineReferenceList.get(j).getHref().equalsIgnoreCase(results.getSearchResultList().get(i)
                        .getResource())) {
                    searchQueryIndexes.add(j);
                    break;
                }
            }
        }
        return searchQueryIndexes;
    }

    @Override
    public String getSearchQuery() {
        if (mSearchText.getText() == null) {
            return null;
        } else {
            String searchQuery = mSearchText.getText().toString();
            if (!searchQuery.isEmpty()) {
                if (searchQuery.contains(" ")) {
                    searchQuery = searchQuery.replaceAll(" ", "%20");
                }
                if (searchQuery.length() != 0) {
                    return Constants.LOCALHOST + bookFileName + "/search?query=" + searchQuery;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }


    private void clearSearchSection() {
        if (mSearchText != null)
            mSearchText.getText().clear();
        changeSearchIcon(true);

    }

    private void changeSearchIcon(boolean doSearch) {
        if (doSearch/* && (int)searchImage.getTag()== DOWN_ARROW_ICON*/) {
            searchImage.setTag(SEARCH_ICON);
            searchImage.setImageResource(R.drawable.ic_search_white_24px);
            findViewById(R.id.cancel_img).setVisibility(View.GONE);
            searchSection.invalidate();
        } else /* if (!doSearch && (int)searchImage.getTag()== SEARCH_ICON)*/ {
            searchImage.setTag(DOWN_ARROW_ICON);
            searchImage.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24);
            findViewById(R.id.cancel_img).setVisibility(View.VISIBLE);
            searchSection.invalidate();
        }
    }

    private void clearSearchHighlights() {
        EventBus.getDefault().post(new ClearSearchEvent());
    }

    private void search() {
        isForSearch = true;
        if (!mIsSearchSectionVisible) {
            searchAnimateShow();
            clearSearchSection();
        } else {
            searchAnimateHide();
            clearSearchHighlights();
        }
    }

    private void searchAnimateShow() {
        if (!mIsSearchSectionVisible) {
            mIsSearchSectionVisible = true;
            searchSection.setVisibility(View.VISIBLE);
            toolbarAnimateHide();
        }
    }

    private void searchAnimateHide() {
        mIsSearchSectionVisible = false;
        searchSection.setVisibility(View.GONE);
    }

    class SearchImageClickListener implements View.OnClickListener {

        public ArrayList<Integer> indexes;
        private int currentIndex = 0, oldIndex = 0;
        private int count = 0;
        public String query, uniqueID;
        private int fragmentPos;
        private boolean onEndPos = false;

        @Override
        public void onClick(View view) {
            // TODO: 22.04.2018 close keyboard if open
            if (isForSearch) {
                new MainPresenter(FolioActivity.this).searchQuery();
            } else {
                if (indexes != null && query != null) {
                    if (indexes.size() > currentIndex) {
                        boolean isNew = true;
                        if (currentIndex > 0 && (int) indexes.get(currentIndex - 1) == ((int) indexes.get
                                (currentIndex))) {
                            isNew = false;
                            count++;
                        } else {
                            changeCurrentIndex();
                            uniqueID = UUID.randomUUID().toString();
                            count = 0;
                            mChapterPosition = indexes.get(currentIndex);
                            mFolioPageViewPager.setCurrentItem(mChapterPosition);
                        }
                        oldIndex = currentIndex;
                        EventBus.getDefault().post(new SearchEvent(query, isNew, count, uniqueID));
                        currentIndex++;
                        onEndPos = false;
                    } else {
                        // TODO: 21.04.2018 change icon & no restart since it may leak
                        onEndPos = true;
                        currentIndex = 0;
                        oldIndex = 0;
                        view.performClick();
                    }
                }
            }
        }

        public void clearIndexes() {
            currentIndex = 0;
            oldIndex = 0;
            count = 0;
            onEndPos = false;
        }

        private void changeCurrentIndex() {
            fragmentPos = mFolioPageViewPager.getCurrentItem();
            if (indexes != null) {
                if (fragmentPos != indexes.get(oldIndex)) {
                    for (int i = 0; i < indexes.size(); i++) {
                        if (indexes.get(i) == fragmentPos && !onEndPos) {
                            currentIndex = i;
                            break;
                        }
                    }
                }
            }
        }
    }
}
