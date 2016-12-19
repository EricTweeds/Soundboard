package tweedle.eric.soundboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import static tweedle.eric.soundboard.R.styleable.View;

public class MainActivity extends AppCompatActivity{

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        ImageButton btn1 = (ImageButton) findViewById(R.id.imageButton1);
        ImageButton btn2 = (ImageButton) findViewById(R.id.imageButton2);
        ImageButton btn3 = (ImageButton) findViewById(R.id.imageButton3);
        ImageButton btn4 = (ImageButton) findViewById(R.id.imageButton4);
        ImageButton btn5 = (ImageButton) findViewById(R.id.imageButton5);
        ImageButton btn6 = (ImageButton) findViewById(R.id.imageButton6);
        ImageButton[] arr ={btn1, btn2, btn3, btn4, btn5, btn6};
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        for (int i = 0; i < arr.length; i++) {
            String UriString = sharedPref.getString(Integer.toString(i), "na");
            if (!(UriString.equals("na"))){
                arr[i].setImageURI(Uri.parse(UriString));
            }
        }
        for (int i = 0; i < sounds.length; i++) {
            String UriString = sharedPref.getString(Integer.toString(i+10), "na");
            if (!(UriString.equals("na"))){
                sounds[i] =MediaPlayer.create(this, (Uri.parse(UriString)));
                alreadySet[i] = true;

            }
        }
    }
    int currentButton = 0;
    boolean[] alreadySet = {false, false, false, false, false, false};
    MediaPlayer[] sounds = new MediaPlayer[6];
    boolean playing = false;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void toggleSelector(boolean showSelector){
        RelativeLayout selector = (RelativeLayout) findViewById(R.id.selector);
        if (showSelector) {
            selector.setVisibility(android.view.View.VISIBLE);
        }
        else{
            selector.setVisibility(android.view.View.GONE);
        }
    }
    public void Go(View view){
        TextView mtext = (TextView) findViewById(R.id.selectorText);
        switch (view.getId()) {
            case R.id.imageButton1:
                mtext.setText("Edit Sound 1");
                currentButton = 0;
                break;
            case R.id.imageButton2:
                mtext.setText("Edit Sound 2");
                currentButton = 1;
                break;
            case R.id.imageButton3:
                mtext.setText("Edit Sound 3");
                currentButton = 2;
                break;
            case R.id.imageButton4:
                mtext.setText("Edit Sound 4");
                currentButton = 3;
                break;
            case R.id.imageButton5:
                mtext.setText("Edit Sound 5");
                currentButton = 4;
                break;
            case R.id.imageButton6:
                mtext.setText("Edit Sound 6");
                currentButton = 5;
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }
        if (playing){
            sounds[currentButton].stop();
            playing = false;
        }
        else if (alreadySet[currentButton]){
            sounds[currentButton].start();
            playing = true;
        }
        else{
            toggleSelector(true);
        }

    }
    public void onBackPressed(){
        toggleSelector(false);
    }
    public void done(View view) { toggleSelector(false);}
    String currentType;
    private static final int READ_REQUEST_CODE = 42;

    public void performFileSearch(View view) {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("image/*");
        currentType = "image";

        startActivityForResult(intent, READ_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            if (resultData != null) {
                ImageButton btn1 = (ImageButton) findViewById(R.id.imageButton1);
                ImageButton btn2 = (ImageButton) findViewById(R.id.imageButton2);
                ImageButton btn3 = (ImageButton) findViewById(R.id.imageButton3);
                ImageButton btn4 = (ImageButton) findViewById(R.id.imageButton4);
                ImageButton btn5 = (ImageButton) findViewById(R.id.imageButton5);
                ImageButton btn6 = (ImageButton) findViewById(R.id.imageButton6);
                ImageButton[] arr ={btn1, btn2, btn3, btn4, btn5, btn6};
                if (currentType.equals("image")) {
                    arr[currentButton].setImageURI(resultData.getData());
                    editor.putString(Integer.toString(currentButton), resultData.getData().toString());
                    editor.apply();

                }
                else if (currentType.equals("audio")){
                    sounds[currentButton] = MediaPlayer.create(this, resultData.getData());
                    editor.putString(Integer.toString(currentButton+10), resultData.getData().toString());
                    editor.apply();
                    alreadySet[currentButton] = true;
                }
            }
        }
    }

    public void performFileSearchSound(View view) {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("audio/*");
        currentType = "audio";

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

}
