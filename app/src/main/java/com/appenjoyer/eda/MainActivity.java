package com.appenjoyer.eda;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static DecimalFormat df2 = new DecimalFormat("0.##");
    //Exams + Percentatge practiques
    static EditText etPe1, etPe2, etPctAs;
    //Qualificació de teoria
    static TextView tvTeo;
    //Pràctiques
    static EditText etPr_1, etPr_2, etPr_3, etPr_4;
    //Qualificació de pràctiques
    static TextView tvPr;
    //Qualificació ponderodada teoria + pràctiques
    static TextView tvQPT;
    //Percentatge d'assistència
    static EditText PctAs;
    //ImageView %
    static ImageView ivAs;
    //Get reference from Activity
    Activity mActivity;

    //ArrayList to save the day and the % of assist
    ArrayList<Sessio> AlSessio = new ArrayList<Sessio>();

    //SharedPreferences to save the state of CheckBox (PctAs)
    SharedPreferences mPrefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setLogo(R.drawable.tcmlogo);
        toolbar.setTitle(getString(R.string.title_toolbar));
        setSupportActionBar(toolbar);
        mActivity = this;
        //Init the views
        initViews();
        mPrefs  = getPreferences(MODE_PRIVATE);
        //FillArray at least the first time
        FillArrayList();
    }

    private void FillArrayList(){
        /*
        All sessions
        @params (Name, value, state checkbox)
         */
        AlSessio.add(new Sessio("Sessió 01", 100, true));
        AlSessio.add(new Sessio("Sessió 02", 50, true));
        AlSessio.add(new Sessio("Sessió 03", 100, true));
        AlSessio.add(new Sessio("Sessió 04", 50, true));
        AlSessio.add(new Sessio("Sessió 05", 100, true));
        AlSessio.add(new Sessio("Sessió 06", 50, true));
        AlSessio.add(new Sessio("Sessió 07", 100, true));
        AlSessio.add(new Sessio("Sessió 08", 50, true));
        AlSessio.add(new Sessio("Sessió 09", 100, true));
        AlSessio.add(new Sessio("Sessió 10", 50, true));
        AlSessio.add(new Sessio("Sessió 11", 100, true));
        AlSessio.add(new Sessio("Sessió 12", 50, true));
        AlSessio.add(new Sessio("Sessió 13", 100, true));
        AlSessio.add(new Sessio("Sessió 14", 50, true));
        AlSessio.add(new Sessio("Sessió pr 01", 100, true));
        AlSessio.add(new Sessio("Sessió pr 02", 100, true));
        AlSessio.add(new Sessio("Sessió pr 03", 100, true));
        AlSessio.add(new Sessio("Sessió pr 04", 100, true));
    }

    private void initViews() {
        //Init the views with Textwatcher if necessary
        this.etPe1 = (EditText) findViewById(R.id.etPe1);
        etPe1.addTextChangedListener(new TeoWatcher(etPe1));
        this.etPe2 = (EditText) findViewById(R.id.etPe2);
        etPe2.addTextChangedListener(new TeoWatcher(etPe2));
        this.tvTeo = (TextView) findViewById(R.id.tvTeo);
        this.etPr_1 = (EditText) findViewById(R.id.etPr_1);
        etPr_1.addTextChangedListener(new PrWatcher(etPr_1));
        this.etPr_2 = (EditText) findViewById(R.id.etPr_2);
        etPr_2.addTextChangedListener(new PrWatcher(etPr_2));
        this.etPr_3 = (EditText) findViewById(R.id.etPr_3);
        etPr_3.addTextChangedListener(new PrWatcher(etPr_3));
        this.etPr_4 = (EditText) findViewById(R.id.etPr_4);
        etPr_4.addTextChangedListener(new PrWatcher(etPr_4));
        this.tvPr = (TextView) findViewById(R.id.tvPr);
        this.tvQPT = (TextView) findViewById(R.id.tvQPT);
        tvPr.addTextChangedListener(new QPTWatcher(tvPr));
        tvTeo.addTextChangedListener(new QPTWatcher(tvTeo));
        this.PctAs = (EditText) findViewById(R.id.etPctAs);
        this.ivAs = (ImageView) findViewById(R.id.ivGetPAs);
        this.etPctAs = (EditText) findViewById(R.id.etPctAs);
        this.ivAs.setOnClickListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_verify) {
            //Create the dialog to calculate the final mark.
            CreateQFDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void CreateQFDialog() {

        final Dialog mDialog;
        mDialog=new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.custom_dialog_qf);
        Button bt = (Button) mDialog.findViewById(R.id.btAceptarQF);
        final ImageView iv = (ImageView) mDialog.findViewById(R.id.ivAprovat);
        final TextView tv = (TextView) mDialog.findViewById(R.id.tvCalculant);
        final ProgressBar progressBar = (ProgressBar)mDialog.findViewById(R.id.view_progress);
        final TextView tvNota = (TextView) mDialog.findViewById(R.id.tvNotaqf);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                double qf = CalcularNota();
                progressBar.setVisibility(View.GONE);
                tv.setText("");
                iv.setVisibility(View.VISIBLE);
                if(qf == -1) mDialog.dismiss();
                qf = qf /10;
                if(qf>5){
                    //Aprobat
                    iv.setBackgroundResource(R.drawable.approved);
                    tvNota.setText(String.format(getString(R.string.qf_suspes),qf));
                    tvNota.setTextColor(ContextCompat.getColor(mActivity, R.color.verdaprovat));
                }
                else{
                    //Suspes
                    iv.setBackgroundResource(R.drawable.suspended);
                    tvNota.setText(String.format(getString(R.string.qf_suspes),qf));
                    tvNota.setTextColor(ContextCompat.getColor(mActivity, R.color.redsuspes));
                }

            }
        }, 2000);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.show();

    }

    private double CalcularNota() {
        double As = 0;
        try {
            double teo = ConvertToDouble(tvTeo);
            double Qtp = ConvertToDouble(tvQPT);
            double pr = ConvertToDouble(tvPr);
            int iPctAs = Integer.valueOf(etPctAs.getText().toString());
            if(iPctAs>80){
                As = (iPctAs-80)*0.25+5;
            }
            if((teo/10)<4){
                return Math.min(teo,Qtp);
            }
            if((teo/10)>=4 && (pr/10)>=4){
                return Math.max(Qtp, (Qtp*0.7) + (As * 0.3));
            }
            if((teo/10)>=4 && (teo/10)<4){
                return Qtp;
            }
        }
        catch (Exception e){
            ShowCustomToast.ShowToast(mActivity,getString(R.string.error_missing_value));
            return -1;
        }

        return 0;

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.ivGetPAs:
                    CreateDialog(getCorrectArrayList());
                break;
        }
    }
    private ArrayList<Sessio> getCorrectArrayList(){
        //if @String json is "" will return the AlSessio
        Gson gson = new Gson();
        String json = mPrefs.getString("ArrayListSesio", "");
        //Type necessary to specify the type of the value saved as a Json
        Type type = new TypeToken <TreeSet<Sessio>> () {}.getType();
        //Parse the json to a TreeSet<Sessio>
        TreeSet<Sessio> obj = gson.fromJson(json,type);

        return obj == null ? AlSessio : new ArrayList<Sessio>(obj);
    }
    private void CreateDialog(ArrayList<Sessio> arraylistsessio) {
        final Dialog mDialog;
        mDialog=new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.custom_dialog_as);
        ListView lv = (ListView) mDialog.findViewById(R.id.listView);
        Button bt = (Button) mDialog.findViewById(R.id.btAcceptAs);
        final SessioAdapter adapter = new SessioAdapter(mActivity, arraylistsessio);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Everytime it will save the updated list with checkbox checkeds or not
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                Gson gson = new Gson();
                TreeSet<Sessio> tsSessio = adapter.getArrayListModified();
                String json = gson.toJson(tsSessio);
                prefsEditor.putString("ArrayListSesio", json);
                prefsEditor.apply();
                UpdatePAS(tsSessio);
                mDialog.dismiss();
            }
        });
        lv.setAdapter(adapter);
        mDialog.show();

    }

    private void UpdatePAS(TreeSet<Sessio> tsSessio) {
        //Arithmetic median
        int cont = 0;
        for(Sessio s: tsSessio){
            if(s.isState()) {
                cont += s.getPercent();
            }
        }
        etPctAs.setText(String.valueOf(cont/tsSessio.size()));

    }

    //InnerClass TeoWatcher
    public class TeoWatcher implements TextWatcher {

        private EditText et;

        public TeoWatcher(EditText et) {
            this.et = et;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(TextUtils.isEmpty(editable.toString())){
                return;
            }
            try {
                //if is higher than 100 show error
                if (Integer.valueOf(et.getText().toString()) > 100) {
                    ShowCustomToast.ShowToast(mActivity,getString(R.string.error_valor_introduit));
                    //put all the TextView ""
                    et.setText("");
                    tvTeo.setText("");
                    tvQPT.setText("");
                }
            }
            catch(NumberFormatException e){
                //no dots and comma accepted
                ShowCustomToast.ShowToast(mActivity, getString(R.string.error_valor_enter));
                et.setText("");
            }
            try{
                if(etPe1.getText().toString().length()>=2 && etPe2.getText().toString().length()>=2) {
                    double pe1, pe2;
                    pe1 = Double.parseDouble(etPe1.getText().toString());
                    pe2 = Double.parseDouble(etPe2.getText().toString());
                    double res = (pe1 + pe2) / 2;
                    String formattedString = df2.format(res);
                    tvTeo.setText(formattedString);
                }
            }
            catch(NumberFormatException e){
                //Error message
                Log.d(mActivity.getLocalClassName(), e.getMessage());

            }
        }
    }
    //InnerClass PrWatcher
    public class PrWatcher implements TextWatcher {

        private EditText et;

        public PrWatcher(EditText et) {
            this.et = et;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

             if(TextUtils.isEmpty(editable.toString())){
                return;
            }
            try {
                if (Integer.valueOf(et.getText().toString()) > 100) {
                    ShowCustomToast.ShowToast(mActivity, getString(R.string.error_valor_introduit));
                    et.setText("");
                    tvPr.setText("");
                    tvQPT.setText("");
                }
            }
            catch(NumberFormatException e){
                ShowCustomToast.ShowToast(mActivity, getString(R.string.error_valor_enter));
                et.setText("");
            }
            try{
                if(etPr_1.getText().toString().length()>=2 && etPr_2.getText().toString().length()>=2 &&etPr_3.getText().toString().length()>=2 && etPr_4.getText().toString().length()>=2 ) {

                    double pr1, pr2, pr3,pr4;
                    pr1 = Double.parseDouble(etPr_1.getText().toString());
                    pr2 = Double.parseDouble(etPr_2.getText().toString());
                    pr3 = Double.parseDouble(etPr_3.getText().toString());
                    pr4 = Double.parseDouble(etPr_4.getText().toString());
                    double res = (pr1 + pr2 + pr3 + pr4) / 4;
                    String formattedString = df2.format(res);
                    tvPr.setText(formattedString);

                }
            }
            catch(NumberFormatException e){
                //Error message
                Log.d(mActivity.getLocalClassName(), e.getMessage());

            }
        }
    }
    //InnerClass PrWatcher
    public class QPTWatcher implements TextWatcher {

        private TextView tv;

        public QPTWatcher(TextView tv) {
            this.tv = tv;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!TextUtils.isEmpty(tvPr.getText().toString()) && !TextUtils.isEmpty(tvTeo.getText().toString())) {
                double dPrTotal, dTeoTotal;
                dPrTotal = ConvertToDouble(tvPr);
                dTeoTotal = ConvertToDouble(tvTeo);
                double res = (dPrTotal * 0.35) + (dTeoTotal * 0.65);
                String formattedString = df2.format(res);
                tvQPT.setText(formattedString);
            }
        }
    }
    //Converts the getText().toString() to double
    private double ConvertToDouble(TextView v){
        String s = v.getText().toString().replace(",",".");
        return Double.parseDouble(s);

    }

}