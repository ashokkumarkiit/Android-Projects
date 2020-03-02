package iitmad.com.temperatureconverter;

import android.content.Context;
import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    RadioButton radioFahrenheit, radioCelsius;
    RadioGroup rGTemperature;
    Context context;
    Button btnbConvert;
    EditText etValueToBeConverted;
    TextView tvConvertedValue, tvConvertorSign, tvHistory;
    StringBuilder sbHistory;
    private static final String TAG = "HomeActivity";
    ScrollView svPage;
    String historyValue = "";
    ConstraintLayout rlConversionHistory;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = getApplicationContext();

        etValueToBeConverted = (EditText) findViewById(R.id.etValueToBeConverted);
        tvConvertedValue = (TextView) findViewById(R.id.tvConvertedValue);
        tvConvertorSign = (TextView) findViewById(R.id.tvConvertorSign);
        tvHistory = (TextView) findViewById(R.id.tvHistory);
        tvHistory.setMovementMethod(new ScrollingMovementMethod());
        sbHistory = new StringBuilder();
        svPage = (ScrollView) findViewById(R.id.svPage);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        rlConversionHistory = (ConstraintLayout) findViewById(R.id.rlConversionHistory);
        if (historyValue.equals("") || historyValue.trim().length() <= 0)
            rlConversionHistory.setVisibility(View.GONE);
        else
            rlConversionHistory.setVisibility(View.VISIBLE);

        btnbConvert = (Button) findViewById(R.id.btnbConvert);
        rGTemperature = (RadioGroup) findViewById(R.id.rGTemperature);
        rGTemperature.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioCelsius:
                        Toast.makeText(context, R.string.strCtoF, Toast.LENGTH_SHORT).show();
                        tvConvertorSign.setText(R.string.strC);
                        tvConvertedValue.setText(R.string.strLblConValue);
                        break;
                    case R.id.radioFahrenheit:
                        Toast.makeText(context, R.string.strFtoC, Toast.LENGTH_SHORT).show();
                        tvConvertorSign.setText(R.string.strF);
                        tvConvertedValue.setText(R.string.strLblConValue);
                        break;
                }

            }
        });

        btnbConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Hiding the Soft keyboard
                try {
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus() == null ? new View(context).getWindowToken() : getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    // Log.d(TAG, "onClick: " + historyValue);

                    double convertedValue = 0.0;
                    if (rGTemperature.getCheckedRadioButtonId() == -1) {
                        // no radio buttons are checked
                        Toast.makeText(context, R.string.strSelectConversion, Toast.LENGTH_SHORT).show();
                    } else {
                        if (etValueToBeConverted.getText().toString().trim().equals("") || etValueToBeConverted.getText() == null) {
                            Toast.makeText(context, R.string.strNoValue, Toast.LENGTH_SHORT).show();
                        } else {
                            if (rGTemperature.getCheckedRadioButtonId() == R.id.radioCelsius) {
                                // C to F: (C * 1.8) + 32 Example (15C): (15.0 * 1.8) + 32.0 = 59F
                                convertedValue = (Double.parseDouble(etValueToBeConverted.getText().toString()) * 1.8) + 32;

                                tvConvertedValue.setText(getString(R.string.strFValue, convertedValue));

                                sbHistory.insert(0, etValueToBeConverted.getText().toString() + " " + tvConvertorSign.getText().toString() + " --> " + tvConvertedValue.getText().toString() + "\n");

                            } else if (rGTemperature.getCheckedRadioButtonId() == R.id.radioFahrenheit) {
                                // F to C: (F – 32.0) / 1.8 Example (75F): (75.0 – 32.0) / 1.8 = 23.9C
                                convertedValue = (Double.parseDouble(etValueToBeConverted.getText().toString()) - 32.0) / 1.8;

                                tvConvertedValue.setText(getString(R.string.strCValue, convertedValue));


                                sbHistory.insert(0, etValueToBeConverted.getText().toString() + " " + tvConvertorSign.getText().toString() + " --> " + tvConvertedValue.getText().toString() + "\n");
                            }
                            rlConversionHistory.setVisibility(View.VISIBLE);
                            tvHistory.setText(sbHistory.toString() + historyValue);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        svPage.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                tvHistory.getParent().requestDisallowInterceptTouchEvent(false);

                return false;
            }
        });

        tvHistory.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                tvHistory.getParent().requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (historyValue.equals("") || historyValue.trim().length() <= 0)
                rlConversionHistory.setVisibility(View.GONE);
            else
                rlConversionHistory.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try {
            if (tvConvertedValue.getText() != null && tvConvertedValue.length() > 0) {
                outState.putString("ConvertedValue", tvConvertedValue.getText().toString());
            }
            if (etValueToBeConverted.getText() != null && (etValueToBeConverted.length() > 0)) {
                outState.putString("InputValue", "" + etValueToBeConverted.getText());
            } else {
                outState.putString("InputValue", "");
            }

            if (tvHistory.getText() != null && tvHistory.length() > 0) {
                outState.putString("History", "" + tvHistory.getText().toString());
            } else {
                outState.putString("History", "" + "");
            }
            super.onSaveInstanceState(outState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        try {
            super.onRestoreInstanceState(savedInstanceState);
            tvConvertedValue.setText(savedInstanceState.getString("ConvertedValue"));
            etValueToBeConverted.setText(String.valueOf(savedInstanceState.getString("InputValue")));
            tvHistory.setText(String.valueOf(savedInstanceState.getString("History")));
            historyValue = String.valueOf(savedInstanceState.getString("History"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        try {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (v instanceof EditText) {
                    Rect outRect = new Rect();
                    v.getGlobalVisibleRect(outRect);
                    if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                        v.clearFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return super.dispatchTouchEvent(event);
    }


}
