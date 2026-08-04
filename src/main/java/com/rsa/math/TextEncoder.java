package com.rsa.math;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TextEncoder {

    public static BigInteger toBigInteger(String text) {
        return new BigInteger(1, text.getBytes(StandardCharsets.UTF_8));
    }

    public static String fromBigInteger(BigInteger block) {
        byte[] bytes = block.toByteArray();
        int off = bytes[0] == 0 ? 1 : 0;
        return new String(bytes, off, bytes.length - off, StandardCharsets.UTF_8);
    }

    public static List<BigInteger> encodeBlocks(String text, int maxBits) {
        byte[] data = text.getBytes(StandardCharsets.UTF_8);
        int blockBytes = (maxBits - 8) / 8;
        if (blockBytes <= 0) {
            throw new IllegalArgumentException("maxBits too small");
        }
        List<BigInteger> blocks = new ArrayList<>();
        for (int i = 0; i < data.length; i += blockBytes) {
            int end = Math.min(i + blockBytes, data.length);
            byte[] chunk = new byte[end - i + 1];
            chunk[0] = (byte) (end - i);
            System.arraycopy(data, i, chunk, 1, end - i);
            blocks.add(new BigInteger(1, chunk));
        }
        return blocks;
    }

    public static String decodeBlocks(List<BigInteger> blocks) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (BigInteger block : blocks) {
            byte[] all = block.toByteArray();
            int off = all[0] == 0 ? 1 : 0;
            int len = all[off] & 0xFF;
            out.write(all, off + 1, len);
        }
        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }
}
