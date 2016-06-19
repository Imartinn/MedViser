package com.example.imart.medviser.model;

/**
 * Created by imart on 18/06/2016.
 */
public class ObjReg {

    private int idReg;
    private int idMed;
    private int idToma;
    private String horaToma;
    private long fechaReg;
    private int estado;


    public ObjReg() {
    }

    public ObjReg(int idReg, int idMed, int idToma, String horaToma, long fechaReg, int estado) {
        this.idReg = idReg;
        this.idMed = idMed;
        this.idToma = idToma;
        this.horaToma = horaToma;
        this.fechaReg = fechaReg;
        this.estado = estado;
    }

    public int getIdReg() {
        return idReg;
    }

    public void setIdReg(int idReg) {
        this.idReg = idReg;
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

    public String getHoraToma() {
        return horaToma;
    }

    public void setHoraToma(String horaToma) {
        this.horaToma = horaToma;
    }

    public long getFechaReg() {
        return fechaReg;
    }

    public void setFechaReg(long fechaReg) {
        this.fechaReg = fechaReg;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
