import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.KeyStore;
import java.security.KeyStoreException;

public class Main {
    public static void main(String[] args) throws Exception {

        String keystorePath = System.getProperty("keystore", System.getProperty("user.home") + "/keystore.jks");
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream(keystorePath), "123456789".toCharArray());

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(trustStore, "123456789".toCharArray());

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(keyManagerFactory.getKeyManagers(), null, null);

        SSLServerSocketFactory sslServerSocketFactory = context.getServerSocketFactory();

        try(SSLServerSocket serverSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(8080)){
            System.out.println("El Servidor de máxima seguridad está escuchando a tope...");
            SSLSocket client = (SSLSocket) serverSocket.accept();

            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            PrintWriter writer = new PrintWriter(client.getOutputStream(), true);

            String message = reader.readLine();
            System.out.println("Se ha recibido del cliente: " + message);

            writer.println("Hola cliente, nadie nos escucha ;)");

            reader.close();
            writer.close();
            client.close();
        }



    }
}