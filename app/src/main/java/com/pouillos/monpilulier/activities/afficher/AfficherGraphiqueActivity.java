package com.pouillos.monpilulier.activities.afficher;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;

import com.google.android.material.chip.Chip;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.NavDrawerActivity;
import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.entities.Profil;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class AfficherGraphiqueActivity extends NavDrawerActivity implements Serializable, BasicUtils {

    @State
    Utilisateur activeUser;
    
    private List<Profil> listProfil;

    @BindView(R.id.chipPoids)
    Chip chipPoids;
    @BindView(R.id.chipTaille)
    Chip chipTaille;
    @BindView(R.id.chipImc)
    Chip chipImc ;
    @BindView(R.id.my_progressBar)
    ProgressBar progressBar;

    private View chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_afficher_graphique);
// 6 - Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);

        setTitle(getString(R.string.evolution));
        progressBar.setVisibility(View.VISIBLE);
        AfficherGraphiqueActivity.AsyncTaskRunnerBD runnerBD = new AfficherGraphiqueActivity.AsyncTaskRunnerBD();
        runnerBD.execute();


    }

    public class AsyncTaskRunnerBD extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {

            publishProgress(0);
            activeUser = findActiveUser();
            publishProgress(20);
            listProfil = Profil.findWithQuery(Profil.class, "Select * from Profil where utilisateur = ? order by date", activeUser.getId().toString());
            //Collections.reverseOrder(listProfil);
            publishProgress(100);
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    @OnClick(R.id.chipPoids)
    public void chipPoidsClick() {
        creerGraphiquePoids();
    }

    @OnClick(R.id.chipTaille)
    public void chipTailleClick() {
        creerGraphiqueTaille();
    }

    @OnClick(R.id.chipImc)
    public void chipImcClick() {
        creerGraphiqueImc();
    }

    public void creerGraphiqueTaille() {


        //creer datas
        int tailleMin = listProfil.get(0).getTaille();
        int tailleMax = listProfil.get(0).getTaille();

        int xValueMax = 0;
        List<Integer> xList = new ArrayList<>();

        //calculer nb jour entre date debut et date fin
        Date dateDebut = listProfil.get(0).getDate();
        Date dateFin = listProfil.get(listProfil.size()-1).getDate();
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

            if (compt<listProfil.size() && listProfil.get(compt).getDate().equals(dateEnCours)) {
                tailleMap.put(i,listProfil.get(compt).getTaille());
                dateMap.put(i,new DateUtils().ecrireDate(listProfil.get(compt).getDate()));
                compt++;
                xValueMax = i;
            }
        }

        for (int i=0; i<listProfil.size();i++) {
            tailleList.add(listProfil.get(i).getTaille());
            if (listProfil.get(i).getTaille()>tailleMax){
                tailleMax = listProfil.get(i).getTaille();
            } else if (listProfil.get(i).getTaille()<tailleMin) {
                tailleMin = listProfil.get(i).getTaille();
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
        tailleRenderer.setDisplayChartValues(false);
        tailleRenderer.setPointStyle(PointStyle.CIRCLE);
        tailleRenderer.setLineWidth(2);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setXLabels(0);
        renderer.setLabelsTextSize(25);
        renderer.setZoomButtonsVisible(false);
        renderer.setPanEnabled(false, false);
        renderer.setClickEnabled(false);
        renderer.setZoomEnabled(false, false);
        renderer.setShowGridY(true);
        renderer.setShowGridX(true);
        renderer.setFitLegend(true);
        renderer.setShowGrid(true);
        renderer.setZoomEnabled(false);
        renderer.setExternalZoomEnabled(false);
        renderer.setAntialiasing(true);
        renderer.setInScroll(false);
        renderer.setXLabelsAlign(Paint.Align.CENTER);
        renderer.setYLabelsAlign(Paint.Align.LEFT);
        renderer.setTextTypeface("sans_serif", Typeface.NORMAL);
        renderer.setYLabels(20);
        renderer.setYAxisMin(tailleMin-5);
        renderer.setYAxisMax(tailleMax+5);
        renderer.setXAxisMin(-xList.size()*12/100);
        renderer.setXAxisMax(xList.size()+xList.size()*12/100);
        renderer.setBackgroundColor(Color.TRANSPARENT);
        renderer.setMarginsColor(getResources().getColor(android.R.color.transparent));
        renderer.setApplyBackgroundColor(true);
        renderer.setMargins(new int[] { 0, 0, 0, 0 });
        renderer.addXTextLabel(0, dateMap.get(0));
        renderer.addXTextLabel(xList.size()-1, dateMap.get(xValueMax));
        renderer.addSeriesRenderer(tailleRenderer);
        renderer.setShowLegend(false);

        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.graphique);
        chartContainer.removeAllViews();
        chart = ChartFactory.getLineChartView(AfficherGraphiqueActivity.this, dataset, renderer);
        chartContainer.addView(chart);
    }
        /*
        //creer datas
        int tailleMin = listProfil.get(0).getTaille();
        int tailleMax = listProfil.get(0).getTaille();

        List<Integer> xList = new ArrayList<>();

        //calculer nb jour entre date debut et date fin
        Date dateDebut = listProfil.get(0).getDate();
        Date dateFin = listProfil.get(listProfil.size()-1).getDate();
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

            if (compt<listProfil.size() && listProfil.get(compt).getDate().equals(dateEnCours)) {
                tailleMap.put(i,listProfil.get(compt).getTaille());
                dateMap.put(i, DateUtils.ecrireDate(listProfil.get(compt).getDate()));
                compt++;
            }
        }

        for (int i=0; i<listProfil.size();i++) {
            tailleList.add(listProfil.get(i).getTaille());
            if (listProfil.get(i).getTaille()>tailleMax){
                tailleMax = listProfil.get(i).getTaille();
            } else if (listProfil.get(i).getTaille()<tailleMin) {
                tailleMin = listProfil.get(i).getTaille();
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
       /* renderer.setChartTitleTextSize(28);
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
        chart = ChartFactory.getLineChartView(AfficherGraphiqueActivity.this, dataset, renderer);
        chartContainer.addView(chart);
    }*/

    public void creerGraphiqueImc() {
        //creer datas
        float imcMin = listProfil.get(0).getImc();
        float imcMax = listProfil.get(0).getImc();

        int xValueMax = 0;
        List<Integer> xList = new ArrayList<>();

        //calculer nb jour entre date debut et date fin
        Date dateDebut = listProfil.get(0).getDate();
        Date dateFin = listProfil.get(listProfil.size()-1).getDate();
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

            if (compt<listProfil.size() && listProfil.get(compt).getDate().equals(dateEnCours)) {
                imcMap.put(i,listProfil.get(compt).getImc());
                dateMap.put(i,new DateUtils().ecrireDate(listProfil.get(compt).getDate()));
                compt++;
                xValueMax = i;
            }
        }

        for (int i=0; i<listProfil.size();i++) {
            imcList.add(listProfil.get(i).getImc());
            if (listProfil.get(i).getImc()>imcMax){
                imcMax = listProfil.get(i).getImc();
            } else if (listProfil.get(i).getImc()<imcMin) {
                imcMin = listProfil.get(i).getImc();
            }
        }

        // Creating an XYSeries for Height
        XYSeries imcSeries = new XYSeries("Imc");
        // Adding data to Height Series
        for (int i = 0; i < xList.size(); i++) {
            if (imcMap.get(i)!=null) {
                imcSeries.add(i, floatArrondi(imcMap.get(i),2));
            }
        }

        // Creating a dataset to hold taille series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Height Series to dataset
        dataset.addSeries(imcSeries);

        // Creating XYSeriesRenderer to customize tailleSeries
        XYSeriesRenderer imcRenderer = new XYSeriesRenderer();
        imcRenderer.setColor(Color.BLUE);
        imcRenderer.setFillPoints(true);
        imcRenderer.setDisplayChartValues(false);
        imcRenderer.setPointStyle(PointStyle.CIRCLE);
        imcRenderer.setLineWidth(2);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setXLabels(0);
        renderer.setLabelsTextSize(25);
        renderer.setZoomButtonsVisible(false);
        renderer.setPanEnabled(false, false);
        renderer.setClickEnabled(false);
        renderer.setZoomEnabled(false, false);
        renderer.setShowGridY(true);
        renderer.setShowGridX(true);
        renderer.setFitLegend(true);
        renderer.setShowGrid(true);
        renderer.setZoomEnabled(false);
        renderer.setExternalZoomEnabled(false);
        renderer.setAntialiasing(true);
        renderer.setInScroll(false);
        renderer.setXLabelsAlign(Paint.Align.CENTER);
        renderer.setYLabelsAlign(Paint.Align.LEFT);
        renderer.setTextTypeface("sans_serif", Typeface.NORMAL);
        renderer.setYLabels(20);
        renderer.setYAxisMin(imcMin-1);
        renderer.setYAxisMax(imcMax+1);
        renderer.setXAxisMin(-xList.size()*12/100);
        renderer.setXAxisMax(xList.size()+xList.size()*12/100);
        renderer.setBackgroundColor(Color.TRANSPARENT);
        renderer.setMarginsColor(getResources().getColor(android.R.color.transparent));
        renderer.setApplyBackgroundColor(true);
        renderer.setMargins(new int[] { 0, 0, 0, 0 });
        renderer.addXTextLabel(0, dateMap.get(0));
        renderer.addXTextLabel(xList.size()-1, dateMap.get(xValueMax));
        renderer.addSeriesRenderer(imcRenderer);
        renderer.setShowLegend(false);

        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.graphique);
        chartContainer.removeAllViews();
        chart = ChartFactory.getLineChartView(AfficherGraphiqueActivity.this, dataset, renderer);
        chartContainer.addView(chart);
        /*//creer datas
        float imcMin = listProfil.get(0).getImcArrondi();
        float imcMax = listProfil.get(0).getImcArrondi();

        List<Integer> xList = new ArrayList<>();

        //calculer nb jour entre date debut et date fin
        Date dateDebut = listProfil.get(0).getDate();
        Date dateFin = listProfil.get(listProfil.size()-1).getDate();
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

            if (compt<listProfil.size() && listProfil.get(compt).getDate().equals(dateEnCours)) {
                imcMap.put(i,listProfil.get(compt).getImcArrondi());
                dateMap.put(i,new DateUtils().ecrireDate(listProfil.get(compt).getDate()));
                compt++;
            }
        }

        for (int i=0; i<listProfil.size();i++) {
            imcList.add(listProfil.get(i).getImcArrondi());
            if (listProfil.get(i).getImcArrondi()>imcMax){
                imcMax = listProfil.get(i).getImcArrondi();
            } else if (listProfil.get(i).getImcArrondi()<imcMin) {
                imcMin = listProfil.get(i).getImcArrondi();
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
       /* // setting text size of the title
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
        chart = ChartFactory.getLineChartView(AfficherGraphiqueActivity.this, dataset, renderer);
        chartContainer.addView(chart);*/
    }

    public void creerGraphiquePoids() {
        //creer datas
        float poidsMin = listProfil.get(0).getPoids();
        float poidsMax = listProfil.get(0).getPoids();

        int xValueMax = 0;
        List<Integer> xList = new ArrayList<>();

        //calculer nb jour entre date debut et date fin
        Date dateDebut = listProfil.get(0).getDate();
        Date dateFin = listProfil.get(listProfil.size()-1).getDate();
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

            if (compt<listProfil.size() && listProfil.get(compt).getDate().equals(dateEnCours)) {
                poidsMap.put(i,listProfil.get(compt).getPoids());
                dateMap.put(i,new DateUtils().ecrireDate(listProfil.get(compt).getDate()));
                compt++;
                xValueMax = i;
            }
        }

        for (int i=0; i<listProfil.size();i++) {
            poidsList.add(listProfil.get(i).getPoids());
            if (listProfil.get(i).getPoids()>poidsMax){
                poidsMax = listProfil.get(i).getPoids();
            } else if (listProfil.get(i).getPoids()<poidsMin) {
                poidsMin = listProfil.get(i).getPoids();
            }
        }

        // Creating an XYSeries for Height
        XYSeries poidsSeries = new XYSeries("Poids");
        // Adding data to Height Series
        for (int i = 0; i < xList.size(); i++) {
            if (poidsMap.get(i)!=null) {
                poidsSeries.add(i, floatArrondi(poidsMap.get(i),2));
            }
        }

        // Creating a dataset to hold taille series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Height Series to dataset
        dataset.addSeries(poidsSeries);

        // Creating XYSeriesRenderer to customize tailleSeries
        XYSeriesRenderer poidsRenderer = new XYSeriesRenderer();
        poidsRenderer.setColor(Color.BLUE);
        poidsRenderer.setFillPoints(true);
        poidsRenderer.setDisplayChartValues(false);
        poidsRenderer.setPointStyle(PointStyle.CIRCLE);
        poidsRenderer.setLineWidth(2);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setXLabels(0);
        renderer.setLabelsTextSize(25);
        renderer.setZoomButtonsVisible(false);
        renderer.setPanEnabled(false, false);
        renderer.setClickEnabled(false);
        renderer.setZoomEnabled(false, false);
        renderer.setShowGridY(true);
        renderer.setShowGridX(true);
        renderer.setFitLegend(true);
        renderer.setShowGrid(true);
        renderer.setZoomEnabled(false);
        renderer.setExternalZoomEnabled(false);
        renderer.setAntialiasing(true);
        renderer.setInScroll(false);
        renderer.setXLabelsAlign(Paint.Align.CENTER);
        renderer.setYLabelsAlign(Paint.Align.LEFT);
        renderer.setTextTypeface("sans_serif", Typeface.NORMAL);
        renderer.setYLabels(20);
        renderer.setYAxisMin(poidsMin-1);
        renderer.setYAxisMax(poidsMax+1);
        renderer.setXAxisMin(-xList.size()*12/100);
        renderer.setXAxisMax(xList.size()+xList.size()*12/100);
        renderer.setBackgroundColor(Color.TRANSPARENT);
        renderer.setMarginsColor(getResources().getColor(android.R.color.transparent));
        renderer.setApplyBackgroundColor(true);
        renderer.setMargins(new int[] { 0, 0, 0, 0 });
        renderer.addXTextLabel(0, dateMap.get(0));
        renderer.addXTextLabel(xList.size()-1, dateMap.get(xValueMax));
        renderer.addSeriesRenderer(poidsRenderer);
        renderer.setShowLegend(false);

        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.graphique);
        chartContainer.removeAllViews();
        chart = ChartFactory.getLineChartView(AfficherGraphiqueActivity.this, dataset, renderer);
        chartContainer.addView(chart);
    }
}
