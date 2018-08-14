package cs193a.stanford.edu.bingo;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity {

    private Typeface face;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        start();
    }

    private void start(){

        // Create typeface object
        face = Typeface.createFromAsset(getAssets(), "fonts/SLANT.ttf");

        TextView header = (TextView)findViewById(R.id.headerId);
        TextView developer = (TextView)findViewById(R.id.developer);
        TextView developerName = (TextView)findViewById(R.id.developerName);
        TextView font = (TextView)findViewById(R.id.font);
        TextView fontName = (TextView)findViewById(R.id.fontName);


        // Set typeface
        header.setTypeface(face);
        developer.setTypeface(face);
        developerName.setTypeface(face);
        font.setTypeface(face);
        fontName.setTypeface(face);


    }
}
