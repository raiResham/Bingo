package cs193a.stanford.edu.bingo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends Activity {

    private Typeface face;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        start();
    }

    private void start(){
        // Create typeface object
        face = Typeface.createFromAsset(getAssets(), "fonts/SLANT.ttf");

        TextView heading = (TextView)findViewById(R.id.headerId);
        Button play = (Button)findViewById(R.id.playId);
        Button about = (Button)findViewById(R.id.aboutId);

        // Set typeface
        heading.setTypeface(face);
        play.setTypeface(face);

        about.setTypeface(face);

        heading.getPaint().setShader(new LinearGradient(0,0,0,heading.getLineHeight(), Color.parseColor("#145A32"), Color.parseColor("#145A32"), Shader.TileMode.REPEAT));

    }

    public void playClicked(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void muteClicked(View view) {
    }

    public void aboutClicked(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
}
