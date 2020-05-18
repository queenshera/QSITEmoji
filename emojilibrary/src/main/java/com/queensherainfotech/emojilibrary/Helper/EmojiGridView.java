package com.queensherainfotech.emojilibrary.Helper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import java.util.Arrays;
import com.queensherainfotech.emojilibrary.R;
import com.queensherainfotech.emojilibrary.Emoji.Emojicon;
import com.queensherainfotech.emojilibrary.Emoji.People;

public class EmojiGridView {
    public View rootView;
    EmojiPopup mEmojiconPopup;
    EmojiRecents mRecents;
    Emojicon[] mData;
    private boolean mUseSystemDefault = false;


    public EmojiGridView(Context context, Emojicon[] emojicons, EmojiRecents recents, EmojiPopup emojiconPopup, boolean useSystemDefault) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        mEmojiconPopup = emojiconPopup;
        rootView = inflater.inflate(R.layout.emoji_grid, null);
        setRecents(recents);
        GridView gridView = (GridView) rootView.findViewById(R.id.Emoji_GridView);
        if (emojicons== null) {
            mData = People.DATA;
        } else {
            Object[] o = (Object[]) emojicons;
            mData = Arrays.asList(o).toArray(new Emojicon[o.length]);
        }
        EmojiAdapter mAdapter = new EmojiAdapter(rootView.getContext(), mData ,useSystemDefault);
        mAdapter.setEmojiClickListener(new OnEmojiconClickedListener() {

            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                if (mEmojiconPopup.onEmojiconClickedListener != null) {
                    mEmojiconPopup.onEmojiconClickedListener.onEmojiconClicked(emojicon);
                }
                if (mRecents != null) {
                    mRecents.addRecentEmoji(rootView.getContext(), emojicon);
                }
            }
        });
        gridView.setAdapter(mAdapter);
    }

    private void setRecents(EmojiRecents recents) {
        mRecents = recents;
    }

    public interface OnEmojiconClickedListener {
        void onEmojiconClicked(Emojicon emojicon);
    }
}