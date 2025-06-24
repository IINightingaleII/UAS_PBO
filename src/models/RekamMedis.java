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


import java.time.LocalDate;

public class RekamMedis extends BaseModel {
    private int id;
    private int pasienId;
    private int dokterId;
    private String diagnosa;
    private String tindakan;
    private LocalDate tanggal;

    public RekamMedis(int id, int pasienId, int dokterId, String diagnosa, String tindakan, LocalDate tanggal) {
        this.id = id;
        this.pasienId = pasienId;
        this.dokterId = dokterId;
        this.diagnosa = diagnosa;
        this.tindakan = tindakan;
        this.tanggal = tanggal;
    }

    @Override
    public int getId() { return id; }
    public int getPasienId() { return pasienId; }
    public int getDokterId() { return dokterId; }
    public String getDiagnosa() { return diagnosa; }
    public String getTindakan() { return tindakan; }
    public LocalDate getTanggal() { return tanggal; }

    public void setId(int id) { this.id = id; }
    public void setPasienId(int pasienId) { this.pasienId = pasienId; }
    public void setDokterId(int dokterId) { this.dokterId = dokterId; }
    public void setDiagnosa(String diagnosa) { this.diagnosa = diagnosa; }
    public void setTindakan(String tindakan) { this.tindakan = tindakan; }
    public void setTanggal(LocalDate tanggal) { this.tanggal = tanggal; }
}

