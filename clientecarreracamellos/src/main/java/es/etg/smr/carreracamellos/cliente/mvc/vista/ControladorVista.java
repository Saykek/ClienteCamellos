package es.etg.smr.carreracamellos.cliente.mvc.vista;

import java.io.IOException;

import es.etg.smr.carreracamellos.cliente.mvc.controlador.ControladorCliente;
import es.etg.smr.carreracamellos.cliente.utilidades.GestorCertificado;
import es.etg.smr.carreracamellos.cliente.utilidades.LogCamellos;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class ControladorVista {

    private static final String LOG_APERTURA_CERTIFICADO_ERROR = "Error al intentar abrir el PDF: %s";
    private static final String LOG_CAMELLO_NO_RECONOCIDO = "Nombre de camello no reconocido: %s";
    private static final String LOG_JUGADORES_ACTUALES = "Jugadores actuales: %s, %s";
    private static final String LOG_TEXTFIELD_OK = "TextField inicializado correctamente.";
    private static final String LOG_TEXTFIELD_FAIL = "Error al inicializar el TextField.";
    private static final String LOG_MENSAJE_RECIBIDO = "Servidor: %s";

    private static final String FORMATO_AVANCE = "%s avanza %d puntos.\n";
    private static final String NOMBRE = "Nombre requerido";
    private static final String NOMBRE_VALIDO = "Por favor, introduce tu nombre antes de continuar.";
    private static final String PUNTOS = " puntos";
    private final String RUTA_CERTIFICADO = "certificados_recibidos/certificado.pdf";
    private final int PUNTOS_MAXIMOS = 100;
    private final double PORCENTAJE = 1.0;
    private final double PORCENTAJE_MAXIMO = 100.0; // Porcentaje máximo para la barra de progreso

    private String nombreJugador1;
    private String nombreJugador2;

    private int puntosAcumuladosJugador1 = 0;
    private int puntosAcumuladosJugador2 = 0;

    @FXML
    private TextField txtNombreCliente;

    @FXML
    private Button btnCertificado;

    public void mostrarBotonCertificado(boolean esGanador) {
        if (esGanador) {
            btnCertificado.setVisible(true);
            btnCertificado.setDisable(false);
        } else {
            btnCertificado.setVisible(false);
            btnCertificado.setDisable(true);
        }
    }

    @FXML
    private void abrirCertificado(ActionEvent event) {
        try {
            GestorCertificado.abrirCertificado(RUTA_CERTIFICADO);

        } catch (IOException e) {
            LogCamellos.error(String.format(LOG_APERTURA_CERTIFICADO_ERROR, e.getMessage()), e);

        }
    }

    @FXML
    private TextField txtNombreCamello1; // icono

    @FXML
    private TextField txtNombreCamello2; // icono

    @FXML
    private Label lblProgresoCamello1;

    @FXML
    private Label lblProgresoCamello2;

    @FXML
    private ProgressBar pbCamello1;

    @FXML
    private ProgressBar pbCamello2;

    @FXML
    public void actualizarProgresoTotal(String nombre, int puntos) {
        double progreso = Math.min((double) puntos / PUNTOS_MAXIMOS, PORCENTAJE);

        Platform.runLater(() -> {
            String nombreNormalizado = nombre.trim().toLowerCase();
            String jugador1Normalizado = nombreJugador1.trim().toLowerCase();
            String jugador2Normalizado = nombreJugador2.trim().toLowerCase();

            int puntosTurno = 0;

            if (nombreNormalizado.equals(jugador1Normalizado)) {
                puntosTurno = puntos - puntosAcumuladosJugador1;
                puntosAcumuladosJugador1 = puntos;

                lblProgresoCamello1.setText(puntos + PUNTOS);
                pbCamello1.setProgress(progreso);

            } else if (nombreNormalizado.equals(jugador2Normalizado)) {
                puntosTurno = puntos - puntosAcumuladosJugador2;
                puntosAcumuladosJugador2 = puntos;

                lblProgresoCamello2.setText(puntos + PUNTOS);
                pbCamello2.setProgress(progreso);

            } else {
                LogCamellos.info(String.format(LOG_CAMELLO_NO_RECONOCIDO, nombre));
                LogCamellos.info(String.format(LOG_JUGADORES_ACTUALES, nombreJugador1, nombreJugador2));
            }

            taMensajes.appendText(String.format(FORMATO_AVANCE, nombre, puntosTurno));
        });
    }

    @FXML
    public void actualizarProgresoCamello(String nombre, int puntos) {
        Platform.runLater(() -> {
            if (txtNombreCamello1.getText().equals(nombre)) {
                lblProgresoCamello1.setText(puntos + PUNTOS);
                pbCamello1.setProgress(puntos / PORCENTAJE_MAXIMO);
            } else if (txtNombreCamello2.getText().equals(nombre)) {
                lblProgresoCamello2.setText(puntos + PUNTOS);
                pbCamello2.setProgress(puntos / PORCENTAJE_MAXIMO);
            }
        });
    }

    @FXML
    private TextArea taCamello2;

    @FXML
    private TextArea taMensajes;

    @FXML
    private ImageView imgCamello1;

    @FXML
    private ImageView imgCamello2;

    @FXML
    private Button btnConectar;

    public void deshabilitarConectar() {
        btnConectar.setDisable(true);
    }

    public void habilitarConectar() {
        btnConectar.setDisable(false);
    }

    private ControladorCliente controladorCliente;

    public void setControladorCliente(ControladorCliente controladorCliente) {
        this.controladorCliente = controladorCliente;
    }

    public void setNombreJugadores(String nombre1, String nombre2) {
        this.nombreJugador1 = nombre1;
        this.nombreJugador2 = nombre2;
        Platform.runLater(() -> {
            txtNombreCamello1.setText(nombre1);
            txtNombreCamello2.setText(nombre2);
        });

    }

    @FXML
    public void mostrarMensaje(String mensaje) {
        Platform.runLater(() -> {
            taMensajes.appendText(mensaje + "\n");
            LogCamellos.info(String.format(LOG_MENSAJE_RECIBIDO, mensaje));
        });
    }

    @FXML
    public void iniciarPartida(ActionEvent event) throws IOException {

        nombreJugador1 = txtNombreCliente.getText().trim();
        if (nombreJugador1.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(NOMBRE);
            alert.setContentText(NOMBRE_VALIDO);
            alert.showAndWait();
            return;
        }
        controladorCliente.conectarConServidor(nombreJugador1);

    }

    @FXML
    public void initialize() {
        btnCertificado.setVisible(false);
        if (txtNombreCliente != null) {
            LogCamellos.info(LOG_TEXTFIELD_OK);
        } else {
            LogCamellos.info(LOG_TEXTFIELD_FAIL);
        }
    }

}
