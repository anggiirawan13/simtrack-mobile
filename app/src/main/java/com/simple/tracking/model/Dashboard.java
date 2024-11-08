package com.simple.tracking.model;

import java.io.Serializable;

public class Dashboard implements Serializable {

    private int proses;
    private int kirim;
    private int terima;
    private int total;

    public int getProses() {
        return proses;
    }

    public void setProses(int proses) {
        this.proses = proses;
    }

    public int getKirim() {
        return kirim;
    }

    public void setKirim(int kirim) {
        this.kirim = kirim;
    }

    public int getTerima() {
        return terima;
    }

    public void setTerima(int terima) {
        this.terima = terima;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
