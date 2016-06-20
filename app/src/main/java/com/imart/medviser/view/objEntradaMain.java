package com.imart.medviser.view;

/**
 * Created by imart on 29/05/2016.
 */
public class objEntradaMain {

    private int idMed;
    private int idToma;
    private String nombreMed;
    private String horaToma;
    private String detallesToma;
    private int estado;


    public objEntradaMain() {}

    public objEntradaMain(int idMed, int idToma, String nombreMed, String horaToma, String detallesToma) {
        this.idMed = idMed;
        this.idToma = idToma;
        this.nombreMed = nombreMed;
        this.horaToma = horaToma;
        this.detallesToma = detallesToma;
    }

    public int getIdMed() {
        return idMed;
    }

    public void setIdMed(int idMed) {
        this.idMed = idMed;
    }

    public int getIdToma() {
        return idToma;
    }

    public void setIdToma(int idToma) {
        this.idToma = idToma;
    }

    public String getNombreMed() {
        return nombreMed;
    }

    public void setNombreMed(String nombreMed) {
        this.nombreMed = nombreMed;
    }

    public String getHoraToma() {
        return horaToma;
    }

    public void setHoraToma(String horaToma) {
        this.horaToma = horaToma;
    }

    public String getDetallesToma() {
        return detallesToma;
    }

    public void setDetallesToma(String detallesToma) {
        this.detallesToma = detallesToma;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
