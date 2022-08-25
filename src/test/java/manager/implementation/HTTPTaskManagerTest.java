package manager.implementation;

import manager.servers.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.URI;

class HTTPTaskManagerTest extends TaskManagerTest<HTTPTaskManager> {
    KVServer kvServer;

    @Override
    HTTPTaskManager createTaskManager() {
        try {
            HTTPTaskManager httpTaskManager = new HTTPTaskManager(URI.create("http://localhost:8078/"));
            return httpTaskManager;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @BeforeEach
    public void startServer() {
        try {
            kvServer = new KVServer();
            kvServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void stopServer() {
        kvServer.stop();
    }

}