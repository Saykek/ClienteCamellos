package es.etg.smr.carreracamellos.cliente.utilidades;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ConexionServidor {
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private DataInputStream entradaDatos; // Para recibir PDF

    public ConexionServidor(Socket socket, InputStream inputStream, OutputStream outputStream) {
        this.socket = socket;
        this.entradaDatos = new DataInputStream(inputStream);
        this.entrada = new BufferedReader(new InputStreamReader(inputStream));
        this.salida = new PrintWriter(outputStream, true);
    }

    public BufferedReader getEntrada() {
        return entrada;
    }

    public DataInputStream getEntradaDatos() {
        return entradaDatos;
    }

    public PrintWriter getSalida() {
        return salida;
    }

    public void cerrar() {
        try {
            if (entradaDatos != null)
                entradaDatos.close();
            if (entrada != null)
                entrada.close();
            if (salida != null)
                salida.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

}
