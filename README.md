# QSITEmoji
Add emojis to android app using this library and all features like whatsapp and telegram.

[![](https://jitpack.io/v/queensherainfotech/QSITEmoji.svg)](https://jitpack.io/#queensherainfotech/QSITEmoji)

Project level gradle
------
```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```


App level gradle
------
```
dependencies {
    ...
    implementation 'com.queensherainfotech:QSITEmoji:1.0.0'
}
```

XML Usage
-----
[activity_main.xml](https://github.com/queensherainfotech/QSITEmoji/blob/master/app/src/main/res/layout/activity_main.xml)
```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/rootView">

    <com.queensherainfotech.emojilibrary.EmojiTextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:layout_marginTop="26sp"
        android:text="Enter text below"/> <!-- You can use simple TextView also, but if you want to enable more customization, then here is the option for you -->

    <ImageView
        android:id="@+id/btnEmoji"
        android:layout_width="40sp"
        android:layout_height="40sp"
        android:layout_alignParentBottom="true"
        android:layout_alignParentStart="true"
        android:padding="3sp"
        android:src="@mipmap/ic_smiley" /> <!-- Use this ImageView to open Emoji keyboard. You can take any other widget if required. -->

    <ImageView
        android:id="@+id/btnSubmit"
        android:layout_width="40sp"
        android:layout_height="40sp"
        android:layout_alignParentBottom="true"
        android:layout_alignParentEnd="true"
        android:padding="3sp"
        android:src="@android:drawable/ic_menu_send" /> <!-- Use this ImageView to submit the text. You can take any other widget if required. -->

    <com.queensherainfotech.emojilibrary.EmojiEditText
        android:id="@+id/emojiEditText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_toStartOf="@id/btnSubmit"
        android:layout_toEndOf="@id/btnEmoji"
        android:inputType="textMultiLine"
        android:imeOptions="actionNext"/> <!-- Use this widget to input custom designed emoji inputs to your app. -->
</RelativeLayout>
```

Java Usage
-----
[MainActivity.java](https://github.com/queensherainfotech/QSITEmoji/blob/master/app/src/main/java/com/queensherainfotech/qsitemoji/MainActivity.java)
```
import com.queensherainfotech.emojilibrary.Action.EmojiActions;  // Used to open custom designed emojis keyboard
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
```
