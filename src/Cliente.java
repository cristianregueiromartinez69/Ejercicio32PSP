import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.KeyStore;

public class Cliente {

    public static void main(String[] args) throws Exception {

        String keystorePath = System.getProperty("keystore", System.getProperty("user.home") + "/keystore.jks");
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream(keystorePath), "123456789".toCharArray());


        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

        SSLSocketFactory socketFactory = sslContext.getSocketFactory();
        SSLSocket socket = (SSLSocket) socketFactory.createSocket("localhost", 8080);

        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        writer.println("Hola desde el cliente más seguro en la faz de la tierra");

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String respuesta = reader.readLine();
        System.out.println("Se ha recibido el siguiente mensaje del servidor " + respuesta);

        writer.close();
        reader.close();
        socket.close();
    }
}
