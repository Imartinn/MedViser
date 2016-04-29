package com.example.imart.medviser.model;

/**
 * Created by Imartinn on 29/04/16.
 */
public class ObjMed {

    private long idMed;
    private String nombre;
    private String detalles;
    private boolean enActivo;

    public ObjMed() { }

    public ObjMed(long idMed, String nombre, String detalles, boolean enActivo) {
        this.idMed = idMed;
        this.nombre = nombre;
        this.detalles = detalles;
        this.enActivo = enActivo;
    }

    public long getIdMed() {
        return idMed;
    }

    public void setIdMed(long idMed) {
        this.idMed = idMed;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public boolean isEnActivo() {
        return enActivo;
    }

    public void setEnActivo(boolean enActivo) {
        this.enActivo = enActivo;
    }
}
