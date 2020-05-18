package com.queensherainfotech.emojilibrary.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.queensherainfotech.emojilibrary.Emoji.Emojicon;

public class EmojiRecentsManager extends ArrayList<Emojicon> {

    private static final String PREFERENCE_NAME = "emojicon";
    private static final String PREF_RECENTS = "recent_emojis";
    private static final String PREF_PAGE = "recent_page";
    private static final Object LOCK = new Object();
    private static EmojiRecentsManager sInstance;

    private Context mContext;

    private EmojiRecentsManager(Context context) {
        mContext = context.getApplicationContext();
        loadRecents();
    }

    public static EmojiRecentsManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new EmojiRecentsManager(context);
                }
            }
        }
        return sInstance;
    }

    public int getRecentPage() {
        return getPreferences().getInt(PREF_PAGE, 0);
    }

    public void setRecentPage(int page) {
        getPreferences().edit().putInt(PREF_PAGE, page).commit();
    }

    public void push(Emojicon object) {
        if (contains(object)) {
            super.remove(object);
        }
        add(0, object);
    }

    @Override
    public boolean add(Emojicon object) {
        boolean ret = super.add(object);
        return ret;
    }

    @Override
    public void add(int index, Emojicon object) {
        super.add(index, object);
    }

    @Override
    public boolean remove(Object object) {
        boolean ret = super.remove(object);
        return ret;
    }

    private SharedPreferences getPreferences() {
        return mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    private void loadRecents() {
        SharedPreferences prefs = getPreferences();
        String str = prefs.getString(PREF_RECENTS, "");
        StringTokenizer tokenizer = new StringTokenizer(str, "~");
        while (tokenizer.hasMoreTokens()) {
            try {
                add(new Emojicon(tokenizer.nextToken()));
            }
            catch (NumberFormatException e) {
            }
        }
    }

    public void saveRecents() {
        StringBuilder str = new StringBuilder();
        int c = size();
        for (int i = 0; i < c; i++) {
            Emojicon e = get(i);
            str.append(e.getEmoji());
            if (i < (c - 1)) {
                str.append('~');
            }
        }
        SharedPreferences prefs = getPreferences();
        prefs.edit().putString(PREF_RECENTS, str.toString()).commit();
    }
}
