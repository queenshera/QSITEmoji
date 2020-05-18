package com.queensherainfotech.emojilibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.queensherainfotech.emojilibrary.Helper.EmojiHandler;
import com.queensherainfotech.emojilibrary.R;

@SuppressLint("AppCompatCustomView")
public class EmojiTextView extends TextView {
    private int mEmojiconSize;
    private int mEmojiconAlignment;
    private int mEmojiconTextSize;
    private int mTextStart = 0;
    private int mTextLength = -1;
    private boolean mUseSystemDefault = false;

    public EmojiTextView(Context context) {
        super(context);
        init(null);
    }

    public EmojiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EmojiTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mEmojiconTextSize = (int) getTextSize();
        if (attrs == null) {
            mEmojiconSize = (int) getTextSize();
        }
        else {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Emojicon);
            mEmojiconSize = (int) a.getDimension(R.styleable.Emojicon_emojiconSize, getTextSize());
            mEmojiconAlignment = a.getInt(R.styleable.Emojicon_emojiconAlignment, DynamicDrawableSpan.ALIGN_BOTTOM);
            mTextStart = a.getInteger(R.styleable.Emojicon_emojiconTextStart, 0);
            mTextLength = a.getInteger(R.styleable.Emojicon_emojiconTextLength, -1);
            mUseSystemDefault = a.getBoolean(R.styleable.Emojicon_emojiconUseSystemDefault, mUseSystemDefault);
            a.recycle();
        }
        setText(getText());
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!TextUtils.isEmpty(text)) {
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            EmojiHandler.addEmojis(getContext(), builder, mEmojiconSize, mEmojiconAlignment, mEmojiconTextSize, mTextStart, mTextLength, mUseSystemDefault);
            text = builder;
        }
        super.setText(text, type);
    }

    public void setEmojiconSize(int pixels) {
        mEmojiconSize = pixels;
        super.setText(getText());
    }

    public void setUseSystemDefault(boolean useSystemDefault) {
        mUseSystemDefault = useSystemDefault;
    }
}
