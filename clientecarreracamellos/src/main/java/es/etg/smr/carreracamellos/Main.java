package es.etg.smr.carreracamellos;

import java.io.IOException;

import es.etg.smr.carreracamellos.cliente.mvc.controlador.ControladorCliente;
import javafx.application.Application;

public class Main {
    public static void main(String[] args)throws IOException {
        
        ControladorCliente controlador = new ControladorCliente();

        Application.launch(ControladorCliente.class, args);
    }
}