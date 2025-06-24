package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import models.RekamMedis;
import models.Pasien;
import models.Dokter;
import service.DatabaseService;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RekamMedisController implements BaseController<RekamMedis> {

    @FXML
    private TableView<RekamMedis> tableRekamMedis;

    @FXML
    private TableColumn<RekamMedis, Integer> colId;

    @FXML
    private TableColumn<RekamMedis, String> colPasien;

    @FXML
    private TableColumn<RekamMedis, String> colDokter;

    @FXML
    private TableColumn<RekamMedis, String> colDiagnosa;

    @FXML
    private TableColumn<RekamMedis, String> colTindakan;

    @FXML
    private TableColumn<RekamMedis, LocalDate> colTanggal;

    @FXML
    private ComboBox<Pasien> cmbPasien;

    @FXML
    private ComboBox<Dokter> cmbDokter;

    @FXML
    private TextField txtDiagnosa;

    @FXML
    private TextField txtTindakan;

    @FXML
    private DatePicker dpTanggal;

    @FXML
    private Button btnTambah;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnHapus;

    @FXML
    private Button btnClear;

    private ObservableList<RekamMedis> rekamMedisList;

    private ObservableList<Pasien> pasienList;

    private ObservableList<Dokter> dokterList;

    @FXML
    public void initialize() {
        // 1. Load data pasien dan dokter dulu supaya pasienList dan dokterList sudah ada
        loadPasien();
        loadDokter();

        // 2. Setup TableColumn cell value factories
        // Kolom ID (integer)
        colId.setCellValueFactory(data
                -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());

        // Kolom diagnosa, tindakan (string)
        colDiagnosa.setCellValueFactory(data
                -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDiagnosa()));
        colTindakan.setCellValueFactory(data
                -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTindakan()));

        // Kolom tanggal (LocalDate)
        colTanggal.setCellValueFactory(data
                -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTanggal()));

        // Kolom pasien dan dokter yang menampilkan nama, bukan id
        colPasien.setCellValueFactory(data -> {
            Pasien p = getPasienById(data.getValue().getPasienId());
            return new javafx.beans.property.SimpleStringProperty(p != null ? p.getNama() : "-");
        });
        colDokter.setCellValueFactory(data -> {
            Dokter d = getDokterById(data.getValue().getDokterId());
            return new javafx.beans.property.SimpleStringProperty(d != null ? d.getNama() : "-");
        });

        // 3. Load data rekam medis dari database ke TableView
        loadData();

        // 4. Setup ComboBox converter supaya ComboBox pasien dan dokter tampil nama
        cmbPasien.setConverter(new StringConverter<Pasien>() {
            @Override
            public String toString(Pasien p) {
                return p != null ? p.getNama() : "";
            }

            @Override
            public Pasien fromString(String string) {
                return null; // Tidak digunakan karena editable = false
            }
        });

        cmbDokter.setConverter(new StringConverter<Dokter>() {
            @Override
            public String toString(Dokter d) {
                return d != null ? d.getNama() : "";
            }

            @Override
            public Dokter fromString(String string) {
                return null;
            }
        });

        // 5. Listener saat baris di tabel dipilih, untuk mengisi form input
        tableRekamMedis.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                cmbPasien.getSelectionModel().select(getPasienById(newSel.getPasienId()));
                cmbDokter.getSelectionModel().select(getDokterById(newSel.getDokterId()));
                txtDiagnosa.setText(newSel.getDiagnosa());
                txtTindakan.setText(newSel.getTindakan());
                dpTanggal.setValue(newSel.getTanggal());
            } else {
                // Jika tidak ada baris yang dipilih, bisa clear form (opsional)
                clearForm();
            }
        });
    }

    private void loadPasien() {
        List<Pasien> list = new ArrayList<>();
        String sql = "SELECT * FROM pasien";
        try (Connection conn = DatabaseService.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Pasien(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getInt("umur"),
                        rs.getString("alamat"),
                        rs.getString("no_hp")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pasienList = FXCollections.observableArrayList(list);
        cmbPasien.setItems(pasienList);
    }

    private void loadDokter() {
        List<Dokter> list = new ArrayList<>();
        String sql = "SELECT * FROM dokter";
        try (Connection conn = DatabaseService.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Dokter(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getString("spesialis")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dokterList = FXCollections.observableArrayList(list);
        cmbDokter.setItems(dokterList);
    }

    private Pasien getPasienById(int id) {
        for (Pasien p : pasienList) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    private Dokter getDokterById(int id) {
        for (Dokter d : dokterList) {
            if (d.getId() == id) {
                return d;
            }
        }
        return null;
    }

    private void loadData() {
        rekamMedisList = FXCollections.observableArrayList(getAll());
        tableRekamMedis.setItems(rekamMedisList);
    }

    @FXML
    private void onTambahClicked() {
        if (!validasiForm()) {
            return;
        }

        RekamMedis rm = new RekamMedis(0,
                cmbPasien.getSelectionModel().getSelectedItem().getId(),
                cmbDokter.getSelectionModel().getSelectedItem().getId(),
                txtDiagnosa.getText(),
                txtTindakan.getText(),
                dpTanggal.getValue()
        );

        insert(rm);
        loadData();
        clearForm();
    }

    @FXML
    private void onEditClicked() {
        RekamMedis selected = tableRekamMedis.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih data", "Silakan pilih data untuk diedit.");
            return;
        }
        if (!validasiForm()) {
            return;
        }

        selected.setPasienId(cmbPasien.getSelectionModel().getSelectedItem().getId());
        selected.setDokterId(cmbDokter.getSelectionModel().getSelectedItem().getId());
        selected.setDiagnosa(txtDiagnosa.getText());
        selected.setTindakan(txtTindakan.getText());
        selected.setTanggal(dpTanggal.getValue());

        update(selected);
        loadData();
        clearForm();
    }

    @FXML
    private void onHapusClicked() {
        RekamMedis selected = tableRekamMedis.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih data", "Silakan pilih data untuk dihapus.");
            return;
        }

        Alert konfirmasi = new Alert(Alert.AlertType.CONFIRMATION);
        konfirmasi.setTitle("Konfirmasi");
        konfirmasi.setHeaderText(null);
        konfirmasi.setContentText("Yakin ingin menghapus data rekam medis ID " + selected.getId() + "?");

        konfirmasi.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                delete(selected.getId());
                loadData();
                clearForm();
            }
        });
    }

    @FXML
    private void onClearClicked() {
        clearForm();
    }

    private void clearForm() {
        cmbPasien.getSelectionModel().clearSelection();
        cmbDokter.getSelectionModel().clearSelection();
        txtDiagnosa.clear();
        txtTindakan.clear();
        dpTanggal.setValue(null);
        tableRekamMedis.getSelectionModel().clearSelection();
    }

    private boolean validasiForm() {
        if (cmbPasien.getSelectionModel().getSelectedItem() == null
                || cmbDokter.getSelectionModel().getSelectedItem() == null
                || txtDiagnosa.getText().isEmpty()
                || txtTindakan.getText().isEmpty()
                || dpTanggal.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validasi", "Semua form harus diisi!");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Implement BaseController interface methods
    @Override
    public List<RekamMedis> getAll() {
        List<RekamMedis> list = new ArrayList<>();
        String sql = "SELECT * FROM rekam_medis";
        try (Connection conn = DatabaseService.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new RekamMedis(
                        rs.getInt("id"),
                        rs.getInt("pasien_id"),
                        rs.getInt("dokter_id"),
                        rs.getString("diagnosa"),
                        rs.getString("tindakan"),
                        rs.getDate("tanggal").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void insert(RekamMedis r) {
        String sql = "INSERT INTO rekam_medis (pasien_id, dokter_id, diagnosa, tindakan, tanggal) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseService.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getPasienId());
            ps.setInt(2, r.getDokterId());
            ps.setString(3, r.getDiagnosa());
            ps.setString(4, r.getTindakan());
            ps.setDate(5, Date.valueOf(r.getTanggal()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal tambah data rekam medis");
        }
    }

    @Override
    public void update(RekamMedis r) {
        String sql = "UPDATE rekam_medis SET pasien_id=?, dokter_id=?, diagnosa=?, tindakan=?, tanggal=? WHERE id=?";
        try (Connection conn = DatabaseService.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getPasienId());
            ps.setInt(2, r.getDokterId());
            ps.setString(3, r.getDiagnosa());
            ps.setString(4, r.getTindakan());
            ps.setDate(5, Date.valueOf(r.getTanggal()));
            ps.setInt(6, r.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal update data rekam medis");
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM rekam_medis WHERE id = ?";
        try (Connection conn = DatabaseService.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal hapus data rekam medis");
        }
    }
}
