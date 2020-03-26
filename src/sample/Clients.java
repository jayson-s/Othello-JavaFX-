package sample;

import java.io.Serializable;
import java.util.function.Consumer;

public class Clients extends InitializeNetwork {

    private String ip;
    private int port;

    public Clients(String ip, int port, Consumer<Serializable> received){
        super(received);
        this.ip = ip;
        this.port = port;
    }

    @Override
    protected boolean isServer() {
        return false;
    }

    @Override
    protected String getIP() {
        return ip;
    }

    @Override
    protected int getPort() {
        return port;
    }
}
