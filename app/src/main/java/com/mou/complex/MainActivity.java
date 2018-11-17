package com.mou.complex;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private CMathView[] inputMathViews;
    private CMathView[] outputMathViews;
    private CMathView busMathView;
    private Button[] buttons;

    private CoordinatorLayout content;//the entire layout
    private int highlightSelectColor = Color.RED;
    private int highlightCursorColor = Color.GRAY;
    private int selectedElement = 0;
    private String inputString;

    private boolean isInputEdited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AVLoadingIndicatorView indicator = findViewById(R.id.loading_indicator);
        final LinearLayout loadingScreen = findViewById(R.id.loading_screen);
        TextView loadingText = findViewById(R.id.loading_text);
        content = findViewById(R.id.content);
        inputMathViews = new CMathView[]{
                findViewById(R.id.d00),
                findViewById(R.id.d01),
                findViewById(R.id.d02),
                findViewById(R.id.d00_res),
                findViewById(R.id.d10),
                findViewById(R.id.d11),
                findViewById(R.id.d12),
                findViewById(R.id.d01_res),
                findViewById(R.id.d20),
                findViewById(R.id.d21),
                findViewById(R.id.d22),
                findViewById(R.id.d02_res)

        };
        outputMathViews = new CMathView[]{
                findViewById(R.id.d00_sol),
                findViewById(R.id.d01_sol),
                findViewById(R.id.d02_sol)
        };
        buttons = new Button[]{
                findViewById(R.id.btn_0),
                findViewById(R.id.btn_1),
                findViewById(R.id.btn_2),
                findViewById(R.id.btn_3),
                findViewById(R.id.btn_4),
                findViewById(R.id.btn_5),
                findViewById(R.id.btn_6),
                findViewById(R.id.btn_7),
                findViewById(R.id.btn_8),
                findViewById(R.id.btn_9),
                findViewById(R.id.btn_dot),
                findViewById(R.id.btn_del),
                findViewById(R.id.btn_add),
                findViewById(R.id.btn_subtract),
                findViewById(R.id.btn_i),
                findViewById(R.id.btn_ang),
                findViewById(R.id.btn_load),
                findViewById(R.id.btn_clear)


        };
        busMathView = findViewById(R.id.input_mv);
        final Dpad dpad = findViewById(R.id.dpad);

        final View.OnClickListener buttonsListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(outputMathViews[0].isHighlighted()){
                    //clear for all
                    for(CMathView mathView : outputMathViews){
                        mathView.setHighlighted(false, 0);
                    }
                }
                switch (v.getId()) {
                    case R.id.btn_0:
                        inputString = inputString.concat("0");
                        break;
                    case R.id.btn_1:
                        inputString = inputString.concat("1");

                        break;
                    case R.id.btn_2:
                        inputString = inputString.concat("2");

                        break;
                    case R.id.btn_3:
                        inputString = inputString.concat("3");

                        break;
                    case R.id.btn_4:
                        inputString = inputString.concat("4");

                        break;
                    case R.id.btn_5:
                        inputString = inputString.concat("5");

                        break;
                    case R.id.btn_6:
                        inputString = inputString.concat("6");

                        break;
                    case R.id.btn_7:
                        inputString = inputString.concat("7");

                        break;
                    case R.id.btn_8:
                        inputString = inputString.concat("8");

                        break;
                    case R.id.btn_9:
                        inputString = inputString.concat("9");

                        break;
                    case R.id.btn_dot:
                        inputString = inputString.concat(".");

                        break;
                    case R.id.btn_del:
                        if (inputString.endsWith("\\angle")) {
                            inputString = inputString.substring(0, inputString.length() - 6);
                        } else if(inputString.length()>0)inputString = inputString.substring(0, inputString.length() - 1);

                        break;
                    case R.id.btn_add:
                        //TODO next update deals with expression
                        inputString = inputString.concat("+");


                        break;
                    case R.id.btn_subtract:
                        //TODO next update deals with expression
                        inputString = inputString.concat("-");


                        break;
                    case R.id.btn_i:
                        inputString = inputString.concat("i");

                        break;
                    case R.id.btn_ang:
                        inputString = inputString.concat("\\angle");

                        break;
                    case R.id.btn_load:
                        inputString = inputMathViews[selectedElement].getText().replace("$", "");
                        busMathView.setText(inputString);
                        break;
                    case R.id.btn_clear:

                        inputMathViews[selectedElement].setText("");
                        busMathView.setText("");
                        inputString = "";
                        break;

                }
                isInputEdited = true;
                busMathView.setText(inputString);
            }
        };
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        final int screenWidth= displayMetrics.widthPixels;


        dpad.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int dpadWidth = dpad.getWidth();
                final int newButtonDimensions = (screenWidth-dpadWidth)/4;
                for (final Button btn : buttons){
                    Log.d(TAG, "run: screenWidth="+screenWidth+" dpad.getWidth()="+dpad.getWidth());
                    Log.d(TAG, "run: old="+  btn.getWidth()+" new="+newButtonDimensions);
                    btn.setOnClickListener(buttonsListener);
                    btn.post(new Runnable() {
                        @Override
                        public void run() {
                            ViewGroup.LayoutParams params = btn.getLayoutParams();
                            params.width = newButtonDimensions;
                            params.height = newButtonDimensions;
                            btn.setLayoutParams(params);
                        }
                    });
                }
                dpad.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        for (CMathView mv : inputMathViews) {
            mv.setText("0");
        }
        updateSelection();
        dpad.setOnDpadClickListener(new Dpad.OnDpadClickListener() {
            @Override
            public void onClick(int selection) {
                switch (selection) {
                    case Dpad.CENTER:
                        if (isInputEdited){
                            isInputEdited = false;
                            inputMathViews[selectedElement].setText(inputString);
                            selectedElement++;
                            if(selectedElement > 11){
                                //jump to solution
                                selectedElement = 0;
                                break;
                            }
                            updateSelection();

                        }else{
                                //jump to solution
                                selectedElement = 0;
                                updateSelection();
                                solve();

                         }
                     break;
                    case Dpad.DOWN:
                        if (!isInputEdited) {
                            selectedElement += 4;
                            if (selectedElement > 11) selectedElement -= 11;
                            updateSelection();
                        }

                        break;
                    case Dpad.LEFT:
                        if (!isInputEdited) {

                            selectedElement -= 1;
                            if (selectedElement < 0) selectedElement = 11;
                            updateSelection();
                        }

                        break;
                    case Dpad.RIGHT:
                        if (!isInputEdited) {

                            selectedElement += 1;
                            if (selectedElement > 11) selectedElement = 0;
                            updateSelection();
                        }

                        break;
                    case Dpad.UP:
                        if (!isInputEdited) {
                            selectedElement -= 4;
                            if (selectedElement < 0) selectedElement += 11;
                            updateSelection();
                        }

                        break;

                }
            }
        });
        final Handler handler = new Handler();
        final Runnable uiRunnable = new Runnable() {
            @Override
            public void run() {


                boolean isFinished = true;
                for(CMathView mv: inputMathViews){
                    if(!mv.isLoadingFinished()){
                        isFinished =false;
                        break;
                    }
                }
                if(isFinished) for(CMathView mv: outputMathViews){
                    if(!mv.isLoadingFinished()){
                        isFinished =false;
                        break;
                    }
                }
                if(isFinished)loadingScreen.setVisibility(View.GONE);
                else handler.postDelayed(this, 100);
            }
        };
        handler.post(uiRunnable);

    }

    private void solve() {
        ComplexMatrix3x3 matrix= new ComplexMatrix3x3();

        Complex[] resCol = new Complex[3];

        for(int i =0; i<inputMathViews.length; i++){
            Complex c = Complex.parseComplex(inputMathViews[i].getText().replace("$",""));
            if(c == null){
                //error
                selectedElement = i;
                updateSelection();
                inputMathViews[selectedElement].setHighlighted(true, Color.RED);
                makeColoredSnackbar("Check for syntax errors at highlighted index", Color.RED, Snackbar.LENGTH_LONG, Gravity.BOTTOM);

                return;
            }
            if(i ==3|| i==7|| i==11){
                resCol[i%3] = c;

            }else{
                int index = i - i/4;
                matrix.setElement(c, index/3, index%3);
            }
        }
        ComplexMatrix3x3 inverse = matrix.inverse();
        if(inverse==null){
            //no solution
            makeColoredSnackbar("No solution for this system for this system equations!", Color.DKGRAY, Snackbar.LENGTH_LONG, Gravity.TOP);
            return;
        }
        Complex[] solution = inverse.multiplyByColMat(
        Complex.parseComplex(inputMathViews[3].getText().replace("$","")),
        Complex.parseComplex(inputMathViews[7].getText().replace("$","")),
        Complex.parseComplex(inputMathViews[11].getText().replace("$",""))
        );

        for(int i = 0; i<3; i++){
            outputMathViews[i].setText(solution[i].toString());
            outputMathViews[i].setHighlighted(true, Color.GREEN);
        }



    }

    private void updateSelection() {
        //erase all highlights
        for (CMathView mv : inputMathViews) {
            if (mv.isHighlighted()) mv.setHighlighted(false, 0);
        }
        inputMathViews[selectedElement].setHighlighted(true, highlightCursorColor);
        inputString = "";
        busMathView.setText("");


    }
    private void makeColoredSnackbar(String msg, int color, int length, int gravityDir){
        Snackbar snack = Snackbar.make(content, msg, length);
        View view = snack.getView();
        view.setBackgroundColor(color);
        CoordinatorLayout.LayoutParams params =(CoordinatorLayout.LayoutParams)view.getLayoutParams();
        params.gravity = gravityDir;
        view.setLayoutParams(params);
        snack.show();
    }

}
