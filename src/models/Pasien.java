/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author hp
 */
public class Pasien extends BaseModel {

    private int id;
    private String nama;
    private int umur;
    private String alamat;
    private String noHP;

    public Pasien(int id, String nama, int umur, String alamat, String noHP) {
        this.id = id;
        this.nama = nama;
        this.umur = umur;
        this.alamat = alamat;
        this.noHP = noHP;
    }

    public Pasien(String nama, int umur, String alamat, String noHP) {
        this(0, nama, umur, alamat, noHP);
    }

    @Override
    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public int getUmur() {
        return umur;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getNoHP() {
        return noHP;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setUmur(int umur) {
        this.umur = umur;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setNoHP(String noHP) {
        this.noHP = noHP;
    }

    @Override
    public String toString() {
        return this.nama; 
    }
}
