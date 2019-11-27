package com.tehmou.book.androidlivesearchexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Make the textView1 change text according to whether switchButton is "checked" or not
        final Switch switchButton = (Switch) findViewById(R.id.switch_button);
        final TextView textView1 = (TextView) findViewById(R.id.text_view_1);

        final Disposable switchButtonDisposable = RxCompoundButton.checkedChanges(switchButton)
                .subscribe(checked -> textView1.setText(String.format("Checked: %s", checked)));


        // Set the textView2 to say the text is too long if editText is more than 7 characters
        EditText editText = (EditText) findViewById(R.id.edit_text);
        TextView textView2 = (TextView) findViewById(R.id.text_view_2);

        RxTextView.textChanges(editText)
                .subscribe(text ->
                        textView2.setText(text.length() > 7 ? "Text too long!" : ""));

        final TextView textViewTemp = findViewById(R.id.text_view_12);
        final Switch switchTemp = findViewById(R.id.switch_button2);
        switchTemp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textViewTemp.setText(String.format("Checked %s", isChecked));
            }
        });

    }
}
