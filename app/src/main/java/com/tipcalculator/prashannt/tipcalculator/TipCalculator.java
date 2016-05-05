package com.tipcalculator.prashannt.tipcalculator;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class TipCalculator extends Activity implements TextView.OnEditorActionListener {

    private Button percentUpButton,percentDownButton;
    private  TextView percentTextView;
    private EditText billAmounteditText;
    private TextView tipTextView;
    private TextView totalTextView;
    private SharedPreferences savedValues;
    private String billAmountString;

    private float tipPercent=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        //code that display the content in full screen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calculator);
        percentTextView=(TextView)findViewById(R.id.tipPercent);
        tipTextView=(TextView)findViewById(R.id.tipAmount);
        totalTextView=(TextView)findViewById(R.id.totalAmount);
        percentUpButton=(Button)findViewById(R.id.ButtonDecrease);
        percentDownButton=(Button)findViewById(R.id.ButtonIncrease);
        billAmounteditText=(EditText)findViewById(R.id.billAmaountEditText);

        savedValues=getSharedPreferences("Saved Values",MODE_PRIVATE);
        billAmounteditText.setOnEditorActionListener(new TextView.OnEditorActionListener(){

            @Override
            //this code is used to make enter key work in the soft keyword
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    calculateAndDisplay();

                    return true;
                }
                return false;
            }
        });
        //click event generator for increase and decrease button.
        percentDownButton.setOnClickListener(   new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipPercent=tipPercent + 0.01f;

                calculateAndDisplay();
            }
        });
        percentUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipPercent=tipPercent-0.01f;
                calculateAndDisplay();
            }
        });
        calculateAndDisplay();

    }

    @Override
    public void onPause(){
        SharedPreferences.Editor editor=savedValues.edit();
        editor.putString("billAmountString", billAmountString);
        editor.putFloat("tipPercent", tipPercent);
        editor.commit();
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        billAmountString=savedValues.getString("billAmountString","");
        tipPercent=savedValues.getFloat("tipPercent",0.01f);
    }

    @Override
    public boolean onEditorAction(TextView v,int actionId,KeyEvent event){
        calculateAndDisplay();
        return false;
    }
    public void calculateAndDisplay(){
        String billAmountString=billAmounteditText.getText().toString();
        float billAmount;
        if(billAmountString.equals("")){
            billAmount=0;
        }
        else{
            billAmount=Float.parseFloat(billAmountString);
        }
        float tipAmount= billAmount*tipPercent;
        float totalAmount= billAmount+tipAmount;
        NumberFormat currency= NumberFormat.getCurrencyInstance();
        tipTextView.setText(currency.format(tipAmount));
        totalTextView.setText(currency.format(totalAmount));
        NumberFormat percent=NumberFormat.getPercentInstance();
        percentTextView.setText(percent.format(tipPercent));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tip_calculator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
