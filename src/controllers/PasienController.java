package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import models.Pasien;
import service.DatabaseService;

import java.sql.*;

public class PasienController {

    @FXML private TableView<Pasien> pasienTable;
    @FXML private TableColumn<Pasien, Integer> colId;
    @FXML private TableColumn<Pasien, String> colNama;
    @FXML private TableColumn<Pasien, Integer> colUmur;
    @FXML private TableColumn<Pasien, String> colAlamat;
    @FXML private TableColumn<Pasien, String> colNoHP;

    @FXML private TextField namaField;
    @FXML private TextField umurField;
    @FXML private TextField alamatField;
    @FXML private TextField noHPField;

    private final ObservableList<Pasien> pasienList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        colNama.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNama()));
        colUmur.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getUmur()).asObject());
        colAlamat.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAlamat()));
        colNoHP.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNoHP()));

        loadPasienFromDatabase();
    pasienTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
    if (newSelection != null) {
        namaField.setText(newSelection.getNama());
        umurField.setText(String.valueOf(newSelection.getUmur()));
        alamatField.setText(newSelection.getAlamat());
        noHPField.setText(newSelection.getNoHP());
    }
});

    }
    
    

    private void loadPasienFromDatabase() {
        pasienList.clear();
        try (Connection conn = DatabaseService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM pasien")) {
            while (rs.next()) {
                Pasien p = new Pasien(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getInt("umur"),
                        rs.getString("alamat"),
                        rs.getString("no_hp")
                );
                pasienList.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pasienTable.setItems(pasienList);
    }

    @FXML
    private void handleAdd() {
        try {
            String nama = namaField.getText();
            int umur = Integer.parseInt(umurField.getText());
            String alamat = alamatField.getText();
            String noHP = noHPField.getText();

            try (Connection conn = DatabaseService.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "INSERT INTO pasien (nama, umur, alamat, no_hp) VALUES (?, ?, ?, ?)")) {
                ps.setString(1, nama);
                ps.setInt(2, umur);
                ps.setString(3, alamat);
                ps.setString(4, noHP);
                ps.executeUpdate();
            }

            loadPasienFromDatabase();
            clearForm();

        } catch (Exception e) {
            showAlert("Input tidak valid atau gagal menyimpan ke database.");
        }
    }

    @FXML
    private void handleDelete() {
        Pasien selected = pasienTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try (Connection conn = DatabaseService.getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM pasien WHERE id = ?")) {
                ps.setInt(1, selected.getId());
                ps.executeUpdate();
                loadPasienFromDatabase();
                clearForm();
            } catch (SQLException e) {
                if (e.getErrorCode() == 1451) {
                    showAlert("Data tidak bisa dihapus karena terkait dengan rekam medis.");
                } else {
                    e.printStackTrace();
                }
            }
        }
    }
    @FXML
private void handleUpdate() {
    Pasien selected = pasienTable.getSelectionModel().getSelectedItem();
    if (selected != null) {
        try {
            String nama = namaField.getText();
            int umur = Integer.parseInt(umurField.getText());
            String alamat = alamatField.getText();
            String noHP = noHPField.getText();

            try (Connection conn = DatabaseService.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "UPDATE pasien SET nama = ?, umur = ?, alamat = ?, no_hp = ? WHERE id = ?")) {
                ps.setString(1, nama);
                ps.setInt(2, umur);
                ps.setString(3, alamat);
                ps.setString(4, noHP);
                ps.setInt(5, selected.getId());
                ps.executeUpdate();
            }

            loadPasienFromDatabase();
            clearForm();
        } catch (Exception e) {
            showAlert("Gagal mengupdate data. Pastikan semua input valid.");
        }
    } else {
        showAlert("Silakan pilih pasien yang ingin diperbarui.");
    }
}


@FXML
private void clearForm() {
    namaField.clear();
    umurField.clear();
    alamatField.clear();
    noHPField.clear();
    pasienTable.getSelectionModel().clearSelection();
}


    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Kesalahan");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
