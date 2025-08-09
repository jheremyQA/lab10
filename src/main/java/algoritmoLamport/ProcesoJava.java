package algoritmoLamport;

import java.io.*;
import java.net.*;
import org.json.JSONObject;

public class ProcesoJava {
    private static int reloj = 0;
    private static final int ID = 0;

    public static void main(String[] args) {
        try (Socket socket = new Socket("161.132.51.124", 5001)) {
            System.out.println("[P" + ID + "] Conectado al servidor...");

            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter salida = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Simula envío de una solicitud
            reloj++;
            JSONObject mensaje = new JSONObject();
            mensaje.put("tipo", "SOLICITUD");
            mensaje.put("idProceso", ID);
            mensaje.put("marcaTiempo", reloj);

            salida.write(mensaje.toString());
            salida.flush();
            System.out.println("[P" + ID + "] Enviado: " + mensaje);

            // Espera respuesta
            char[] buffer = new char[1024];
            int leido = entrada.read(buffer);
            String respuestaJson = new String(buffer, 0, leido);
            JSONObject respuesta = new JSONObject(respuestaJson);

            int tsRespuesta = respuesta.getInt("marcaTiempo");
            reloj = Math.max(reloj, tsRespuesta) + 1;

            System.out.println("[P" + ID + "] Recibido OK de P" + respuesta.getInt("idProceso") +
                               " | TS: " + tsRespuesta + " → Nuevo reloj: " + reloj);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
