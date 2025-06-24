package controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.User;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    private User loggedInUser;

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        welcomeLabel.setText("Selamat datang, " + user.getUsername() + "!");
    }

    @FXML
    private void handleOpenPasien() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PasienView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Data Pasien");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOpenDokter() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DokterView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Data Dokter");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOpenRekamMedis() {
        // kode buka window/data rekam medis
        // misal buka fxml rekam medis:
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RekamMedisView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Data Rekam Medis");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Logout");
        alert.setHeaderText(null);
        alert.setContentText("Apakah Anda yakin ingin logout?");

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
                    Scene loginScene = new Scene(loader.load());
                    Stage stage = (Stage) welcomeLabel.getScene().getWindow(); // ambil stage dari komponen mana saja, seperti welcomeLabel
                    stage.setScene(loginScene);
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Gagal memuat halaman login.");
                }
            }
        });
    }
    
    private void showAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}


}
