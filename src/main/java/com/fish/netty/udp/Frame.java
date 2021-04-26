package com.fish.netty.udp;

/**
 * Frame:[**]-[********]-[msg]-[*]
 * [cmd(short)]-[timestamp(long)]-[msg]-[结束标记（true/false）]
 *
 * @author: fjjdragon
 * @date: 2021-03-24 22:59
 */
public class Frame {
    private int mark;
    private int index;
    private byte[] content;

    private short cmd = 0;
    private long timestamp;

    public Frame() {
        this.content = new byte[1024];
        this.index = 0;
        this.timestamp = System.currentTimeMillis();
    }

    public Frame(int length) {
        this.content = new byte[length];
        this.index = 0;
        this.timestamp = System.currentTimeMillis();
    }

    public Frame(byte[] bytes) {
        this.content = bytes;
        this.index = bytes.length;
        this.timestamp = System.currentTimeMillis();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public short getCmd() {
        return cmd;
    }

    public void setCmd(short cmd) {
        this.cmd = cmd;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] getBytes() {
        return content;
    }

    public byte[] pack() {
        return content;
    }

}