package com.queensherainfotech.emojilibrary.Action;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.queensherainfotech.emojilibrary.Emoji.Emojicon;
import com.queensherainfotech.emojilibrary.EmojiEditText;
import com.queensherainfotech.emojilibrary.Helper.EmojiGridView;
import com.queensherainfotech.emojilibrary.Helper.EmojiPopup;
import com.queensherainfotech.emojilibrary.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmojiActions implements View.OnFocusChangeListener {

    private boolean useSystemEmoji = false;
    private EmojiPopup popup;
    private Context context;
    private View rootView;
    private ImageView emojiButton;
    private int KeyBoardIcon = R.drawable.ic_keyboard_black_24dp;
    private int SmileyIcons = R.mipmap.ic_smiley;
    private KeyboardListener keyboardListener;
    private List<EmojiEditText> emojiEditTextList = new ArrayList<>();
    private EmojiEditText emojiEditText;

    public EmojiActions(Context ctx, View rootView, EmojiEditText emojiEditText, ImageView emojiButton) {
        this.emojiButton = emojiButton;
        this.context = ctx;
        this.rootView = rootView;
        addEmojiEditTextList(emojiEditText);
        this.popup = new EmojiPopup(rootView, ctx, useSystemEmoji);
        initListeners();
    }

    public void addEmojiEditTextList(EmojiEditText... emojiEditText) {
        Collections.addAll(emojiEditTextList, emojiEditText);
        for (EmojiEditText editText : emojiEditText) {
            editText.setOnFocusChangeListener(this);
        }
    }

    public EmojiActions(Context ctx, View rootView, EmojiEditText emojiEditText) {
        addEmojiEditTextList(emojiEditText);
        this.context = ctx;
        this.rootView = rootView;
        this.popup = new EmojiPopup(rootView, ctx, useSystemEmoji);
        initListeners();
    }

    public EmojiPopup getPopup() {
        return popup;
    }

    public void setEmojiButton(ImageView emojiButton) {
        this.emojiButton = emojiButton;
        initEmojiButtonListener();
    }

    public void setColors(int iconPressedColor, int tabsColor, int backgroundColor) {
        this.popup.setColors(iconPressedColor, tabsColor, backgroundColor);
    }

    public void setIconsIds(int keyboardIcon, int smileyIcon) {
        this.KeyBoardIcon = keyboardIcon;
        this.SmileyIcons = smileyIcon;
    }

    public void setUseSystemEmoji(boolean useSystemEmoji) {
        this.useSystemEmoji = useSystemEmoji;
        for (EmojiEditText editText : emojiEditTextList) {
            editText.setUseSystemDefault(useSystemEmoji);
        }
        refresh();
    }


    private void refresh() {
        popup.updateUseSystemDefault(useSystemEmoji);
    }

    public void initListeners() {
        if (emojiEditText == null) {
            emojiEditText = emojiEditTextList.get(0);
        }
        popup.setSizeForSoftKeyboard();

        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                changeEmojiKeyboardIcon(emojiButton, SmileyIcons);
            }
        });

        popup.setOnSoftKeyboardOpenCloseListener(new EmojiPopup.OnSoftKeyboardOpenCloseListener() {
            @Override
            public void onKeyboardOpen(int keyBoardHeight) {
                if (keyboardListener != null) {
                    keyboardListener.onKeyboardOpen();
                }
            }

            @Override
            public void onKeyboardClose() {
                if (keyboardListener != null) {
                    keyboardListener.onKeyboardClose();
                }
                if (popup.isShowing()) {
                    popup.dismiss();
                }
            }
        });

        popup.setOnEmojiconClickedListener(new EmojiGridView.OnEmojiconClickedListener() {
            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                if (emojicon == null) {
                    return;
                }

                int start = emojiEditText.getSelectionStart();
                int end = emojiEditText.getSelectionEnd();
                if (start < 0) {
                    emojiEditText.append(emojicon.getEmoji());
                } else {
                    emojiEditText.getText()
                            .replace(Math.min(start, end),
                                    Math.max(start, end),
                                    emojicon.getEmoji(),
                                    0,
                                    emojicon.getEmoji()
                                            .length());
                }
            }
        });

        popup.setOnEmojiBackspaceClickedListener(new EmojiPopup.OnEmojiBackspaceClickedListener() {
            @Override
            public void onEmojiBackspaceClicked(View v) {
                KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                emojiEditText.dispatchKeyEvent(event);
            }
        });
        initEmojiButtonListener();
    }

    private void initEmojiButtonListener() {
        if (emojiButton != null) {
            emojiButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    togglePopupVisibility();
                }
            });
        }
    }

    private void togglePopupVisibility() {
        if (!popup.isShowing()) {
            showPopup();
        } else {
            hidePopup();
        }
    }

    public void showPopup() {
        if (emojiEditText == null) {
            emojiEditText = emojiEditTextList.get(0);
        }
        if (popup.isKeyBoardOpen()) {
            popup.showAtBottom();
            changeEmojiKeyboardIcon(emojiButton, KeyBoardIcon);
        } else {
            emojiEditText.setFocusableInTouchMode(true);
            emojiEditText.requestFocus();
            final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(emojiEditText, InputMethodManager.SHOW_IMPLICIT);
            popup.showAtBottomPending();
            changeEmojiKeyboardIcon(emojiButton, KeyBoardIcon);
        }
    }

    public void hidePopup() {
        if (popup != null && popup.isShowing()) {
            popup.dismiss();
        }
    }

    private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId) {
        if (iconToBeChanged != null) {
            iconToBeChanged.setImageResource(drawableResourceId);
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            if (view instanceof EmojiEditText) {
                emojiEditText = (EmojiEditText) view;
            }
        }
    }

    public interface KeyboardListener {
        void onKeyboardOpen();
        void onKeyboardClose();
    }

    public void setKeyboardListener(KeyboardListener listener) {
        this.keyboardListener = listener;
    }
}
