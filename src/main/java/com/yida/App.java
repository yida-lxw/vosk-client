package com.yida;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @package        com.yida
 * @author         yida
 * @date           2024-12-19 09:49
 * @description    Type your description over here.
 */
public class App {
	public static void main(String[] args) {
		if(null == args || args.length <= 0) {
			System.out.println("请输入音频文件路径(文件路径中若包含了空格, 请使用英文双引号将其包裹起来)");
			return;
		}
		String audioPath = args[0];
		//String audioPath = "E:/idea_projects/vosk-client/xxxx.wav";
		File audioFile = new File(audioPath);
		AudioWebSocketClient client = null;
		try {
			client = new AudioWebSocketClient("ws://10.180.81.101:10040", audioFile);

			ExecutorService executorService = Executors.newSingleThreadExecutor();
			AudioWebSocketClient finalClient = client;
			executorService.submit(() -> {
				while (true) {
					long currentTime = System.currentTimeMillis();
					//超过10秒未收到服务端的消息, 则自动关闭链接
					long diff = Math.abs(currentTime - finalClient.getLast_message_time());
					System.out.println("当前时间:" + currentTime + ",最后一次消息更新时间:" + finalClient.getLast_message_time() + ",两者差值:" + diff);
					if (diff > 10000 && finalClient.getLast_message_time() > 0) {
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
