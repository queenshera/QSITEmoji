package com.queensherainfotech.emojilibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.style.DynamicDrawableSpan;
import android.util.AttributeSet;
import android.widget.EditText;

import com.queensherainfotech.emojilibrary.Helper.EmojiHandler;
import com.queensherainfotech.emojilibrary.R;

@SuppressLint("AppCompatCustomView")
public class EmojiEditText extends EditText {
    private int mEmojiconSize;
    private int mEmojiconAlignment;
    private int mEmojiconTextSize;
    private boolean mUseSystemDefault = false;

    public EmojiEditText(Context context) {
        super(context);
        mEmojiconSize = (int) getTextSize();
        mEmojiconTextSize = (int) getTextSize();
    }

    public EmojiEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EmojiEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Emojicon);
        mEmojiconSize = (int) a.getDimension(R.styleable.Emojicon_emojiconSize, getTextSize());
        mEmojiconAlignment = a.getInt(R.styleable.Emojicon_emojiconAlignment, DynamicDrawableSpan.ALIGN_BASELINE);
        mUseSystemDefault = a.getBoolean(R.styleable.Emojicon_emojiconUseSystemDefault, false);
        a.recycle();
        mEmojiconTextSize = (int) getTextSize();
        setText(getText());
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        updateText();
    }

    public void setEmojiconSize(int pixels) {
        mEmojiconSize = pixels;

        updateText();
    }

    private void updateText() {
        EmojiHandler.addEmojis(getContext(), getText(), mEmojiconSize, mEmojiconAlignment, mEmojiconTextSize, mUseSystemDefault);
    }

    public void setUseSystemDefault(boolean useSystemDefault) {
        mUseSystemDefault = useSystemDefault;
    }
}
