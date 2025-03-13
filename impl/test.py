import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BingxServer {
    private static final int PORT = 8888;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Bingx 服务端已启动，监听端口: " + PORT);

            while (true) {
                // 等待客户端连接
                try (Socket socket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    System.out.println("客户端已连接: " + socket.getInetAddress());

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("收到客户端消息: " + inputLine);
                        // 回显消息给客户端
                        out.println("服务端收到: " + inputLine);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("无法启动服务端: " + e.getMessage());
        }
    }
}