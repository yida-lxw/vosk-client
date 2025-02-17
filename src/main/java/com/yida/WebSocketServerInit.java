package com.yida;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class WebSocketServerInit {
    @Value("${websocket.host}")
    private String host;

    @Value("${websocket.port}")
    private int port;

    @Value("${websocket.audio-file_path}")
    private String audioFilePath;

    @Value("${websocket.auto-close-connection-timeout-if-no-any-messages:10000}")
    private long autoCloseConnectionTimeout;

    @PostConstruct
    public void init() {
        if(null == audioFilePath || audioFilePath.length() <= 0) {
            System.out.println("请输入音频文件路径(文件路径中若包含了空格, 请使用英文双引号将其包裹起来)");
            return;
        }
        File audioFile = new File(audioFilePath);
        AudioWebSocketClient client = null;
        try {
            String wsURI = "ws://" + host + ":" + port;
            client = new AudioWebSocketClient(wsURI, audioFile);

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            AudioWebSocketClient finalClient = client;
            executorService.submit(() -> {
                while (true) {
                    long currentTime = System.currentTimeMillis();
                    //超过10秒未收到服务端的消息, 则自动关闭链接
                    long diff = Math.abs(currentTime - finalClient.getLast_message_time());
                    System.out.println("当前时间:" + currentTime + ",最后一次消息更新时间:" + finalClient.getLast_message_time() + ",两者差值:" + diff);
                    if (diff > autoCloseConnectionTimeout && finalClient.getLast_message_time() > 0) {
                        finalClient.send("{\"eof\" : 1}");
                        finalClient.close();
                        System.out.println("主动关闭客户端链接");
                        break;
                    }
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            executorService.shutdown();
            client.setConnectionLostTimeout(1200);
            client.connect();
        } catch (IOException | URISyntaxException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }
}
