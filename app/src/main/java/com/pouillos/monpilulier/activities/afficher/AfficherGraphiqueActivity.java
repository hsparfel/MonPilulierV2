package com.pouillos.monpilulier.activities.afficher;

import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.material.chip.Chip;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.NavDrawerActivity;
import com.pouillos.monpilulier.activities.graphique.DayAxisValueFormatter;
import com.pouillos.monpilulier.activities.graphique.MyValueFormatter;
import com.pouillos.monpilulier.activities.graphique.XYMarkerView;
import com.pouillos.monpilulier.entities.Profil;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.enumeration.Graphique;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class AfficherGraphiqueActivity extends NavDrawerActivity implements Serializable, BasicUtils, SeekBar.OnSeekBarChangeListener, OnChartValueSelectedListener {

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

    //  @BindView(R.id.lineChart)
    //   LineChart lineChart;

    @BindView(R.id.chart1)
    LineChart chart;


    protected Typeface tfRegular;
    protected Typeface tfLight;

    Date dateDebut;
    Date dateFin;
    int deltaAnnee;

    int tailleMax;
    Float poidsMax;
    Float imcMax;
    int tailleMin;
    Float poidsMin;
    Float imcMin;


    //private View chart;

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
        chart.setVisibility(View.GONE);
        AfficherGraphiqueActivity.AsyncTaskRunnerBD runnerBD = new AfficherGraphiqueActivity.AsyncTaskRunnerBD();
        runnerBD.execute();

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    private final RectF onValueSelectedRectF = new RectF();

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;

        RectF bounds = onValueSelectedRectF;
        /*   chart.getBarBounds((Entry) e, bounds);*/
        MPPointF position = chart.getPosition(e, YAxis.AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + chart.getLowestVisibleX() + ", high: "
                        + chart.getHighestVisibleX());

        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() {

    }

    public class AsyncTaskRunnerBD extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {

            publishProgress(0);
            activeUser = findActiveUser();
            publishProgress(20);
            listProfil = Profil.findWithQuery(Profil.class, "Select * from Profil where utilisateur = ? order by date", activeUser.getId().toString());
            //Collections.reverseOrder(listProfil);
            dateDebut = listProfil.get(0).getDate();
            dateFin = listProfil.get(listProfil.size()-1).getDate();
            deltaAnnee = dateFin.getYear()-dateDebut.getYear();

            tailleMax = listProfil.get(0).getTaille();
            tailleMin = listProfil.get(0).getTaille();
            poidsMax = listProfil.get(0).getPoids();
            poidsMin = listProfil.get(0).getPoids();
            imcMax = listProfil.get(0).getImc();
            imcMin = listProfil.get(0).getImc();
            for (Profil currentProfil : listProfil) {
                if (currentProfil.getTaille() > tailleMax) {
                    tailleMax = currentProfil.getTaille();
                } else if (currentProfil.getTaille() < tailleMin) {
                    tailleMin = currentProfil.getTaille();
                }
                if (currentProfil.getPoids() > poidsMax) {
                    poidsMax = currentProfil.getPoids();
                } else if (currentProfil.getPoids() < poidsMin) {
                    poidsMin = currentProfil.getPoids();
                }
                if (currentProfil.getImc() > imcMax) {
                    imcMax = currentProfil.getImc();
                } else if (currentProfil.getImc() < imcMin) {
                    imcMin = currentProfil.getImc();
                }
            }

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

    private void setData(List<Profil> listeProfil,Graphique typeGraphique) {
        ArrayList<Entry> values = new ArrayList<>();
        for (Profil currentProfil : listeProfil) {
            if (typeGraphique == Graphique.Weight) {
                values.add(new Entry(currentProfil.getDate().getTime(),currentProfil.getPoids()));
            } else if (typeGraphique == Graphique.Size) {
                values.add(new Entry(currentProfil.getDate().getTime(),currentProfil.getTaille()));
            } else if (typeGraphique == Graphique.Imc) {
                values.add(new Entry(currentProfil.getDate().getTime(),currentProfil.getImc()));
            }

        }
        LineDataSet set1 = null;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            if (typeGraphique == Graphique.Weight) {
                set1 = new LineDataSet(values, Graphique.Weight.getName());
            } else if (typeGraphique == Graphique.Size) {
                set1 = new LineDataSet(values, Graphique.Size.getName());
            } else if (typeGraphique == Graphique.Imc) {
                set1 = new LineDataSet(values, Graphique.Imc.getName());
            }


                set1.setDrawIcons(false);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            LineData data = new LineData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(tfLight);
            /*  data.setBarWidth(0.9f);*/

            chart.setData(data);
        }
    }

    public void initialiseGraphique(Graphique typeGraphique) {

        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(10);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);
        // chart.setDrawYLabels(false);

        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(tfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(5);
        xAxis.setValueFormatter(xAxisFormatter);
        ValueFormatter custom = null;
        if (typeGraphique == Graphique.Weight) {
            custom = new MyValueFormatter(" "+Graphique.Weight.getUnite());
        } else if (typeGraphique == Graphique.Size) {
            custom = new MyValueFormatter(" "+Graphique.Size.getUnite());
        } else if (typeGraphique == Graphique.Imc) {
            custom = new MyValueFormatter(" "+Graphique.Imc.getUnite());
        }


        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        if (typeGraphique == Graphique.Weight) {
            leftAxis.setAxisMinimum(trouverDizaine(poidsMin));
        } else if (typeGraphique == Graphique.Size) {
            leftAxis.setAxisMinimum(trouverDizaine(tailleMin));
        } else if (typeGraphique == Graphique.Imc) {
            leftAxis.setAxisMinimum(trouverDizaine(imcMin));
        }
        //leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(tfLight);
        //rightAxis.setLabelCount(0, false);
        rightAxis.setLabelCount(0, true);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        if (typeGraphique == Graphique.Weight) {
            rightAxis.setAxisMinimum(trouverDizaine(poidsMin));
        } else if (typeGraphique == Graphique.Size) {
            rightAxis.setAxisMinimum(trouverDizaine(tailleMin));
        } else if (typeGraphique == Graphique.Imc) {
            rightAxis.setAxisMinimum(trouverDizaine(imcMin));
        }
        //rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = chart.getLegend();
    /*    l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);*/

        //chart.setDrawLegend(false);
        l.setEnabled(false);

        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart

    }

    private float trouverDizaine(float source) {
        float dizaine = 0;
        if (source > 20) {
            source = source - 10f;
            int temp = (int) source/10;
            dizaine = temp*10;
        }
        return dizaine;
    }


  /*  private ArrayList getData(){
        ArrayList<Entry> entries = new ArrayList<>();
        Float currentFloat = 0f;
        for (Profil currentProfil : listProfil) {
            entries.add(new Entry(currentFloat, currentProfil.getPoids()));
            currentFloat++;
        }
        return entries;
    }*/

    @OnClick(R.id.chipPoids)
    public void chipPoidsClick() {
        //creerGraphiquePoids();

        initialiseGraphique(Graphique.Weight);
        setData(listProfil,Graphique.Weight);
       // chart.notifyDataSetChanged();
        chart.invalidate();
        chart.setVisibility(View.VISIBLE);
        //  creerGraphique();
    }

    @OnClick(R.id.chipTaille)
    public void chipTailleClick() {
        //creerGraphiqueTaille();

        initialiseGraphique(Graphique.Size);
        setData(listProfil,Graphique.Size);
     //   chart.notifyDataSetChanged();
        chart.invalidate();
        chart.setVisibility(View.VISIBLE);
        //  creerGraphique();
    }

    @OnClick(R.id.chipImc)
    public void chipImcClick() {
        //creerGraphiqueImc();

        initialiseGraphique(Graphique.Imc);
        setData(listProfil,Graphique.Imc);
      //  chart.notifyDataSetChanged();
        chart.invalidate();
        chart.setVisibility(View.VISIBLE);
        //   creerGraphique();
    }

}
