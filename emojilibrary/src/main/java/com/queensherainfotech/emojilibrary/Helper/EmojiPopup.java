package com.queensherainfotech.emojilibrary.Helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.queensherainfotech.emojilibrary.R;
import com.queensherainfotech.emojilibrary.Emoji.Cars;
import com.queensherainfotech.emojilibrary.Emoji.Electr;
import com.queensherainfotech.emojilibrary.Emoji.Emojicon;
import com.queensherainfotech.emojilibrary.Emoji.Food;
import com.queensherainfotech.emojilibrary.Emoji.Nature;
import com.queensherainfotech.emojilibrary.Emoji.People;
import com.queensherainfotech.emojilibrary.Emoji.Sport;
import com.queensherainfotech.emojilibrary.Emoji.Symbols;

import java.util.Arrays;
import java.util.List;

public class EmojiPopup extends PopupWindow implements ViewPager.OnPageChangeListener, EmojiRecents {
    private int mEmojiTabLastSelectedIndex = -1;
    private View[] mEmojiTabs;
    private PagerAdapter mEmojisAdapter;
    private EmojiRecentsManager mRecentsManager;
    private int keyBoardHeight = 0;
    private Boolean pendingOpen = false;
    private Boolean isOpened = false;
    public EmojiGridView.OnEmojiconClickedListener onEmojiconClickedListener;
    OnEmojiBackspaceClickedListener onEmojiBackspaceClickedListener;
    OnSoftKeyboardOpenCloseListener onSoftKeyboardOpenCloseListener;
    View rootView;
    Context mContext;
    boolean mUseSystemDefault = false;
    View view;
    int positionPager = 0;
    boolean setColor = false;
    int iconPressedColor = Color.parseColor("#495C66");
    int tabsColor = Color.parseColor("#DCE1E2");
    int backgroundColor = Color.parseColor("#E6EBEF");

    private ViewPager emojisPager;

    public EmojiPopup(View rootView, Context mContext, boolean useSystemDefault) {
        super(mContext);
        this.mUseSystemDefault = useSystemDefault;
        this.mContext = mContext;
        this.rootView = rootView;
        View customView = createCustomView();
        setContentView(customView);
        setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        setSize(LayoutParams.MATCH_PARENT, 255);
        setBackgroundDrawable(null);
    }

    public void setOnSoftKeyboardOpenCloseListener(OnSoftKeyboardOpenCloseListener listener) {
        this.onSoftKeyboardOpenCloseListener = listener;
    }

    public void setOnEmojiconClickedListener(EmojiGridView.OnEmojiconClickedListener listener) {
        this.onEmojiconClickedListener = listener;
    }

    public void setOnEmojiBackspaceClickedListener(OnEmojiBackspaceClickedListener listener) {
        this.onEmojiBackspaceClickedListener = listener;
    }

