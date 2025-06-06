package es.etg.smr.carreracamellos.cliente.utilidades;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogCamellos {

    public static final String FICHERO_LOG = "fichero.log";
    public static final String LOGGER = "CarreraCamellosLogger";
    public static final Logger logger = Logger.getLogger(LOGGER);
    public static boolean inicializado = false;

    static { // para iniciarlo solo una vez
        try {
            logger.setUseParentHandlers(false);

            FileHandler fh = new FileHandler(FICHERO_LOG, true);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            logger.addHandler(fh);
            logger.setLevel(Level.ALL);

            inicializado = true;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al inicializar el logger: " + e.getMessage());
        }
    }

    public static void info(String mensaje) {
        if (inicializado) {
            logger.log(Level.INFO, mensaje);
        }
    }

    public static void debug(String mensaje) {
        if (inicializado) {
            logger.log(Level.FINE, mensaje);
        }
    }

    public static void error(String mensaje, Throwable error) {
        if (inicializado) {
            logger.log(Level.SEVERE, mensaje, error);
        }
    }
}
