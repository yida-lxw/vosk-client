package com.yida.utils;

/**
 * @author yida
 * @package com.yida.utils
 * @date 2024-12-18 13:32
 * @description Type your description over here.
 */
public class AudioUtils {
	public static final String command_template = "D:/ffmpeg6.0/bin/ffmpeg.exe -i \"%s\" -ar %s -c:a pcm_s16le -ac 1 \"%s\"";

	/**
	 * @description 将音频文件转换为要求的格式(采样率16000, 16位采样精度 单声道 pcm)
	 * @author yida
	 * @date 2024-12-18 15:13:41
	 * @param audioPath
	 * @param audioOutputPath
	 * @param sampleRate
	 *
	 */
	public static void convert(String audioPath, String audioOutputPath, int sampleRate) {
		String command = String.format(command_template, audioPath, String.valueOf(sampleRate), audioOutputPath);
		CommandExecuteUtils.execute(command);
	}

	public static void main(String[] args) {
		String audioPath = "E:/idea_projects/vosk-client/xxxx.wav";
		String audioOutputPath = "E:/idea_projects/vosk-client/xxxx_8k.wav";
		AudioUtils.convert(audioPath, audioOutputPath, 8000);
	}
}
