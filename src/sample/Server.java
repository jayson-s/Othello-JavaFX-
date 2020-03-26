package sample;
import java.io.Serializable;
import java.util.function.Consumer;

public class Server extends InitializeNetwork {

    private int port;

    Server(int port, Consumer<Serializable> received){
        super(received);
        this.port = port;
    }

    @Override
    protected boolean isServer() {
        return true;
    }

    @Override
    protected String getIP() {
        return null;
    }

    @Override
    protected int getPort() {
        return port;
    }
}

