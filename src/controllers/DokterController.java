package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import models.Dokter;
import service.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DokterController {

    @FXML
    private TableView<Dokter> tableDokter;

    @FXML
    private TableColumn<Dokter, Integer> colId;

    @FXML
    private TableColumn<Dokter, String> colNama;

    @FXML
    private TableColumn<Dokter, String> colSpesialis;

    @FXML
    private TextField txtNama;

    @FXML
    private TextField txtSpesialis;

    @FXML
    private Button btnTambah;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnHapus;

    @FXML
    private Button btnClear;

    private ObservableList<Dokter> dokterList;

    // Inisialisasi controller, dipanggil otomatis oleh FXMLLoader
    @FXML
    public void initialize() {
        // Set cell value factories untuk kolom tabel
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        colNama.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNama()));
        colSpesialis.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSpesialis()));

        loadData();

        // Listener untuk pilih baris tabel, isi form input
        tableDokter.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtNama.setText(newSelection.getNama());
                txtSpesialis.setText(newSelection.getSpesialis());
            }
        });
    }

    private void loadData() {
        List<Dokter> list = getAll();
        dokterList = FXCollections.observableArrayList(list);
        tableDokter.setItems(dokterList);
    }

    // Ambil semua data dokter dari database
    public List<Dokter> getAll() {
        try (Connection conn = DatabaseService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM dokter")) {

            List<Dokter> list = FXCollections.observableArrayList();
            while (rs.next()) {
                list.add(new Dokter(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getString("spesialis")
                ));
            }
            return list;
        }  catch (SQLException e) {
    e.printStackTrace();
    return new ArrayList<>();
}
}
    

    // Event handler untuk tombol Tambah
    @FXML
    private void onTambahClicked() {
        String nama = txtNama.getText().trim();
        String spesialis = txtSpesialis.getText().trim();

        if (nama.isEmpty() || spesialis.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validasi", "Nama dan Spesialis harus diisi!");
            return;
        }

        Dokter dokterBaru = new Dokter(nama, spesialis);
        insert(dokterBaru);
        loadData();
        clearForm();
    }

    // Event handler untuk tombol Edit
    @FXML
    private void onEditClicked() {
        Dokter selected = tableDokter.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih Data", "Silakan pilih data dokter yang akan diedit.");
            return;
        }

        String nama = txtNama.getText().trim();
        String spesialis = txtSpesialis.getText().trim();

        if (nama.isEmpty() || spesialis.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validasi", "Nama dan Spesialis harus diisi!");
            return;
        }

        selected.setNama(nama);
        selected.setSpesialis(spesialis);

        update(selected);
        loadData();
        clearForm();
    }

    // Event handler untuk tombol Hapus
    @FXML
    private void onHapusClicked() {
        Dokter selected = tableDokter.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih Data", "Silakan pilih data dokter yang akan dihapus.");
            return;
        }

        Alert konfirmasi = new Alert(Alert.AlertType.CONFIRMATION);
        konfirmasi.setTitle("Konfirmasi Hapus");
        konfirmasi.setHeaderText(null);
        konfirmasi.setContentText("Yakin ingin menghapus dokter " + selected.getNama() + "?");

        konfirmasi.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                delete(selected.getId());
                loadData();
                clearForm();
            }
        });
    }

    // Event handler untuk tombol Bersihkan form
    @FXML
    private void onClearClicked() {
        clearForm();
    }

    private void clearForm() {
        txtNama.clear();
        txtSpesialis.clear();
        tableDokter.getSelectionModel().clearSelection();
    }

    // Method CRUD yang terhubung ke DatabaseService

    private void insert(Dokter d) {
        try (PreparedStatement ps = DatabaseService.getConnection().prepareStatement(
                "INSERT INTO dokter (nama, spesialis) VALUES (?, ?)")) {
            ps.setString(1, d.getNama());
            ps.setString(2, d.getSpesialis());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal menambah data dokter.");
        }
    }

    private void update(Dokter d) {
        try (PreparedStatement ps = DatabaseService.getConnection().prepareStatement(
                "UPDATE dokter SET nama = ?, spesialis = ? WHERE id = ?")) {
            ps.setString(1, d.getNama());
            ps.setString(2, d.getSpesialis());
            ps.setInt(3, d.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal mengupdate data dokter.");
        }
    }

    private void delete(int id) {
        try (PreparedStatement ps = DatabaseService.getConnection().prepareStatement(
                "DELETE FROM dokter WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
                if (e.getErrorCode() == 1451) { // Foreign key constraint fail (contoh)
                    showAlert1("Data tidak bisa dihapus karena masih terkait dengan rekam medis.");
                } else {
                    e.printStackTrace();
                }
            }
    }

    // Utility untuk menampilkan alert
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
     private void showAlert1(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Kesalahan");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
