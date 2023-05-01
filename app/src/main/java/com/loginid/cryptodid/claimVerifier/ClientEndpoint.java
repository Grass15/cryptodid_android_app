package com.loginid.cryptodid.claimVerifier;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;


public class ClientEndpoint {
    public String response;
    public CountDownLatch latch = new CountDownLatch(1);;
    public WebSocketClient webSocketClient;
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
                System.out.println(response.length());
                webSocketClient.send(response);
            }

            @Override
            public void onMessage(String message) {
                System.out.println("onTextReceived");
                System.out.println(message);
                response = message;
                latch.countDown();
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

}

