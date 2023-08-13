package com.softperl.urdunovelscollections.AdapterUtil;

import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.folioreader.model.ReadPositionImpl;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.softperl.urdunovelscollections.ConstantUtil.Constant;
import com.softperl.urdunovelscollections.CustomUtil.GlideApp;
import com.softperl.urdunovelscollections.CustomUtil.GridSpacingItemDecoration;
import com.softperl.urdunovelscollections.FontUtil.Font;
import com.softperl.urdunovelscollections.InterfaceUtil.HomeCallback;
import com.softperl.urdunovelscollections.ObjectUtil.AdObject;
import com.softperl.urdunovelscollections.ObjectUtil.BarObject;
import com.softperl.urdunovelscollections.ObjectUtil.DataObject;
import com.softperl.urdunovelscollections.ObjectUtil.EmptyObject;
import com.softperl.urdunovelscollections.ObjectUtil.HomeObject;
import com.softperl.urdunovelscollections.ObjectUtil.NativeAdObject;
import com.softperl.urdunovelscollections.ObjectUtil.ProgressObject;
import com.softperl.urdunovelscollections.ObjectUtil.SearchObject;
import com.softperl.urdunovelscollections.R;
import com.softperl.urdunovelscollections.Utility.Utility;
import com.ixidev.gdpr.GDPRChecker;
import com.jcminarro.roundkornerlayout.RoundKornerRelativeLayout;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hp on 5/5/2018.
 */

public abstract class HomeAdapter extends RecyclerView.Adapter {
    private Gson gson;
    private int NO_DATA_VIEW = 1;
    private int HOME_VIEW = 2;
    private int PROGRESS_VIEW = 3;
    private int NATIVE_VIEW = 4;
    private int RADIO_VIEW = 5;
    private int BAR_VIEW = 6;
    private int POPULAR_VIEW = 7;
    private int CATEGORY_VIEW = 8;
    private int FEED_VIEW = 9;
    private int AUTHOR_VIEW = 10;
    private int FEATURED_VIEW = 11;
    private int SEARCH_VIEW = 12;
    private int FOR_YOU_VIEW = 13;
    private int ADMOB_VIEW = 14;
    private int HISTORY_VIEW = 15;
    private Context context;
    private HomeCallback homeCallback;
    private ArrayList<Object> wallpaperArray = new ArrayList<>();
    private String TAG = HomeAdapter.class.getName();


    public HomeAdapter(Context context, ArrayList<Object> wallpaperArray) {
        this.context = context;
        this.wallpaperArray = wallpaperArray;
        gson = new Gson();
    }

    public HomeAdapter(Context context, ArrayList<Object> wallpaperArray, HomeCallback homeCallback) {
        this.context = context;
        this.wallpaperArray = wallpaperArray;
        this.homeCallback = homeCallback;
        gson = new Gson();
    }

