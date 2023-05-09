package com.loginid.cryptodid.claimVerifier;

import com.google.gson.Gson;
import com.loginid.cryptodid.MainActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

import javax.websocket.OnMessage;
import javax.websocket.Session;


public class ClientEndpoint {
    public String response;
    public CountDownLatch latch = new CountDownLatch(1);
    public WebSocketClient webSocketClient;
    List<Byte> fileByte=new ArrayList<Byte>();
    private Gson gson = new Gson();


    public WebSocketClient createWebSocketClient(String url){
        URI uri;
        try {
            uri = new URI(url);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }


        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("onOpen");
                //webSocketClient.send(response);
                latch.countDown();
                latch = new CountDownLatch(1);
            }

            @OnMessage(maxMessageSize = 20000000)
            public void onMessage(ByteBuffer buffer) {
                byte[] arr = new byte[buffer.remaining()];
                buffer.get(arr);
                Byte[] byteObjects = new Byte[arr.length];
                int i=0;
                for(byte b: arr)
                    byteObjects[i++] = b;
                fileByte.addAll(Arrays.asList(byteObjects));
                System.out.println("message");
                webSocketClient.send("ping");
            }

            @Override
            public void onMessage(String message) {
                int j=0;
                if(!Objects.equals(gson.fromJson(message, String.class), "DONE")){
                    byte[] cloudKeyBytes = new byte[fileByte.size()];;
                    Byte[] byteObjects = fileByte.toArray(new Byte[0]);
                    for(Byte b: byteObjects)
                        cloudKeyBytes[j++] = b.byteValue();
                    fileByte.clear();
                    try {
                        FileUtils.writeByteArrayToFile(new File(MainActivity.path+"/"+message), cloudKeyBytes);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(message);
                    response = message;
                    latch.countDown();

                }else{
                    try {
                        TimeUnit.SECONDS.sleep(30);
                        System.out.println("Waiting");
                        webSocketClient.send("ping");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("onCloseReceived");
            }
            @Override
            public void onError(Exception ex) {
                System.out.println("onErrorReceived");
            }


        };
        return webSocketClient;

    }
    public void sendFile(String filePath, String name) throws IOException {
        File file = new File(filePath);
        byte[] bytes = FileUtils.readFileToByteArray(file);
        int from, to;
        from = 0;
        to = 20000000;
        while (bytes.length > to){
            webSocketClient.send(Arrays.copyOfRange(bytes, from, to));
            from = to;
            to += 20000000;
        }
        webSocketClient.send(Arrays.copyOfRange(bytes, from, bytes.length));
        webSocketClient.send(name);

    }

}

