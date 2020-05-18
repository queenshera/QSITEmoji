package com.queensherainfotech.emojilibrary.Helper;

import android.content.Context;
import android.widget.GridView;
import com.queensherainfotech.emojilibrary.R;
import com.queensherainfotech.emojilibrary.Emoji.Emojicon;

public class EmojiRecentsGridView extends EmojiGridView implements EmojiRecents {
    EmojiAdapter mAdapter;
    private boolean mUseSystemDefault = false;

    public EmojiRecentsGridView(Context context, Emojicon[] emojicons,
                                EmojiRecents recents, EmojiPopup emojiconsPopup, boolean useSystemDefault) {
        super(context, emojicons, recents, emojiconsPopup,useSystemDefault);
        this.mUseSystemDefault=useSystemDefault;
        EmojiRecentsManager recents1 = EmojiRecentsManager
                .getInstance(rootView.getContext());
        mAdapter = new EmojiAdapter(rootView.getContext(),  recents1,mUseSystemDefault);
        mAdapter.setEmojiClickListener(new OnEmojiconClickedListener() {

            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                if (mEmojiconPopup.onEmojiconClickedListener != null) {
                    mEmojiconPopup.onEmojiconClickedListener.onEmojiconClicked(emojicon);
                }
            }
        });
        GridView gridView = (GridView) rootView.findViewById(R.id.Emoji_GridView);
        gridView.setAdapter(mAdapter);
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    @Override
    public void addRecentEmoji(Context context, Emojicon emojicon) {
        EmojiRecentsManager recents = EmojiRecentsManager
                .getInstance(context);
        recents.push(emojicon);

        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

}