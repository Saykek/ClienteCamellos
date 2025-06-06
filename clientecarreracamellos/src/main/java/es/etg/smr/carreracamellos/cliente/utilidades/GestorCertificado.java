package es.etg.smr.carreracamellos.cliente.utilidades;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GestorCertificado {

    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
    private static final String OS_MAC = "mac";
    private static final String OS_LINUX_1 = "nix";
    private static final String OS_LINUX_2 = "nux";
    private static final String OS_WINDOWS = "win";
    private static final String COMANDO_MAC = "open";
    private static final String COMANDO_LINUX = "xdg-open";
    private static final String COMANDO_WINDOWS = "cmd";
    private static final String CMD_FLAG_EJECUTAR = "/c";
    private static final String CMD_COMANDO_START = "start";
    private static final String CMD_VENTANA_TITULO = "";

    private static final String LOG_CERTIFICADO_NO_EXISTE = "El archivo no existe: %s";

    private static final String CARPETA_CERTIFICADOS = "certificados_recibidos";
    private static final String NOMBRE_CERTIFICADO = "certificado.pdf";
    private static final String MJ_RECEPCION_CERTIFICADO = "Recibiendo certificado PDF del servidor...";
    private static final String MJ_CERTIFICADO_OK = "Certificado PDF recibido correctamente. Guardando en disco...";
    private static final String FORMATO_TAMANIO_CERTIFICADO = "Longitud del certificado PDF: %s";
    private static final String FORMATO_UBICACION_CERTIFICADO = "Certificado PDF guardado en:  %s";
    private static final String FORMATO_ERROR_GUARDAR_CERTIFICADO = "Error al guardar el certificado PDF: %s";
    private static final String DIRECTORIO_ACTUAL = System.getProperty("user.dir");

    public static void abrirCertificado(String ruta) throws IOException {

        File pdfFile = new File(ruta);

        if (!pdfFile.exists()) {
            LogCamellos.info(LOG_CERTIFICADO_NO_EXISTE + pdfFile.getAbsolutePath());
            return;
        }
        // Para macOS
        if (OS_NAME.contains(OS_MAC)) {
            new ProcessBuilder(COMANDO_MAC, pdfFile.getAbsolutePath()).start();

            // Para Linux
        } else if (OS_NAME.contains(OS_LINUX_1) ||
                OS_NAME.contains(OS_LINUX_2)) {
            new ProcessBuilder(COMANDO_LINUX, pdfFile.getAbsolutePath()).start();

            // Para Windows
        } else if (OS_NAME.contains(OS_WINDOWS)) {
            new ProcessBuilder(COMANDO_WINDOWS, CMD_FLAG_EJECUTAR, CMD_COMANDO_START, CMD_VENTANA_TITULO,
                    pdfFile.getAbsolutePath()).start();
        }
    }

    public static void recibirCertificado(ConexionServidor conexion) throws IOException {

        LogCamellos.info(MJ_RECEPCION_CERTIFICADO);
        int longitud = conexion.getEntradaDatos().readInt(); // LEO EL TAMAÑO
        LogCamellos.info(String.format(FORMATO_TAMANIO_CERTIFICADO, longitud));

        byte[] datosPdf = new byte[longitud]; // leo los bytes
        conexion.getEntradaDatos().readFully(datosPdf); // leo los bytes

        LogCamellos.info(MJ_CERTIFICADO_OK);

        String rutaBase = DIRECTORIO_ACTUAL + File.separator + CARPETA_CERTIFICADOS; // Obtengo la ruta
                                                                                     // base del proyecto
        File carpeta = new File(rutaBase);
        if (!carpeta.exists()) {
            carpeta.mkdir(); // Creo la carpeta si no existe
        }
        File archivoPdf = new File(carpeta, NOMBRE_CERTIFICADO); // Nombre del archivo PDF

        try (FileOutputStream flujo = new FileOutputStream(archivoPdf)) {
            flujo.write(datosPdf); // Escribo los bytes en el archivo
            LogCamellos.info(String.format(FORMATO_UBICACION_CERTIFICADO, archivoPdf.getAbsolutePath()));
        } catch (IOException e) {
            LogCamellos.error(String.format(FORMATO_ERROR_GUARDAR_CERTIFICADO + e.getMessage()), e);

        }
    }
}
