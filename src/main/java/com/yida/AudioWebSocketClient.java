package com.yida;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 * @author yida
 * @package com.yida
 * @date 2024-12-18 14:05
 * @description Type your description over here.
 */
public class AudioWebSocketClient extends WebSocketClient {

	private final File audioFile;
	private final int bufferSize;
	private long last_message_time = 0L;

	public AudioWebSocketClient(String uri, File audioFile) throws IOException, URISyntaxException, UnsupportedAudioFileException {
		super(new URI(uri));
		this.audioFile = audioFile;
		try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile)) {
			AudioFormat format = audioInputStream.getFormat();
			this.bufferSize = (int) (format.getFrameRate() * 0.2);
		}
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile)) {
			AudioFormat format = audioInputStream.getFormat();
			send("{\"config\" : { \"sample_rate\" : " + format.getSampleRate() + " }}");
			byte[] buffer = new byte[bufferSize];
			int bytesRead;
			while ((bytesRead = audioInputStream.read(buffer)) != -1) {
				if (bytesRead > 0) {
					// 使用ByteBuffer来发送数据
					ByteBuffer byteBuffer = ByteBuffer.wrap(buffer, 0, bytesRead);
					send(byteBuffer);
				}
			}
			//send("{\"eof\" : 1}");
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onMessage(String message) {
		String emptyPartialMessage = "{\n" +
				"  \"partial\" : \"\"\n" +
				"}";
		String emptyTextMessage = "{\n" +
				"  \"text\" : \"\"\n" +
				"}";
		//忽略空消息
		if (emptyPartialMessage.equals(message) || emptyTextMessage.equals(message)) {
			return;
		}
		System.out.println(message);
		last_message_time = System.currentTimeMillis();
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason);
	}

	@Override
	public void onError(Exception ex) {
		ex.printStackTrace();
	}

	public long getLast_message_time() {
		return last_message_time;
	}
}
