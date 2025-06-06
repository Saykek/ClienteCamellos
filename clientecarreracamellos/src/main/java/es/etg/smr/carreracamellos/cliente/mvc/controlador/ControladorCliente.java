package es.etg.smr.carreracamellos.cliente.mvc.controlador;

import java.io.IOException;

import es.etg.smr.carreracamellos.Main;
import es.etg.smr.carreracamellos.cliente.mvc.modelo.Cliente;
import es.etg.smr.carreracamellos.cliente.mvc.vista.ControladorVista;
import es.etg.smr.carreracamellos.cliente.utilidades.LogCamellos;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ControladorCliente extends Application {

    private final static String MJ_INICIO = "Iniciando interfaz de usuario...";
    private final static String MJ_ERROR = "Error de conexión ";
    private final static String FORMATO_CARGAR_VISTA = "Cargando vista: %s";
    private final static String FORMATO_CONEXION_SERVIDOR = "Conectando al servidor como:  %s";
    private final static String FORMATO_MJ_RECIBIDO = "Mensaje recibido tras conexión:  %s";
    private final static String FORMATO_ERROR_CONEXION = "Error al conectar con el servidor:  %s";

    private final static String VISTA = "/es/etg/smr/carreracamellos/cliente/vista/pantallaPrincipal.fxml";

    private Scene scene;
    private final Cliente cliente;
    private ControladorVista controladorVista;

    @Override
    public void start(Stage stage) throws IOException {

        LogCamellos.info(MJ_INICIO);

        stage.setScene(cargarVista(VISTA));// Cargo la vista principal
        stage.show();
    }

    private Scene cargarVista(String ficheroView) throws IOException {
        LogCamellos.info(String.format(FORMATO_CARGAR_VISTA, ficheroView));
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(ficheroView));
        Parent root = (Parent) fxmlLoader.load();

        // Obtengo el controlador de la vista para pasarle una referencia al controlador
        controladorVista = fxmlLoader.<ControladorVista>getController();
        controladorVista.setControladorCliente(this);

        cliente.setControladorVista(controladorVista);
        scene = new Scene(root);

        return scene;
    }

    // Constructor con valores por defecto (localhost:3009)
    public ControladorCliente() throws IOException {
        cliente = new Cliente();
    }

    // Constructor personalizado (para usar otro puerto)
    public ControladorCliente(String host, int puerto) {
        cliente = new Cliente(host, puerto);
    }

    // conecto con el servidor y enviar el nombre del jugador
    public String conectarConServidor(String nombreJugador) {
        try {
            LogCamellos.info(String.format(FORMATO_CONEXION_SERVIDOR, nombreJugador));
            cliente.conectar(nombreJugador);
            cliente.enviarNombre(nombreJugador);
            String respuesta = cliente.recibirMensaje();
            LogCamellos.info(String.format(FORMATO_MJ_RECIBIDO, respuesta));
            return respuesta;
        } catch (IOException e) {
            LogCamellos.error(String.format(FORMATO_ERROR_CONEXION + e.getMessage()), e);
            return MJ_ERROR + e.getMessage();
        }
    }

}
