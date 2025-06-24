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
public class Dokter extends BaseModel {

    private int id;
    private String nama;
    private String spesialis;

    public Dokter(int id, String nama, String spesialis) {
        this.id = id;
        this.nama = nama;
        this.spesialis = spesialis;
    }

    public Dokter(String nama, String spesialis) {
        this(0, nama, spesialis);
    }

    @Override
    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getSpesialis() {
        return spesialis;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setSpesialis(String spesialis) {
        this.spesialis = spesialis;
    }

    @Override
    public String toString() {
        return this.nama;
    }

}
 