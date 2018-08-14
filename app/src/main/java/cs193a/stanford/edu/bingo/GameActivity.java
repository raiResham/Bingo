package cs193a.stanford.edu.bingo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameActivity extends Activity {

    private Button cells[][] = new Button[7][6];
    private Boolean cellClicked[][] = new Boolean[7][6];
    private int board[][] = new int[7][6];
    private Typeface face;
    private Boolean gameOver = false;
    private final int WON = 10000;
    private final int CONTINUE = -1;
    private int bars = 0;
    // private int bars = 0; to see magic/bug
    private String message = "BINGO";
    private int currentBINGOIndex = 0;
    private Button cellMsg[] = new Button[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        init();
    }

    private void init(){
        // Create typeface object
        face = Typeface.createFromAsset(getAssets(), "fonts/SLANT.ttf");

        // Get reset button
        Button resetBtn = (Button)findViewById(R.id.resetBtn);
        resetBtn.setTypeface(face);

        // Create cells
        for(int i = 0; i < 7 ; i++)
        {
            for(int j = 0; j < 6; j++)
            {
                Button cell = new Button(this);
                cell.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ((ViewGroup) findViewById(R.id.grid)).addView(cell);
                // Add cell to imageView
                cells[i][j] = cell;
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cellClicked(v);
                    }
                });
                cell.setBackgroundResource(R.drawable.circle);
                cell.setTextColor(Color.parseColor("#ffffff"));
                cell.setTag(String.valueOf("("+i+", "+j+")"));
                cell.setTypeface(face);
                cell.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 31);
                // Setting cell size and margin in gridlayout
                float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50 , getResources().getDisplayMetrics());
                cell.getLayoutParams().width = (int)px;
                cell.getLayoutParams().height = (int)px;
                GridLayout.LayoutParams params = (GridLayout.LayoutParams) cell.getLayoutParams();
                params.setMargins(5, 5, 5, 5); // left, top, right, bottom
            }
        }
        assignNumbers();

    }

    // Assigns number to board cell in random
    // Initialize value of boolean cell clicked variable
    private void assignNumbers(){
        shuffleBoard();
        putNumberInCells();
        putBINGOOnTop();
        resetBoundaryValues();
    }

    private void resetValues(){
        bars = 0;
        currentBINGOIndex = 0;
        gameOver = false;
    }

    private void shuffleBoard(){
        List<Integer> temp = new ArrayList<Integer>();
        int count = 1;
        for(int i = 1; i <= 25; i++)
            temp.add(count++);

        // Shuffle the board
        Collections.shuffle(temp);

        // Fill board
        int index = 0;
        for(int  i = 1; i <= 5; i++){
            for(int j = 0; j < 5; j++){
                board[i][j] = temp.get(index++);
            }
        }
    }

    private void putNumberInCells(){
        // Fill number in cells
        int index = 0;
        for(int  i = 1; i <= 5; i++){
            for(int j = 0; j < 5; j++){
                Button cell = cells[i][j];
                cell.setEnabled(true);
                cell.setText(String.valueOf(board[i][j]));
                cell.setBackgroundResource(R.drawable.circle);
             //   cell.getPaint().setShader(new LinearGradient(0,0,0,Color.parseColor("#ff758c"),  Color.parseColor("#ff7eb3"),  Color.parseColor("#96e6a1"), Shader.TileMode.CLAMP));

                cell.setEnabled(true);
                // cellClicked[i][j] = false;
            }
        }


        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 6; j++){
                cellClicked[i][j] = false;
            }
        }

    }

    private void putBINGOOnTop(){
        // Put BINGO cells on top
        for(int i = 0; i < 5; i++){
            Button cell = new Button(this);
            cell.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ((ViewGroup) findViewById(R.id.gridMsg)).addView(cell);
            cell.setTypeface(face);
            cell.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
            GridLayout.LayoutParams params = (GridLayout.LayoutParams) cell.getLayoutParams();
            params.setMargins(5, 5, 5, 5); // left, top, right, bottom
            cell.setText(String.valueOf(message.charAt(i)));
            cell.setBackgroundResource(R.drawable.circlepressed);
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60 , getResources().getDisplayMetrics());
            cell.getLayoutParams().width = (int)px;
            cell.getLayoutParams().height = (int)px;
            // Get reference to show cell later
            cellMsg[i] = cell;
            // Hide cell visibility

        }
        hideBINGOVisibility();

    }

    private void hideBINGOVisibility(){
        for(int i = 0; i < 5; i++)
            cellMsg[i].setVisibility(View.INVISIBLE);
    }

    private void cellClicked(View v){
        if(v.isEnabled() && !gameOver){

            // Disable cell
            v.setEnabled(false);

           //   ((Button)v).getPaint().setShader(new LinearGradient(0,0,0,((Button)v).getLineHeight(),  Color.parseColor("#48c6f1"),  Color.parseColor("#6f86d6"), Shader.TileMode.CLAMP));
            //((Button)v).getPaint().setShader(new LinearGradient(0,0,0,((Button)v).getLineHeight(),  Color.parseColor("#000000"),  Color.parseColor("#000000"), Shader.TileMode.CLAMP));


            // Get tag of view, v.
            String tag = (String)v.getTag();

            // get i and j index of cell clicked
            int i  = Character.getNumericValue(tag.charAt(1));
            int j  = Character.getNumericValue(tag.charAt(4));

            // Register the click
            cellClicked[i][j] = true;

            // Change button color
            changeCellColor(v);

            // Check for winning conditions
            int result = evaluate(i, j);

            if(result == WON){
                gameOver = true;
                // Animate
                animate();
            }
        }else{
            Toast.makeText(this, "help",Toast.LENGTH_LONG);
        }
    }

    private  void animate(){
        ArrayList<Animator> anim_list = new ArrayList<Animator>();
        for(int i = 0 ; i < 5; i++){
            ObjectAnimator animX = ObjectAnimator.ofFloat(cellMsg[i], "scaleX", .5f);
            animX.setRepeatCount(7);
            animX.setRepeatMode(ObjectAnimator.REVERSE);
            anim_list.add(animX);
            ObjectAnimator animY = ObjectAnimator.ofFloat(cellMsg[i], "scaleY", .5f);
            animY.setRepeatCount(7);
            animY.setRepeatMode(ObjectAnimator.REVERSE);
            anim_list.add(animY);
        }
        // Animate BINGO letters
        AnimatorSet anim = new AnimatorSet();
        anim.setDuration(700);
        anim.playTogether(anim_list);
        anim.start();
    }

    // Called when cell is pressed.
    private void changeCellColor(View v){
        Button cell = (Button)v;
        cell.setBackgroundResource(R.drawable.circlepressed);
    }

    private int evaluate(int iIndex, int jIndex){

        int tempBars = 0;
        int count = 0;
        // Check vertical matches
            // Count upwards
            // Keep col fixed
            for(int i = iIndex; i > 0; i--){
                if(cellClicked[i][jIndex])  count++;
                else
                    break;
            }

            // Count downwards
            for(int i = iIndex + 1; i <= 5; i++ ){
                if(cellClicked[i][jIndex]) count++;
                else
                    break;
            }

            // Check to see if count is 5
            if(count == 5){
                // Increase bar count by one
                tempBars++;
                showMessage(iIndex, jIndex, "vertical");
            }

            // Reset value of count
            count = 0;

        // Check horizontal matches
            // Count left
            // Keep rows fixed
            for(int j = jIndex; j >=0; j--){
                if(cellClicked[iIndex][j]) count++;
                else
                    break;
            }

            // Count right
            for(int j = jIndex + 1; j < 5; j++){
                if(cellClicked[iIndex][j]) count++;
                else
                    break;
            }
            if(count == 5){
                tempBars++;
                showMessage(iIndex, jIndex, "horizontal");
            }


        // Check diagonal matches
            // reset value of count
            count = 0;
            if(iIndex == (jIndex+1)){
                if(cellClicked[1][0]) count++;
                if(cellClicked[2][1]) count++;
                if(cellClicked[3][2]) count++;
                if(cellClicked[4][3]) count++;
                if(cellClicked[5][4]) count++;

            }
            // See if count is 5
            if(count==5){
                tempBars ++;
                showMessage(iIndex, jIndex, "diagonal1");// diagonal 1 = \
            }


            // reset value of count
            count = 0;
            if((iIndex + jIndex) == 5){
                if(cellClicked[1][4]) count++;
                if(cellClicked[2][3]) count++;
                if(cellClicked[3][2]) count++;
                if(cellClicked[4][1]) count++;
                if(cellClicked[5][0]) count++;
            }
            // See if count is 5
            if(count == 5){
                tempBars++;
                showMessage(iIndex, jIndex, "diagonal2"); // diagonal 2 = /
            }

        bars = bars + tempBars;

        if(bars >= 5){
            return WON;
        }

        else
            return CONTINUE;
    }

    // Shows B I N G O !
    private void showMessage(int i, int j, String barOrientation){

        if(currentBINGOIndex < message.length()){
            if(barOrientation.equals("vertical")){
               if(i <= 3){
                   // put on top
                   cells[0][j].setText(String.valueOf(message.charAt(currentBINGOIndex)));
                   cells[0][j].setVisibility(View.VISIBLE);

               }else{
                   // put on bottom
                   cells[6][j].setText(String.valueOf(message.charAt(currentBINGOIndex)));
                   cells[6][j].setVisibility(View.VISIBLE);
               }
            }
            if(barOrientation.equals("horizontal")){
                // no option but to place in the right
                cells[i][5].setText(String.valueOf(message.charAt(currentBINGOIndex)));
                cells[i][5].setVisibility(View.VISIBLE);
            }
            if(barOrientation.equals("diagonal1")){
                cells[6][5].setText(String.valueOf(message.charAt(currentBINGOIndex)));
                cells[6][5].setVisibility(View.VISIBLE);
            }
            if(barOrientation.equals("diagonal2")){
                cells[0][5].setText(String.valueOf(message.charAt(currentBINGOIndex)));
                cells[0][5].setVisibility(View.VISIBLE);
            }
            cellMsg[currentBINGOIndex++].setVisibility(View.VISIBLE);
        }
    }

    private void playClickSound(){

    }

    public void resetClicked(View view) {
        resetValues();
        shuffleBoard();
        putNumberInCells();
        hideBINGOVisibility();
        resetBoundaryValues();
    }

    public void resetBoundaryValues(){

        for(int i = 0; i < 6; i++){
            cells[0][i].setText("");
            cells[0][i].setVisibility(View.INVISIBLE);
            cells[0][i].setEnabled(false);

            cells[6][i].setText("");
            cells[6][i].setVisibility(View.INVISIBLE);
            cells[6][i].setEnabled(false);
        }

        for(int i = 0; i < 7; i++){
            cells[i][5].setText("");
            cells[i][5].setVisibility(View.INVISIBLE);
            cells[i][5].setEnabled(false);
        }

    }
}
