import connector.HttpConnector;

public class Start {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        String host="0.0.0.0";
        int port = 8080;
        try (HttpConnector connector = new HttpConnector(host, port)) {
            for (;;) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
