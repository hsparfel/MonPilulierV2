package com.pouillos.monpilulier.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.entities.Analyse;
import com.pouillos.monpilulier.entities.Profil;
import com.pouillos.monpilulier.entities.Rdv;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.fragments.DatePickerFragmentDateJour;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyProfilActivity extends AppCompatActivity {

    private ImageButton buttonValider;
    private ImageButton buttonAnnuler;
    private TextView textTaille;
    private SeekBar seekBarTaille;
    private TextView textPoids;
    private SeekBar seekBarPoids;
    private TextView textImc;

    private TextView textDate;
    private Date date;
    private Class<?> activitySource;

    List<Profil> listMyProfil;

    private Intent intent;
    private Utilisateur utilisateur;
    private Profil profil;

    private Button buttonTaille;
    private Button buttonPoids;
    private Button buttonImc;

    private View chart;
   // private int[] x = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
    //private String[] friends = new String[] { "Tuan", "Thai", "Tin", "Nguyen", "Thanh", "Phong", "Nhan", "Dung", "Son",
    //        "Thuy", "Cuong", "Khanh" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profil);

        buttonValider = (ImageButton) findViewById(R.id.buttonValider);
        buttonAnnuler= (ImageButton) findViewById(R.id.buttonAnnuler);
        textTaille = findViewById(R.id.textTaille);
        textPoids = findViewById(R.id.textPoids);
        textDate = findViewById(R.id.textDate);
        textImc = findViewById(R.id.textImc);
        seekBarTaille = findViewById(R.id.seekBarTaille);
        seekBarPoids = findViewById(R.id.seekBarPoids);
        buttonTaille = findViewById(R.id.buttonTaille);
        buttonImc = findViewById(R.id.buttonImc);
        buttonPoids = findViewById(R.id.buttonPoids);
        utilisateur  = (new Utilisateur()).findActifUser();




        traiterIntent();
        trouverDernierProfil();
        afficherProfil();

        buttonTaille.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creerGraphiqueTaille();
            }
        });

        buttonPoids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creerGraphiquePoids();
            }
        });

        buttonImc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creerGraphiqueImc();
            }
        });



        seekBarTaille.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                profil.setTaille(progress);
                profil.setImc(profil.calculerImc());
                textTaille.setText(String.valueOf(profil.getTaille()));
                textImc.setText(String.valueOf(profil.getImc()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBarPoids.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {

                profil.setPoids(progress/10);
                profil.setImc(profil.calculerImc());
                textPoids.setText(String.valueOf(progress/10));
                textImc.setText(String.valueOf(profil.getImc()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });



        buttonAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retourPagePrecedente();
            }
        });

        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //rediger les verifs de remplissage des champs

                //enregistrer en bdd
                saveToDb();

                //retour
                retourPagePrecedente();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void traiterIntent() {
        intent = getIntent();
        activitySource = (Class<?>) intent.getSerializableExtra("activitySource");
    }

    public void retourPagePrecedente() {
        Intent nextActivity = new Intent(MyProfilActivity.this,activitySource);
        nextActivity.putExtra("activitySource", MyProfilActivity.class);
        startActivity(nextActivity);
        finish();
    }


    public void saveToDb() {
        Profil profilToSave = new Profil(utilisateur, profil.getPoids(), profil.getTaille(), date);
        profilToSave.save();
    }

    public void trouverDernierProfil() {
        listMyProfil = Profil.findWithQuery(Profil.class, "Select * from Profil where utilisateur = ? order by date", utilisateur.getId().toString());
        if (listMyProfil.size()>0) {
            profil = listMyProfil.get(listMyProfil.size()-1);
        } else {
            profil = new Profil(utilisateur, 60f, 170, new Date());
        }
    }

    public void afficherProfil() {
        textTaille.setText(String.valueOf(profil.getTaille()));
        textPoids.setText(String.valueOf(profil.getPoids()));
        textDate.setText(new DateUtils().ecrireDate(profil.getDate()));
        textImc.setText(String.valueOf(profil.getImc()));
        seekBarTaille.setProgress(profil.getTaille());
        seekBarPoids.setProgress((int) (profil.getPoids()*10));
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragmentDateJour newFragment = new DatePickerFragmentDateJour();
        newFragment.show(getSupportFragmentManager(), "buttonDate");
        newFragment.setOnDateClickListener(new DatePickerFragmentDateJour.onDateClickListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                datePicker.setMaxDate(new Date().getTime());
                TextView tv1= (TextView) findViewById(R.id.textDate);
                String dateJour = ""+datePicker.getDayOfMonth();
                String dateMois = ""+(datePicker.getMonth()+1);
                String dateAnnee = ""+datePicker.getYear();
                if (datePicker.getDayOfMonth()<10) {
                    dateJour = "0"+dateJour;
                }
                if (datePicker.getMonth()<10) {
                    dateMois = "0"+dateMois;
                }
                String dateString = dateJour+"/"+dateMois+"/"+dateAnnee;
                tv1.setText("date: "+dateString);
                textDate.setError(null);
                DateFormat df = new SimpleDateFormat("dd/MM/yy");
                try{
                    date = df.parse(dateString);
                }catch(ParseException e){
                    System.out.println("ERROR");
                }
            }
        });
    }

    public void creerGraphiqueTaille() {
        //creer datas
        int tailleMin = listMyProfil.get(0).getTaille();
        int tailleMax = listMyProfil.get(0).getTaille();

        List<Integer> xList = new ArrayList<>();

        //calculer nb jour entre date debut et date fin
        Date dateDebut = listMyProfil.get(0).getDate();
        Date dateFin = listMyProfil.get(listMyProfil.size()-1).getDate();
        long diff = dateFin.getTime() - dateDebut.getTime();
        float res = (diff / (1000*60*60*24));
        int res2 = (int) res +1;
        int compt = 0;
        List<Integer> tailleList = new ArrayList<>();
        List<String> dateList = new ArrayList<>();
        Map<Integer,Integer> tailleMap = new HashMap<>();
        Map<Integer,String> dateMap = new HashMap<>();

        Calendar c = Calendar.getInstance();

        for (int i=0; i<=res2;i++) {
            xList.add(i);
            c.setTime(dateDebut);
            c.add(Calendar.DATE, i); //same with c.add(Calendar.DAY_OF_MONTH, 1);

            // convert calendar to date
            Date dateEnCours = c.getTime();

            if (compt<listMyProfil.size() && listMyProfil.get(compt).getDate().equals(dateEnCours)) {
                tailleMap.put(i,listMyProfil.get(compt).getTaille());
                dateMap.put(i,DateUtils.ecrireDate(listMyProfil.get(compt).getDate()));
                compt++;
            }
        }

        for (int i=0; i<listMyProfil.size();i++) {
            tailleList.add(listMyProfil.get(i).getTaille());
            if (listMyProfil.get(i).getTaille()>tailleMax){
                tailleMax = listMyProfil.get(i).getTaille();
            } else if (listMyProfil.get(i).getTaille()<tailleMin) {
                tailleMin = listMyProfil.get(i).getTaille();
            }
        }

    // Creating an XYSeries for Height
    XYSeries tailleSeries = new XYSeries("Taille");
    // Adding data to Height Series
        for (int i = 0; i < xList.size(); i++) {
        if (tailleMap.get(i)!=null) {
            tailleSeries.add(i, tailleMap.get(i));
        }
    }

    // Creating a dataset to hold taille series
    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
    // Adding Height Series to dataset
        dataset.addSeries(tailleSeries);

    // Creating XYSeriesRenderer to customize tailleSeries
    XYSeriesRenderer tailleRenderer = new XYSeriesRenderer();
        tailleRenderer.setColor(Color.BLUE);
        tailleRenderer.setFillPoints(true);
        tailleRenderer.setDisplayChartValues(true);

    // Creating a XYMultipleSeriesRenderer to customize the whole chart
    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setXLabels(0);
    //renderer.setChartTitle("Comparing taille chart ");
    //renderer.setXTitle("Friends Name");
    //renderer.setYTitle("Unit in centimeter");

    /***
     * Customizing graphs
     */
    // setting text size of the title
        renderer.setChartTitleTextSize(28);
    // setting text size of the axis title
        renderer.setAxisTitleTextSize(24);
    // setting text size of the graph lable
        renderer.setLabelsTextSize(12);
    // setting zoom buttons visiblity
        renderer.setZoomButtonsVisible(false);
    // setting pan enablity which uses graph to move on both axis
        //renderer.setPanEnabled(true, true);
        renderer.setPanEnabled(false, false);
    // setting click false on graph
        renderer.setClickEnabled(false);
    // setting zoom to false on both axis
        renderer.setZoomEnabled(false, false);
       // renderer.setZoomEnabled(true, true);
    // setting lines to display on y axis
        renderer.setShowGridY(true);
    // setting lines to display on x axis
        renderer.setShowGridX(true);
    // setting legend to fit the screen size
        renderer.setFitLegend(true);
    // setting displaying line on grid
        renderer.setShowGrid(true);
    // setting zoom to false
        renderer.setZoomEnabled(false);
    // setting external zoom functions to false
        renderer.setExternalZoomEnabled(false);
    // setting displaying lines on graph to be formatted(like using
    // graphics)
        renderer.setAntialiasing(true);
    // setting to in scroll to false
        renderer.setInScroll(false);
    // setting to set legend taille of the graph
        renderer.setLegendHeight(30);
    // setting x axis label align
        renderer.setXLabelsAlign(Paint.Align.CENTER);
    // setting y axis label to align
        renderer.setYLabelsAlign(Paint.Align.LEFT);
    // setting text style
        renderer.setTextTypeface("sans_serif", Typeface.NORMAL);
    // setting number of values to display in y axis
        renderer.setYLabels(20);
    //setting x axis min value
        renderer.setYAxisMin(tailleMin-10);
    // setting y axis max value
        renderer.setYAxisMax(tailleMax+10);
    // setting used to move the graph on xaxiz to .5 to the right
        renderer.setXAxisMin(-0.5);
    // setting used to move the graph on xaxiz to .5 to the right
    //renderer.setXAxisMax(11);
        renderer.setXAxisMax(xList.size());
    // setting bar size or space between two bars
    //renderer.setBarSpacing(0.5);
    // Setting background color of the graph to transparent
        renderer.setBackgroundColor(Color.TRANSPARENT);
    // Setting margin color of the graph to transparent
        renderer.setMarginsColor(getResources().getColor(android.R.color.transparent));
        renderer.setApplyBackgroundColor(true);
        renderer.setScale(2f);
    // setting x axis point size
        renderer.setPointSize(4f);
    // setting the margin size for the graph in the order top, left, bottom,
    // right
        renderer.setMargins(new int[] { 10, 10, 10, 10 });

        for (int i = 0; i < xList.size(); i++) {
            if (dateMap.get(i)!=null) {
                renderer.addXTextLabel(i, dateMap.get(i));
            }
        //renderer.setXLabelsPadding(10);

        //renderer.setYLabelsPadding(10);
    }

    // Adding tailleRender to multipleRenderer
    // Note: The order of adding dataseries to dataset and renderers to
    // multipleRenderer
    // should be same
        renderer.addSeriesRenderer(tailleRenderer);

    // this part is used to display graph on the xml
    LinearLayout chartContainer = (LinearLayout) findViewById(R.id.graphique);
    // remove any views before u paint the chart
        chartContainer.removeAllViews();
    //drawing bar chart
    chart = ChartFactory.getLineChartView(MyProfilActivity.this, dataset, renderer);
        chartContainer.addView(chart);
    }

    public void creerGraphiqueImc() {
        //creer datas
        float imcMin = listMyProfil.get(0).getImcArrondi();
        float imcMax = listMyProfil.get(0).getImcArrondi();

        List<Integer> xList = new ArrayList<>();

        //calculer nb jour entre date debut et date fin
        Date dateDebut = listMyProfil.get(0).getDate();
        Date dateFin = listMyProfil.get(listMyProfil.size()-1).getDate();
        long diff = dateFin.getTime() - dateDebut.getTime();
        float res = (diff / (1000*60*60*24));
        int res2 = (int) res +1;
        int compt = 0;
        List<Float> imcList = new ArrayList<>();
        List<String> dateList = new ArrayList<>();
        Map<Integer,Float> imcMap = new HashMap<>();
        Map<Integer,String> dateMap = new HashMap<>();

        Calendar c = Calendar.getInstance();

        for (int i=0; i<=res2;i++) {
            xList.add(i);
            c.setTime(dateDebut);
            c.add(Calendar.DATE, i); //same with c.add(Calendar.DAY_OF_MONTH, 1);

            // convert calendar to date
            Date dateEnCours = c.getTime();

            if (compt<listMyProfil.size() && listMyProfil.get(compt).getDate().equals(dateEnCours)) {
                imcMap.put(i,listMyProfil.get(compt).getImcArrondi());
                dateMap.put(i,new DateUtils().ecrireDate(listMyProfil.get(compt).getDate()));
                compt++;
            }
        }

        for (int i=0; i<listMyProfil.size();i++) {
            imcList.add(listMyProfil.get(i).getImcArrondi());
            if (listMyProfil.get(i).getImcArrondi()>imcMax){
                imcMax = listMyProfil.get(i).getImcArrondi();
            } else if (listMyProfil.get(i).getImcArrondi()<imcMin) {
                imcMin = listMyProfil.get(i).getImcArrondi();
            }
        }

        // Creating an XYSeries for Height
        XYSeries imcSeries = new XYSeries("Imc");
        // Adding data to Height Series
        for (int i = 0; i < xList.size(); i++) {
            if (imcMap.get(i)!=null) {
                imcSeries.add(i, imcMap.get(i));
            }
        }

        // Creating a dataset to hold taille series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Height Series to dataset
        dataset.addSeries(imcSeries);

        // Creating XYSeriesRenderer to customize tailleSeries
        XYSeriesRenderer imcRenderer = new XYSeriesRenderer();
        imcRenderer.setColor(Color.RED);
        imcRenderer.setFillPoints(true);
        imcRenderer.setDisplayChartValues(true);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setXLabels(0);
        //renderer.setChartTitle("Comparing taille chart ");
        //renderer.setXTitle("Friends Name");
        //renderer.setYTitle("Unit in centimeter");

        /***
         * Customizing graphs
         */
        // setting text size of the title
        renderer.setChartTitleTextSize(28);
        // setting text size of the axis title
        renderer.setAxisTitleTextSize(24);
        // setting text size of the graph lable
        renderer.setLabelsTextSize(12);
        // setting zoom buttons visiblity
        renderer.setZoomButtonsVisible(false);
        // setting pan enablity which uses graph to move on both axis
        //renderer.setPanEnabled(true, true);
        renderer.setPanEnabled(false, false);
        // setting click false on graph
        renderer.setClickEnabled(false);
        // setting zoom to false on both axis
        renderer.setZoomEnabled(false, false);
        // renderer.setZoomEnabled(true, true);
        // setting lines to display on y axis
        renderer.setShowGridY(true);
        // setting lines to display on x axis
        renderer.setShowGridX(true);
        // setting legend to fit the screen size
        renderer.setFitLegend(true);
        // setting displaying line on grid
        renderer.setShowGrid(true);
        // setting zoom to false
        renderer.setZoomEnabled(false);
        // setting external zoom functions to false
        renderer.setExternalZoomEnabled(false);
        // setting displaying lines on graph to be formatted(like using
        // graphics)
        renderer.setAntialiasing(true);
        // setting to in scroll to false
        renderer.setInScroll(false);
        // setting to set legend taille of the graph
        renderer.setLegendHeight(30);
        // setting x axis label align
        renderer.setXLabelsAlign(Paint.Align.CENTER);
        // setting y axis label to align
        renderer.setYLabelsAlign(Paint.Align.LEFT);
        // setting text style
        renderer.setTextTypeface("sans_serif", Typeface.NORMAL);
        // setting number of values to display in y axis
        renderer.setYLabels(20);
        //setting x axis min value
        renderer.setYAxisMin(imcMin-10);
        // setting y axis max value
        renderer.setYAxisMax(imcMax+10);
        // setting used to move the graph on xaxiz to .5 to the right
        renderer.setXAxisMin(-0.5);
        // setting used to move the graph on xaxiz to .5 to the right
        //renderer.setXAxisMax(11);
        renderer.setXAxisMax(xList.size());
        // setting bar size or space between two bars
        //renderer.setBarSpacing(0.5);
        // Setting background color of the graph to transparent
        renderer.setBackgroundColor(Color.TRANSPARENT);
        // Setting margin color of the graph to transparent
        renderer.setMarginsColor(getResources().getColor(android.R.color.transparent));
        renderer.setApplyBackgroundColor(true);
        renderer.setScale(2f);
        // setting x axis point size
        renderer.setPointSize(4f);
        // setting the margin size for the graph in the order top, left, bottom,
        // right
        renderer.setMargins(new int[] { 10, 10, 10, 10 });

        for (int i = 0; i < xList.size(); i++) {
            if (dateMap.get(i)!=null) {
                renderer.addXTextLabel(i, dateMap.get(i));
            }
            //renderer.setXLabelsPadding(10);

            //renderer.setYLabelsPadding(10);
        }

        // Adding tailleRender to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to
        // multipleRenderer
        // should be same
        renderer.addSeriesRenderer(imcRenderer);

        // this part is used to display graph on the xml
        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.graphique);
        // remove any views before u paint the chart
        chartContainer.removeAllViews();
        //drawing bar chart
        chart = ChartFactory.getLineChartView(MyProfilActivity.this, dataset, renderer);
        chartContainer.addView(chart);
    }

    public void creerGraphiquePoids() {
        //creer datas
        float poidsMin = listMyProfil.get(0).getPoids();
        float poidsMax = listMyProfil.get(0).getPoids();

        List<Integer> xList = new ArrayList<>();

        //calculer nb jour entre date debut et date fin
        Date dateDebut = listMyProfil.get(0).getDate();
        Date dateFin = listMyProfil.get(listMyProfil.size()-1).getDate();
        long diff = dateFin.getTime() - dateDebut.getTime();
        float res = (diff / (1000*60*60*24));
        int res2 = (int) res +1;
        int compt = 0;
        List<Float> poidsList = new ArrayList<>();
        List<String> dateList = new ArrayList<>();
        Map<Integer,Float> poidsMap = new HashMap<>();
        Map<Integer,String> dateMap = new HashMap<>();

        Calendar c = Calendar.getInstance();

        for (int i=0; i<=res2;i++) {
            xList.add(i);
            c.setTime(dateDebut);
            c.add(Calendar.DATE, i); //same with c.add(Calendar.DAY_OF_MONTH, 1);

            // convert calendar to date
            Date dateEnCours = c.getTime();

            if (compt<listMyProfil.size() && listMyProfil.get(compt).getDate().equals(dateEnCours)) {
                poidsMap.put(i,listMyProfil.get(compt).getPoids());
                dateMap.put(i,new DateUtils().ecrireDate(listMyProfil.get(compt).getDate()));
                compt++;
            }
        }

        for (int i=0; i<listMyProfil.size();i++) {
            poidsList.add(listMyProfil.get(i).getPoids());
            if (listMyProfil.get(i).getPoids()>poidsMax){
                poidsMax = listMyProfil.get(i).getPoids();
            } else if (listMyProfil.get(i).getPoids()<poidsMin) {
                poidsMin = listMyProfil.get(i).getPoids();
            }
        }

        // Creating an XYSeries for Height
        XYSeries poidsSeries = new XYSeries("Poids");
        // Adding data to Height Series
        for (int i = 0; i < xList.size(); i++) {
            if (poidsMap.get(i)!=null) {
                poidsSeries.add(i, poidsMap.get(i));
            }
        }

        // Creating a dataset to hold taille series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Height Series to dataset
        dataset.addSeries(poidsSeries);

        // Creating XYSeriesRenderer to customize tailleSeries
        XYSeriesRenderer poidsRenderer = new XYSeriesRenderer();
        poidsRenderer.setColor(Color.BLACK);
        poidsRenderer.setFillPoints(true);
        poidsRenderer.setDisplayChartValues(true);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setXLabels(0);
        //renderer.setChartTitle("Comparing taille chart ");
        //renderer.setXTitle("Friends Name");
        //renderer.setYTitle("Unit in centimeter");

        /***
         * Customizing graphs
         */
        // setting text size of the title
        renderer.setChartTitleTextSize(28);
        // setting text size of the axis title
        renderer.setAxisTitleTextSize(24);
        // setting text size of the graph lable
        renderer.setLabelsTextSize(12);
        // setting zoom buttons visiblity
        renderer.setZoomButtonsVisible(false);
        // setting pan enablity which uses graph to move on both axis
        //renderer.setPanEnabled(true, true);
        renderer.setPanEnabled(false, false);
        // setting click false on graph
        renderer.setClickEnabled(false);
        // setting zoom to false on both axis
        renderer.setZoomEnabled(false, false);
        // renderer.setZoomEnabled(true, true);
        // setting lines to display on y axis
        renderer.setShowGridY(true);
        // setting lines to display on x axis
        renderer.setShowGridX(true);
        // setting legend to fit the screen size
        renderer.setFitLegend(true);
        // setting displaying line on grid
        renderer.setShowGrid(true);
        // setting zoom to false
        renderer.setZoomEnabled(false);
        // setting external zoom functions to false
        renderer.setExternalZoomEnabled(false);
        // setting displaying lines on graph to be formatted(like using
        // graphics)
        renderer.setAntialiasing(true);
        // setting to in scroll to false
        renderer.setInScroll(false);
        // setting to set legend taille of the graph
        renderer.setLegendHeight(30);
        // setting x axis label align
        renderer.setXLabelsAlign(Paint.Align.CENTER);
        // setting y axis label to align
        renderer.setYLabelsAlign(Paint.Align.LEFT);
        // setting text style
        renderer.setTextTypeface("sans_serif", Typeface.NORMAL);
        // setting number of values to display in y axis
        renderer.setYLabels(20);
        //setting x axis min value
        renderer.setYAxisMin(poidsMin-10);
        // setting y axis max value
        renderer.setYAxisMax(poidsMax+10);
        // setting used to move the graph on xaxiz to .5 to the right
        renderer.setXAxisMin(-0.5);
        // setting used to move the graph on xaxiz to .5 to the right
        //renderer.setXAxisMax(11);
        renderer.setXAxisMax(xList.size());
        // setting bar size or space between two bars
        //renderer.setBarSpacing(0.5);
        // Setting background color of the graph to transparent
        renderer.setBackgroundColor(Color.TRANSPARENT);
        // Setting margin color of the graph to transparent
        renderer.setMarginsColor(getResources().getColor(android.R.color.transparent));
        renderer.setApplyBackgroundColor(true);
        renderer.setScale(2f);
        // setting x axis point size
        renderer.setPointSize(4f);
        // setting the margin size for the graph in the order top, left, bottom,
        // right
        renderer.setMargins(new int[] { 10, 10, 10, 10 });

        for (int i = 0; i < xList.size(); i++) {
            if (dateMap.get(i)!=null) {
                renderer.addXTextLabel(i, dateMap.get(i));
            }
            //renderer.setXLabelsPadding(10);

            //renderer.setYLabelsPadding(10);
        }

        // Adding tailleRender to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to
        // multipleRenderer
        // should be same
        renderer.addSeriesRenderer(poidsRenderer);

        // this part is used to display graph on the xml
        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.graphique);
        // remove any views before u paint the chart
        chartContainer.removeAllViews();
        //drawing bar chart
        chart = ChartFactory.getLineChartView(MyProfilActivity.this, dataset, renderer);
        chartContainer.addView(chart);
    }
}