    @Override
    public int getItemViewType(int position) {

        if (wallpaperArray.get(position) instanceof EmptyObject) {
            return NO_DATA_VIEW;
        } else if (wallpaperArray.get(position) instanceof HomeObject) {

            HomeObject homeObject = (HomeObject) wallpaperArray.get(position);

            if (homeObject.getData_type() == Constant.DATA_TYPE.FEED) {
                return FEED_VIEW;
            } else if (homeObject.getData_type() == Constant.DATA_TYPE.COMMON) {
                return FOR_YOU_VIEW;
            } else if (homeObject.getData_type() == Constant.DATA_TYPE.HISTORY) {
                return HOME_VIEW;
            } else {
                return HOME_VIEW;
            }

        } else if (wallpaperArray.get(position) instanceof DataObject) {

            DataObject dataObject = (DataObject) wallpaperArray.get(position);

            if (dataObject.getDataType() == Constant.DATA_TYPE.POPULAR) {
                return POPULAR_VIEW;
            } else if (dataObject.getDataType() == Constant.DATA_TYPE.CATEGORIES) {
                return CATEGORY_VIEW;
            } else if (dataObject.getDataType() == Constant.DATA_TYPE.FEED) {
                return FEED_VIEW;
            } else if (dataObject.getDataType() == Constant.DATA_TYPE.ARTIST) {
                return AUTHOR_VIEW;
            } else if (dataObject.getDataType() == Constant.DATA_TYPE.FEATURED) {
                return FEATURED_VIEW;
            } else {
                return HISTORY_VIEW;
            }


        } else if (wallpaperArray.get(position) instanceof ProgressObject) {
            return PROGRESS_VIEW;
        } else if (wallpaperArray.get(position) instanceof NativeAdObject) {
            return NATIVE_VIEW;
        } else if (wallpaperArray.get(position) instanceof BarObject) {
            return BAR_VIEW;
        } else if (wallpaperArray.get(position) instanceof SearchObject) {
            return SEARCH_VIEW;
        } else if (wallpaperArray.get(position) instanceof AdObject) {
            return ADMOB_VIEW;
        }

        return NO_DATA_VIEW;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == NO_DATA_VIEW) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_item_layout, parent, false);
            viewHolder = new EmptyHolder(view);

        } else if (viewType == HOME_VIEW) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item_layout, parent, false);
            viewHolder = new HomeHolder(view);

        } else if (viewType == POPULAR_VIEW) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_item_layout, parent, false);
            viewHolder = new PopularHolder(view);

        } else if (viewType == CATEGORY_VIEW) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorized_item_layout, parent, false);
            viewHolder = new CategoryHolder(view);

        } else if (viewType == FEED_VIEW) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item_layout, parent, false);
            viewHolder = new FeedHolder(view);

        } else if (viewType == AUTHOR_VIEW) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.author_item_layout, parent, false);
            viewHolder = new AuthorHolder(view);

        } else if (viewType == FEATURED_VIEW) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_item_layout, parent, false);
            viewHolder = new FeaturedHolder(view);

        } else if (viewType == RADIO_VIEW) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_data_item_layout, parent, false);
            viewHolder = new RadioHolder(view);

        } else if (viewType == PROGRESS_VIEW) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item_layout, parent, false);
            viewHolder = new ProgressHolder(view);

        } else if (viewType == NATIVE_VIEW) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.base_native_item_layout, parent, false);
            viewHolder = new NativeAdHolder(view);

        } else if (viewType == SEARCH_VIEW) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_layout, parent, false);
            viewHolder = new SearchHolder(view);

        } else if (viewType == FOR_YOU_VIEW) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.for_you_item_layout, parent, false);
            viewHolder = new ForYouHolder(view);

        } else if (viewType == ADMOB_VIEW) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_item_layout, parent, false);
            viewHolder = new AdHolder(view);

        } else if (viewType == HISTORY_VIEW) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.read_book_item_layout, parent, false);
            viewHolder = new BookHolder(view);

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ProgressHolder) {

            //LookUpObject lookUpObject = (LookUpObject) wallpaperArray.get(position);
            ProgressHolder lookUpHolder = (ProgressHolder) holder;


        } else if (holder instanceof EmptyHolder) {

            EmptyHolder emptyHolder = (EmptyHolder) holder;
            EmptyObject emptyObject = (EmptyObject) wallpaperArray.get(position);

            emptyHolder.imageIcon.setImageResource(emptyObject.getPlaceHolderIcon());
            emptyHolder.txtTitle.setText(emptyObject.getTitle());
            emptyHolder.txtDescription.setText(emptyObject.getDescription());


        } else if (holder instanceof RadioHolder) {

            DataObject dataObject = (DataObject) wallpaperArray.get(position);
            final RadioHolder wallpaperHolder = (RadioHolder) holder;

            wallpaperHolder.txtArtist.setText(dataObject.getArtistName());
            wallpaperHolder.txtName.setText(dataObject.getTitle());
            wallpaperHolder.layoutCategory.setTag(position);
            wallpaperHolder.coverRadio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) wallpaperHolder.layoutCategory.getTag();
                    select(false, pos);
                }
            });

            GlideApp.with(context).load(Constant.ServerInformation.PICTURE_URL + dataObject.getOriginalUrl())
                    .into(wallpaperHolder.coverRadio);


        } else if (holder instanceof HomeHolder) {

            HomeObject dataObject = (HomeObject) wallpaperArray.get(position);
            final HomeHolder homeHolder = (HomeHolder) holder;
            ArrayList<Object> list = new ArrayList<>();

            homeHolder.txtLabel.setText(dataObject.getLabel());
            homeHolder.txtTitle.setText(dataObject.getTitle());
            list.addAll(dataObject.getDataObjectArrayList());

            Utility.Logger(TAG, "List Size = " + list.size() + " Data Size = " + dataObject.getDataObjectArrayList().size());

            homeHolder.txtLabel.setTag(position);

            if (((HomeObject) wallpaperArray.get(position)).getData_type() == Constant.DATA_TYPE.FEATURED)
                homeHolder.txtMore.setVisibility(View.GONE);

            homeHolder.txtMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = (int) homeHolder.txtLabel.getTag();
                    HomeObject homeObject = (HomeObject) wallpaperArray.get(pos);
                    if (homeCallback != null) {
                        homeCallback.onMore(homeObject.getData_type());
                    }

                }
            });

            homeHolder.homeAdapter = new HomeAdapter(context, list) {
                @Override
                public void select(boolean isLocked, int position) {
                    int pos = (int) homeHolder.txtLabel.getTag();
                    homeCallback.onSelect(pos, position);
                }
            };
            homeHolder.recyclerViewRadio.setAdapter(homeHolder.homeAdapter);


        } else if (holder instanceof PopularHolder) {

            DataObject dataObject = (DataObject) wallpaperArray.get(position);
            final PopularHolder popularHolder = (PopularHolder) holder;

            popularHolder.txtBook.setText(dataObject.getTitle());
            popularHolder.txtAuthor.setText(dataObject.getArtistName());
            popularHolder.layoutBook.setTag(position);
            popularHolder.layoutBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = (int) popularHolder.layoutBook.getTag();
                    select(false, pos);

                }
            });

            GlideApp.with(context).load(Constant.ServerInformation.PICTURE_URL + dataObject.getOriginalUrl())
                    .into(popularHolder.imageCover);


        } else if (holder instanceof CategoryHolder) {

            DataObject dataObject = (DataObject) wallpaperArray.get(position);
            final CategoryHolder categoryHolder = (CategoryHolder) holder;

            categoryHolder.txtCategories.setText(dataObject.getCategoryTitle());
            categoryHolder.layoutCategories.setTag(position);
            categoryHolder.layoutCategories.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = (int) categoryHolder.layoutCategories.getTag();
                    select(false, pos);

                }
            });

            GlideApp.with(context).load(Constant.ServerInformation.PICTURE_URL + dataObject.getCategoryPicture())
                    .into(categoryHolder.imageCategories);


        } else if (holder instanceof SearchHolder) {

            final SearchHolder searchHolder = (SearchHolder) holder;

            searchHolder.layoutSearch.setTag(position);
            searchHolder.layoutSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (homeCallback != null) {
                        homeCallback.onSelectSearch();
                    }

                }
            });


        } else if (holder instanceof ForYouHolder) {

            HomeObject dataObject = (HomeObject) wallpaperArray.get(position);
            final ForYouHolder forYouHolder = (ForYouHolder) holder;

            forYouHolder.txtLabel.setText(dataObject.getLabel());
            forYouHolder.txtTitle.setText(dataObject.getTitle());

            forYouHolder.txtMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (homeCallback != null) {
                        homeCallback.onMore(Constant.DATA_TYPE.FEED);
                    }

                }
            });


        } else if (holder instanceof FeedHolder) {

            DataObject dataObject = (DataObject) wallpaperArray.get(position);
            final FeedHolder feedHolder = (FeedHolder) holder;

            feedHolder.txtBook.setText(dataObject.getTitle());

            Document doc = Jsoup.parse(dataObject.getDescription());
            feedHolder.txtDescription.setText(doc.text());

            feedHolder.txtCount.setText(dataObject.getDownloads());
            feedHolder.layoutRead.setTag(position);
            feedHolder.layoutRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = (int) feedHolder.layoutRead.getTag();
                    if (homeCallback != null)
                        homeCallback.onSelect(-1, pos);

                }
            });

            GlideApp.with(context).load(Constant.ServerInformation.PICTURE_URL + dataObject.getOriginalUrl())
                    .into(feedHolder.imageCover);


        } else if (holder instanceof AuthorHolder) {

            DataObject dataObject = (DataObject) wallpaperArray.get(position);
            final AuthorHolder authorHolder = (AuthorHolder) holder;

            authorHolder.txtAuthor.setText(dataObject.getTitle());
            authorHolder.txtWork.setText(dataObject.getAuthorWork() + " " + Utility.getStringFromRes(context, R.string.work));
            authorHolder.layoutAuthor.setBackgroundColor(Utility.getAuthorCardColour(context, position));

            authorHolder.layoutAuthor.setTag(position);
            authorHolder.layoutAuthor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = (int) authorHolder.layoutAuthor.getTag();
                    select(false, pos);

                }
            });

            GlideApp.with(context).load(Constant.ServerInformation.PICTURE_URL + dataObject.getOriginalUrl())
                    .into(authorHolder.imageCover);


        } else if (holder instanceof FeaturedHolder) {

            DataObject dataObject = (DataObject) wallpaperArray.get(position);
            final FeaturedHolder featuredHolder = (FeaturedHolder) holder;

            featuredHolder.txtBook.setText(dataObject.getTitle());
            featuredHolder.txtAuthor.setText(dataObject.getArtistName());

//            if(dataObject.getRating().equalsIgnoreCase(Constant.DEFAULT_RATING)){
//                featuredHolder.ratingBook.setRating(Float.parseFloat("5.0"));
//            }else {
//                featuredHolder.ratingBook.setRating(Float.parseFloat("5.0"));
//            }
            featuredHolder.ratingBook.setRating(Float.parseFloat(Constant.DEFAULT_RATING));


            featuredHolder.layoutBook.setTag(position);
            featuredHolder.layoutBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = (int) featuredHolder.layoutBook.getTag();
                    select(false, pos);

                }
            });


            GlideApp.with(context).load(Constant.ServerInformation.PICTURE_URL + dataObject.getOriginalUrl())
                    .into(featuredHolder.imageCover);


        } else if (holder instanceof BookHolder) {

            DataObject dataObject = (DataObject) wallpaperArray.get(position);
            final BookHolder bookHolder = (BookHolder) holder;

            bookHolder.txtBook.setText(dataObject.getTitle());
            double percentage = 0.0;

            if (dataObject.getFileType().equalsIgnoreCase(Constant.DataType.EPUB)) {

                ReadPositionImpl readPosition = gson.fromJson(dataObject.getCurrentPage(), ReadPositionImpl.class);
                percentage = ((readPosition.getChapterIndex() + 0.0) / Double.parseDouble(dataObject.getBookPage())) * 100.0;

                Utility.Logger(TAG, "File Type : " + dataObject.getFileType() + " Percentage = " + percentage);

            } else if (dataObject.getFileType().equalsIgnoreCase(Constant.DataType.PDF)) {

                percentage = Double.parseDouble(dataObject.getCurrentPage()) / Double.parseDouble((dataObject.getBookPage())) * 100.0;

                Utility.Logger(TAG, "File Type : " + dataObject.getFileType() + " Percentage = " + percentage);

            }

            Utility.Logger(TAG, "Data = Total Pages : " + dataObject.getBookPage()
                    + " Current Page : " + dataObject.getCurrentPage() + " Book Type : " + dataObject.getFileType() +
                    " Percentage : " + percentage);

            bookHolder.txtReadingPercentage.setText((int) percentage + " %");
            bookHolder.progressBar.setProgress((int) percentage);

            bookHolder.layoutBook.setTag(position);
            bookHolder.imageCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) bookHolder.layoutBook.getTag();
                    select(false, pos);
                }
            });

            GlideApp.with(context).load(Constant.ServerInformation.PICTURE_URL + dataObject.getCoverUrl())
                    .into(bookHolder.imageCover);


        }


    }

    @Override
    public int getItemCount() {
        return wallpaperArray.size();

    }

    public abstract void select(boolean isLocked, int position);

    protected class EmptyHolder extends RecyclerView.ViewHolder {
        private ImageView imageIcon;
        private TextView txtTitle;
        private TextView txtDescription;

        public EmptyHolder(View view) {
            super(view);

            imageIcon = (ImageView) view.findViewById(R.id.image_icon);
            txtTitle = (TextView) view.findViewById(R.id.txt_title);
            txtDescription = (TextView) view.findViewById(R.id.txt_description);
        }
    }

    protected class RadioHolder extends RecyclerView.ViewHolder {
        private ImageView coverRadio;
        private TextView txtName;
        private TextView txtArtist;
        private LinearLayout layoutCategory;
        private RelativeLayout imageLocked;

        public RadioHolder(View view) {
            super(view);
            coverRadio = (ImageView) view.findViewById(R.id.cover_radio);
            layoutCategory = view.findViewById(R.id.layout_category);
            txtName = view.findViewById(R.id.txt_name);
            txtArtist = view.findViewById(R.id.txt_artist);
            //imageLocked = view.findViewById(R.id.image_locked);
        }

    }

    protected class ProgressHolder extends RecyclerView.ViewHolder {
        private GeometricProgressView progressView;

        public ProgressHolder(View view) {
            super(view);
            progressView = (GeometricProgressView) view.findViewById(R.id.progressView);
        }

    }

    protected class NativeAdHolder extends RecyclerView.ViewHolder {
        private final LinearLayout layoutAd;
        private NativeAd mNativeBannerAd;

        public NativeAdHolder(View view) {
            super(view);

            layoutAd = (LinearLayout) view.findViewById(R.id.layout_ad);
            /*List<String> testDevices = new ArrayList<>();
            testDevices.add(Constant.Credentials.ADMOB_TEST_DEVICE_ID);
            testDevices.add("HASHED_ID_2");
            AdSettings.addTestDevices(testDevices);*/
            mNativeBannerAd = new NativeAd(context, Constant.Credentials.FB_AD_PLACEMENT_ID);
            NativeAdListener nativeAdListener = new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {

                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    Utility.Logger("Erro Ad", adError.getErrorMessage());
                    Log.d("FBADSTAG", "onError: "+adError.getErrorMessage());

                }

                @Override
                public void onAdLoaded(Ad ad) {

                    mNativeBannerAd.unregisterView();
                    Log.d("FBADSTAG", "onAdLoaded: Loaded");

                    // Add the Ad view into the ad container.

                    LayoutInflater inflater = LayoutInflater.from(context);
                    // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                    RelativeLayout adView = (RelativeLayout) inflater.inflate(R.layout.native_ad_item_layout, layoutAd, false);
                    //layoutAd.addView(adView);

                    // Add the AdChoices icon
                    /*LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
                    AdChoicesView adChoicesView = new AdChoicesView(context, mNativeBannerAd, true);
                    adChoicesContainer.addView(adChoicesView, 0);*/

                    // Create native UI using the ad metadata.
                    MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
                    TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
                    MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
                    TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
                    //TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
                    TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
                    Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);
                    nativeAdCallToAction.setTypeface(Font.ubuntu_regular_font(context));

                    // Set the Text.
                    nativeAdTitle.setText(mNativeBannerAd.getAdvertiserName());
                    ///nativeAdBody.setText(mNativeBannerAd.getAdBodyText());
                    nativeAdSocialContext.setText(mNativeBannerAd.getAdSocialContext());
                    nativeAdCallToAction.setVisibility(mNativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
                    nativeAdCallToAction.setText(mNativeBannerAd.getAdCallToAction());
                    sponsoredLabel.setText(mNativeBannerAd.getSponsoredTranslation());

                    // Create a list of clickable views
                    List<View> clickableViews = new ArrayList<>();
                    clickableViews.add(nativeAdTitle);
                    clickableViews.add(nativeAdCallToAction);

                    // Register the Title and CTA button to listen for clicks.
                    mNativeBannerAd.registerViewForInteraction(
                            adView,
                            nativeAdMedia,
                            nativeAdIcon,
                            clickableViews);
                    // Add the Native Banner Ad View to your ad container
                    layoutAd.addView(adView);

                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            };

            // Initiate a request to load an ad.
            mNativeBannerAd.loadAd(
                    mNativeBannerAd.buildLoadAdConfig()
                            .withAdListener(nativeAdListener)
                            .build());
            Log.d("FBADSTAG", "onError: ");


        }
    }

    protected class HomeHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle;
        private TextView txtLabel;
        private TextView txtMore;
        private GridLayoutManager gridLayoutManager;
        private RecyclerView recyclerViewRadio;
        private HomeAdapter homeAdapter;

        public HomeHolder(View view) {
            super(view);

            txtTitle = (TextView) view.findViewById(R.id.txt_title);
            txtLabel = (TextView) view.findViewById(R.id.txt_label);
            txtMore = view.findViewById(R.id.txt_more);

            gridLayoutManager = new GridLayoutManager(context, 1, LinearLayoutManager.HORIZONTAL, false);
            recyclerViewRadio = (RecyclerView) view.findViewById(R.id.recycler_view_radio);
            recyclerViewRadio.setLayoutManager(gridLayoutManager);

            int spanCount = 5; // 3 columns
            int spacing = 15; // 50px
            boolean includeEdge = true;
            recyclerViewRadio.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));


        }
    }

    protected class PopularHolder extends RecyclerView.ViewHolder {
        private LinearLayout layoutBook;
        private ImageView imageCover;
        private TextView txtBook;
        private TextView txtAuthor;

        public PopularHolder(View view) {
            super(view);

            layoutBook = (LinearLayout) view.findViewById(R.id.layout_book);
            imageCover = (ImageView) view.findViewById(R.id.image_cover);
            txtBook = (TextView) view.findViewById(R.id.txt_book);
            txtAuthor = (TextView) view.findViewById(R.id.txt_author);

        }
    }

    protected class CategoryHolder extends RecyclerView.ViewHolder {
        private LinearLayout layoutCategories;
        private ImageView imageCategories;
        private TextView txtCategories;

        public CategoryHolder(View view) {
            super(view);

            layoutCategories = (LinearLayout) view.findViewById(R.id.layout_categories);
            imageCategories = (ImageView) view.findViewById(R.id.image_categories);
            txtCategories = (TextView) view.findViewById(R.id.txt_categories);

        }
    }

    protected class SearchHolder extends RecyclerView.ViewHolder {
        private LinearLayout layoutSearch;

        public SearchHolder(View view) {
            super(view);

            layoutSearch = view.findViewById(R.id.layout_search);

        }

    }

    protected class ForYouHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle;
        private TextView txtLabel;
        private TextView txtMore;

        public ForYouHolder(View view) {
            super(view);

            txtTitle = (TextView) view.findViewById(R.id.txt_title);
            txtLabel = (TextView) view.findViewById(R.id.txt_label);
            txtMore = view.findViewById(R.id.txt_more);

        }
    }

    protected class FeedHolder extends RecyclerView.ViewHolder {
        private ImageView imageCover;
        private TextView txtBook;
        private TextView txtDescription;
        private TextView txtCount;
        private LinearLayout layoutRead;

        public FeedHolder(View view) {
            super(view);

            imageCover = (ImageView) view.findViewById(R.id.image_cover);
            txtBook = (TextView) view.findViewById(R.id.txt_book);
            txtDescription = (TextView) view.findViewById(R.id.txt_description);
            txtCount = (TextView) view.findViewById(R.id.txt_count);
            layoutRead = (LinearLayout) view.findViewById(R.id.layout_read);

        }
    }

    protected class AuthorHolder extends RecyclerView.ViewHolder {
        private RoundKornerRelativeLayout layoutAuthor;
        private TextView txtAuthor;
        private TextView txtWork;
        private ImageView imageCover;

        public AuthorHolder(View view) {
            super(view);

            layoutAuthor = view.findViewById(R.id.layout_author);
            txtAuthor = (TextView) view.findViewById(R.id.txt_author);
            txtWork = (TextView) view.findViewById(R.id.txt_work);
            imageCover = (ImageView) view.findViewById(R.id.image_cover);
        }
    }

    protected class FeaturedHolder extends RecyclerView.ViewHolder {
        private LinearLayout layoutBook;
        private ImageView imageCover;
        private TextView txtBook;
        private TextView txtAuthor;
        private RatingBar ratingBook;

        public FeaturedHolder(View view) {
            super(view);

            layoutBook = (LinearLayout) view.findViewById(R.id.layout_book);
            imageCover = (ImageView) view.findViewById(R.id.image_cover);
            txtBook = (TextView) view.findViewById(R.id.txt_book);
            txtAuthor = (TextView) view.findViewById(R.id.txt_author);
            ratingBook = view.findViewById(R.id.rating_book);

        }
    }

    protected class AdHolder extends RecyclerView.ViewHolder {
        LinearLayout mAdView;

        public AdHolder(View view) {
            super(view);


            mAdView = view.findViewById(R.id.adView);
            mAdView.setVisibility(View.VISIBLE);

            AdView adView = new AdView(context);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(Constant.Credentials.ADMOB_BANNER_ID);

            AdRequest.Builder adRequest = new AdRequest.Builder();

            GDPRChecker.Request request = GDPRChecker.getRequest();
            if (request == GDPRChecker.Request.NON_PERSONALIZED) {
                // load non Personalized ads
                Bundle extras = new Bundle();
                extras.putString("npa", "1");
                adRequest.addNetworkExtrasBundle(AdMobAdapter.class, extras);
            } // else do nothing , it will load PERSONALIZED ads

            adView.loadAd(adRequest.build());
            mAdView.addView(adView);


        }
    }

    protected class BookHolder extends RecyclerView.ViewHolder {
        private ImageView imageCover;
        private TextView txtReadingPercentage;
        private TextView txtBook;
        private LinearLayout layoutBook;
        private ProgressBar progressBar;

        public BookHolder(View view) {
            super(view);

            txtBook = view.findViewById(R.id.txt_book);
            progressBar = view.findViewById(R.id.progress);
            imageCover = (ImageView) view.findViewById(R.id.image_cover);
            txtReadingPercentage = view.findViewById(R.id.txt_reading_percentage);
            layoutBook = view.findViewById(R.id.layout_book);
        }

    }

}
