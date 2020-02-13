package com.tehmou.book.androidwithlatestfromexercise;

import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Observable<String> titleObservable =
                RxTextView
                        .textChanges((TextView) findViewById(R.id.title_edit_text))
                        .map(Object::toString);

        final Observable<String> messageObservable =
                RxTextView
                        .textChanges((TextView) findViewById(R.id.message_edit_text))
                        .map(Object::toString);

        final Observable<Object> clickEvents =
                RxView.clicks(findViewById(R.id.action_button));

        // Create an ad-hoc structure that contains all information needed for the dialog. This
        // *could* be a custom data structure, but we can get away with a Pair in this simple case.
        final Observable<Pair<String, String>> dialogInformationObservable =
                Observable.combineLatest(titleObservable, messageObservable, Pair::new);

        // Convert the Void clicks into the necessary information to show the dialog
        final Observable<Pair<String, String>> showDialogEventObservable =
                clickEvents.withLatestFrom(dialogInformationObservable,
                        (ignore, dialogInformation) -> dialogInformation);

        // Subscribe to the event Observable that sends us the necessary information to show the
        // dialog at the time when it should be shown.
        showDialogEventObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dialogInformation ->
                        new AlertDialog.Builder(this)
                                .setTitle(dialogInformation.first)
                                .setMessage(dialogInformation.second)
                                .show());
    }
}
