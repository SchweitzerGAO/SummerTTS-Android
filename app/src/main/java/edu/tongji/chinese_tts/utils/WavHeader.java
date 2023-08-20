package edu.tongji.chinese_tts.utils;

public class WavHeader {
    public String riff = "RIFF";
    public int chunkSize;
    public String wave = "WAVE";
    public String fmt = "fmt ";
    public int fmtSize = 16;
    public short audioFmt = 1;
    public short numChannels;
    public int sampleRate;
    public int byteRate;
    public short blockAlign = 2;
    public short bitsPerSample = 16;
    public String data = "data";
    public int dataSize;

    public WavHeader(int sampleRate, int sampleWidth, short numChannels, int numSamples) {
        this.numChannels = numChannels;
        this.sampleRate = sampleRate;
        dataSize = numSamples * sampleWidth * numChannels;
        int HEADER_WO_DATA = 36;
        chunkSize = dataSize + HEADER_WO_DATA; // 44 is the size of header itself
        byteRate = sampleRate * sampleWidth * numChannels;
        blockAlign = (short) (sampleWidth * numChannels);

    }



}
