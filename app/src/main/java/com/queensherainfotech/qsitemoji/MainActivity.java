package com.queensherainfotech.qsitemoji;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.queensherainfotech.emojilibrary.Action.EmojiActions;
import com.queensherainfotech.emojilibrary.EmojiEditText;
import com.queensherainfotech.emojilibrary.EmojiTextView;

public class MainActivity extends AppCompatActivity {

    EmojiEditText emojiEditText;
    EmojiTextView textView;
    ImageView emojiButton;
    ImageView submitButton;
    View rootView;
    EmojiActions emojiActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootView = findViewById(R.id.rootView);
        emojiButton = findViewById(R.id.btnEmoji);
        submitButton = findViewById(R.id.btnSubmit);
        emojiEditText = findViewById(R.id.emojiEditText);
        textView = findViewById(R.id.textView);

        emojiActions = new EmojiActions(MainActivity.this,rootView,emojiEditText,emojiButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = emojiEditText.getText().toString();
                textView.setText(newText);
            }
        });
    }
}
