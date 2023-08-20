package edu.tongji.chinese_tts.utils;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ChineseTTSUtil {

    private static void load() {
        try{
            System.loadLibrary("zh_tts");
        }
        catch (Error e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private static final String TAG = "ChineseTTSUtil";

    private static DataOutputStream dos;

    protected static final int sampleRate = 16000;
    protected static final int sampleWidth = 2; // 16-bit
    protected static final short channels = 1;    // mono


    private static byte[] intToByteArray(int data) {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(data).array();
    }

    private static byte[] shortToByteArray(short data) {
        return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(data).array();
    }
    private static void openFile(String path) throws IOException {
        if(dos != null) {
            closeFile();
        }
        dos = new DataOutputStream(new FileOutputStream(path));
    }

    private static void closeFile() throws IOException {
        if(dos != null) {
            dos.close();
            dos = null;
        }

    }
    private static void writeHeader(WavHeader header) throws IOException {

        if(dos == null) {
            return;
        }
        dos.writeBytes(header.riff);
        dos.write(intToByteArray(header.chunkSize));
        dos.writeBytes(header.wave);
        dos.writeBytes(header.fmt);
        dos.write(intToByteArray(header.fmtSize), 0, 4);
        dos.write(shortToByteArray(header.audioFmt), 0, 2);
        dos.write(shortToByteArray(header.numChannels), 0, 2);
        dos.write(intToByteArray(header.sampleRate), 0, 4);
        dos.write(intToByteArray(header.byteRate), 0, 4);
        dos.write(shortToByteArray(header.blockAlign), 0, 2);
        dos.write(shortToByteArray(header.bitsPerSample), 0, 2);
        dos.writeBytes(header.data);
        dos.write(intToByteArray(header.dataSize), 0, 4);
    }
    private static void writeData(short[] audio) throws IOException {
        for(short a: audio) {
            dos.write(shortToByteArray(a),0,2);
        }

    }

    private static void writeAudio(String path, WavHeader header, short[] audio) throws IOException {
        openFile(path);
        writeHeader(header);
        writeData(audio);
        closeFile();
    }

    private static native short[] synthesize(String text, String modelPath);

    public static void synthAndWrite(String text, String modelPath, String wavPath) throws IOException {
        try {
            load();
            short[] wavData = synthesize(text,modelPath);

            WavHeader header = new WavHeader(sampleRate,sampleWidth,channels,wavData.length);
            writeAudio(wavPath,header,wavData);
        }
        catch (Exception e) {
            Log.e(TAG,e.getMessage());
        }

    }

}
