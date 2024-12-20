package com.yida;

import com.neovisionaries.ws.client.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

//首先需要将音频文件转为采样率16000, 16位采样精度 单声道 pcm
//ffmpeg -i file.mp3 -ar 16000 -c:a pcm_s -ac 1 file.wav
public class VoskClient {

    private ArrayList<String> results = new ArrayList<String>();

    public ArrayList<String> transcribe(String path) throws Exception {
            WebSocketFactory factory = new WebSocketFactory();
            WebSocket ws = factory.createSocket("ws://localhost:2700");
            ws.addListener(new WebSocketAdapter() {
                @Override
                public void onTextMessage(WebSocket websocket, String message) {
                    results.add(message);
                }
            });
            ws.connect();

            int totalFramesRead = 0;
            File fileIn = new File(path);
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fileIn);
                AudioFormat format = audioInputStream.getFormat();
                int bytesPerFrame = format.getFrameSize();
                if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
                    bytesPerFrame = 1;
                }

                // Let Vosk server now the sample rate of sound file
                ws.sendText("{ \"config\" : { \"sample_rate\" : " + (int)format.getSampleRate() + " } }");

                // Set an arbitrary buffer size of 1024 frames.
                int numBytes = 1024 * bytesPerFrame;
                byte[] audioBytes = new byte[numBytes];
                try {
                    int numBytesRead = 0;
                    int numFramesRead = 0;
                    // Try to read numBytes bytes from the file.
                    while ((numBytesRead = audioInputStream.read(audioBytes)) != -1) {
                        // Calculate the number of frames actually read.
                        numFramesRead = numBytesRead / bytesPerFrame;
                        totalFramesRead += numFramesRead;
                        ws.sendBinary(audioBytes);
                    }
                    ws.sendText("{\"reset\" : 1}");
                    //ws.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
		}
            } catch (Exception e) {
                e.printStackTrace();
	    }
            return results;
        }

    public static void main(String[] args) throws Exception {
        String audioFilePath = "E:/idea_projects/vosk-client/传承红色基因_8k.wav";
        VoskClient client = new VoskClient();
        for (String res : client.transcribe(audioFilePath)) {
             System.out.println(res);
        }
    }
}

