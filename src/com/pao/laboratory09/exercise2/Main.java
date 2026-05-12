package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    private static final Map<String, Byte> STATUS_TO_BYTE = Map.of(
        "PENDING",   (byte) 0,
        "PROCESSED", (byte) 1,
        "REJECTED",  (byte) 2
    );
    private static final String[] BYTE_TO_STATUS = {"PENDING", "PROCESSED", "REJECTED"};

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        int n = Integer.parseInt(sc.nextLine().trim());

        new File("output").mkdirs();

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            for (int i = 0; i < n; i++) {
                String[] p = sc.nextLine().trim().split(" ");
                int id        = Integer.parseInt(p[0]);
                double suma   = Double.parseDouble(p[1]);
                String data   = p[2];
                TipTranzactie tip = TipTranzactie.valueOf(p[3]);

                dos.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(id).array());
                dos.write(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(suma).array());
                byte[] dataBytes = new byte[10];
                Arrays.fill(dataBytes, (byte) ' ');
                System.arraycopy(data.getBytes(), 0, dataBytes, 0, data.length());
                dos.write(dataBytes);
                dos.write(tip == TipTranzactie.CREDIT ? 0 : 1);
                dos.write(0);
                dos.write(new byte[8]);
            }
        }

        try (RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw")) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("READ ")) {
                    int idx = Integer.parseInt(line.substring(5).trim());
                    System.out.println(readRecord(raf, idx));

                } else if (line.startsWith("UPDATE ")) {
                    String[] parts = line.substring(7).trim().split(" ");
                    int idx = Integer.parseInt(parts[0]);
                    String status = parts[1];
                    raf.seek((long) idx * RECORD_SIZE + 23);
                    raf.write(STATUS_TO_BYTE.get(status));
                    System.out.println("Updated [" + idx + "]: " + status);

                } else if (line.equals("PRINT_ALL")) {
                    long count = raf.length() / RECORD_SIZE;
                    for (int i = 0; i < count; i++) {
                        System.out.println(readRecord(raf, i));
                    }
                }
            }
        }
    }

    private static String readRecord(RandomAccessFile raf, int idx) throws IOException {
        raf.seek((long) idx * RECORD_SIZE);
        byte[] bytes = new byte[RECORD_SIZE];
        raf.readFully(bytes);
        ByteBuffer buf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);

        int id         = buf.getInt();        
        double suma    = buf.getDouble();     
        byte[] db      = new byte[10];
        buf.get(db);                         
        String data    = new String(db).trim();
        int tipByte    = buf.get() & 0xFF;  
        int statusByte = buf.get() & 0xFF; 

        String tip    = tipByte == 0 ? "CREDIT" : "DEBIT";
        String status = BYTE_TO_STATUS[statusByte];

        return String.format("[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s",
                idx, id, data, tip, suma, status);
    }
}