    public void showAtBottom() {
        showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

    public void showAtBottomPending() {
        if (isKeyBoardOpen()) {
            showAtBottom();
        } else {
            pendingOpen = true;
        }
    }

    public Boolean isKeyBoardOpen() {
        return isOpened;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        EmojiRecentsManager.getInstance(mContext)
                .saveRecents();
    }

    public void setSizeForSoftKeyboard() {
        rootView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        rootView.getWindowVisibleDisplayFrame(r);

                        int screenHeight = getUsableScreenHeight();
                        int heightDifference = screenHeight - (r.bottom - r.top);
                        int resourceId = mContext.getResources()
                                .getIdentifier("status_bar_height", "dimen", "android");
                        if (resourceId > 0) {
                            heightDifference -= mContext.getResources()
                                    .getDimensionPixelSize(resourceId);
                        }
                        if (heightDifference > 100) {
                            keyBoardHeight = heightDifference;
                            setSize(LayoutParams.MATCH_PARENT, keyBoardHeight);
                            if (isOpened == false) {
                                if (onSoftKeyboardOpenCloseListener != null) {
                                    onSoftKeyboardOpenCloseListener.onKeyboardOpen(keyBoardHeight);
                                }
                            }
                            isOpened = true;
                            if (pendingOpen) {
                                showAtBottom();
                                pendingOpen = false;
                            }
                        } else {
                            isOpened = false;
                            if (onSoftKeyboardOpenCloseListener != null) {
                                onSoftKeyboardOpenCloseListener.onKeyboardClose();
                            }
                        }
                    }
                });
    }

    private int getUsableScreenHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();

            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay()
                    .getMetrics(metrics);

            return metrics.heightPixels;

        } else {
            return rootView.getRootView()
                    .getHeight();
        }
    }

    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public void updateUseSystemDefault(boolean mUseSystemDefault) {
        if (view != null) {
            mEmojisAdapter = null;
            positionPager = emojisPager.getCurrentItem();
            dismiss();

            this.mUseSystemDefault = mUseSystemDefault;
            setContentView(createCustomView());
            mEmojiTabs[positionPager].setSelected(true);
            emojisPager.setCurrentItem(positionPager);
            onPageSelected(positionPager);
            if (!isShowing()) {
                if (isKeyBoardOpen()) {
                    showAtBottom();
                }
                else {
                    showAtBottomPending();
                }
            }
        }
    }


    private View createCustomView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.emojis, null, false);
        emojisPager = (ViewPager) view.findViewById(R.id.emojis_pager);
        LinearLayout tabs = (LinearLayout) view.findViewById(R.id.emojis_tab);

        emojisPager.setOnPageChangeListener(this);
        EmojiRecents recents = this;
        mEmojisAdapter = new EmojisPagerAdapter(Arrays.asList(new EmojiRecentsGridView(mContext, null, null, this, mUseSystemDefault),
                new EmojiGridView(mContext, People.DATA, recents, this, mUseSystemDefault),
                new EmojiGridView(mContext, Nature.DATA, recents, this, mUseSystemDefault),
                new EmojiGridView(mContext, Food.DATA, recents, this, mUseSystemDefault),
                new EmojiGridView(mContext, Sport.DATA, recents, this, mUseSystemDefault),
                new EmojiGridView(mContext, Cars.DATA, recents, this, mUseSystemDefault),
                new EmojiGridView(mContext, Electr.DATA, recents, this, mUseSystemDefault),
                new EmojiGridView(mContext, Symbols.DATA, recents, this, mUseSystemDefault)
        ));
        emojisPager.setAdapter(mEmojisAdapter);
        mEmojiTabs = new View[8];

        mEmojiTabs[0] = view.findViewById(R.id.emojis_tab_0_recents);
        mEmojiTabs[1] = view.findViewById(R.id.emojis_tab_1_people);
        mEmojiTabs[2] = view.findViewById(R.id.emojis_tab_2_nature);
        mEmojiTabs[3] = view.findViewById(R.id.emojis_tab_3_food);
        mEmojiTabs[4] = view.findViewById(R.id.emojis_tab_4_sport);
        mEmojiTabs[5] = view.findViewById(R.id.emojis_tab_5_cars);
        mEmojiTabs[6] = view.findViewById(R.id.emojis_tab_6_elec);
        mEmojiTabs[7] = view.findViewById(R.id.emojis_tab_7_sym);
        for (int i = 0; i < mEmojiTabs.length; i++) {
            final int position = i;
            mEmojiTabs[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    emojisPager.setCurrentItem(position);
                }
            });
        }


        emojisPager.setBackgroundColor(backgroundColor);
        tabs.setBackgroundColor(tabsColor);
        for (int x = 0; x < mEmojiTabs.length; x++) {
            ImageButton btn = (ImageButton) mEmojiTabs[x];
            btn.setColorFilter(iconPressedColor);
        }

        ImageButton imgBtn = (ImageButton) view.findViewById(R.id.emojis_backspace);
        imgBtn.setColorFilter(iconPressedColor);
        imgBtn.setBackgroundColor(backgroundColor);


        view.findViewById(R.id.emojis_backspace)
                .setOnTouchListener(new RepeatListener(500, 50, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (onEmojiBackspaceClickedListener != null) {
                            onEmojiBackspaceClickedListener.onEmojiBackspaceClicked(v);
                        }
                    }
                }));

        // get last selected page
        mRecentsManager = EmojiRecentsManager.getInstance(view.getContext());
        int page = mRecentsManager.getRecentPage();
        // last page was recents, check if there are recents to use
        // if none was found, go to page 1
        if (page == 0 && mRecentsManager.size() == 0) {
            page = 1;
        }

        if (page == 0) {
            onPageSelected(page);
        } else {
            emojisPager.setCurrentItem(page, false);
        }
        return view;
    }

    @Override
    public void addRecentEmoji(Context context, Emojicon emojicon) {
        EmojiRecentsGridView fragment = ((EmojisPagerAdapter) emojisPager.getAdapter()).getRecentFragment();
        fragment.addRecentEmoji(context, emojicon);
    }


    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int i) {
        if (mEmojiTabLastSelectedIndex == i) {
            return;
        }
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:

                if (mEmojiTabLastSelectedIndex >= 0 && mEmojiTabLastSelectedIndex < mEmojiTabs.length) {
                    mEmojiTabs[mEmojiTabLastSelectedIndex].setSelected(false);
                }
                mEmojiTabs[i].setSelected(true);
                mEmojiTabLastSelectedIndex = i;
                mRecentsManager.setRecentPage(i);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    public void setColors(final int iconPressedColor, final int tabsColor, final int backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.iconPressedColor = iconPressedColor;
        this.tabsColor = tabsColor;
    }

    private static class EmojisPagerAdapter extends PagerAdapter {
        private List<EmojiGridView> views;

        public EmojiRecentsGridView getRecentFragment() {
            for (EmojiGridView it : views) {
                if (it instanceof EmojiRecentsGridView) {
                    return (EmojiRecentsGridView) it;
                }
            }
            return null;
        }

        public EmojisPagerAdapter(List<EmojiGridView> views) {
            super();
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = views.get(position).rootView;
            ((ViewPager) container).addView(v, 0);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object view) {
            ((ViewPager) container).removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View view, Object key) {
            return key == view;
        }
    }

    public static class RepeatListener implements View.OnTouchListener {

        private Handler handler = new Handler();

        private int initialInterval;
        private final int normalInterval;
        private final OnClickListener clickListener;

        private Runnable handlerRunnable = new Runnable() {
            @Override
            public void run() {
                if (downView == null) {
                    return;
                }
                handler.removeCallbacksAndMessages(downView);
                handler.postAtTime(this, downView, SystemClock.uptimeMillis() + normalInterval);
                clickListener.onClick(downView);
            }
        };

        private View downView;

        public RepeatListener(int initialInterval, int normalInterval, OnClickListener clickListener) {
            if (clickListener == null) {
                throw new IllegalArgumentException("null runnable");
            }
            if (initialInterval < 0 || normalInterval < 0) {
                throw new IllegalArgumentException("negative interval");
            }

            this.initialInterval = initialInterval;
            this.normalInterval = normalInterval;
            this.clickListener = clickListener;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downView = view;
                    handler.removeCallbacks(handlerRunnable);
                    handler.postAtTime(handlerRunnable, downView, SystemClock.uptimeMillis() + initialInterval);
                    clickListener.onClick(view);
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                    handler.removeCallbacksAndMessages(downView);
                    downView = null;
                    return true;
            }
            return false;
        }
    }

    public interface OnEmojiBackspaceClickedListener {
        void onEmojiBackspaceClicked(View v);
    }

    public interface OnSoftKeyboardOpenCloseListener {
        void onKeyboardOpen(int keyBoardHeight);

        void onKeyboardClose();
    }


}