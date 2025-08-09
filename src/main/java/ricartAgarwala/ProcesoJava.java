package ricartAgarwala;

import java.io.*;
import java.net.Socket;
import org.json.JSONObject;

public class ProcesoJava {
    static int reloj = 0;
    static int id = 0;

    public static void main(String[] args) {
        try (Socket socket = new Socket("161.132.51.124", 5001)) {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter salida = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Enviar solicitud
            reloj++;
            JSONObject solicitud = new JSONObject();
            solicitud.put("tipo", "SOLICITUD");
            solicitud.put("idProceso", id);
            solicitud.put("marcaTiempo", reloj);
            salida.write(solicitud.toString());
            salida.flush();
            System.out.println("[P" + id + "] Enviado: " + solicitud);

            // Esperar respuesta
            char[] buffer = new char[1024];
            int leido = entrada.read(buffer);
            String recibido = new String(buffer, 0, leido);
            JSONObject respuesta = new JSONObject(recibido);
            int tsRespuesta = respuesta.getInt("marcaTiempo");
            reloj = Math.max(reloj, tsRespuesta) + 1;

            System.out.println("[P" + id + "] Recibido OK → Nuevo reloj: " + reloj);
            System.out.println("[P" + id + "] 🔒 ENTRA al recurso crítico");
            Thread.sleep(2000);
            System.out.println("[P" + id + "] 🔓 SALE del recurso crítico");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
