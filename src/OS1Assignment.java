package src;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class OS1Assignment {
    public static void main(String []args) {
        try {
            int idx = 1;

            // Stores the current 8 byte sequence being operated on
            long[] current = new long[8];
            // Stores virtual addresses
            ArrayList<Long> virtualAddresses = new ArrayList<Long>();

            // Stores mappings. Index 0 (or Page 0) maps to Frame 2
            // Page 1 maps to Frame 4, and so forth...
            int[] physicalPages = { 2, 4, 1, 7, 3, 5, 6 };

            String finalContents = "";

            InputStream is = new FileInputStream("./src/" + args[0]);
            // Read first byte of file
            long data = is.read();

            // While end of stream (byte = -1) not reached
            while(data != -1) {
                // If on a multiple of 8 (or 0)
                if(idx % 8 == 0) {
                    // Store current byte in first index of current array
                    current[0] = data;

                    // Store the current hexadecimal address in string format
                    String hexadecimalStr = "";

                    // Decrementing for-loop, because bytes are read in reverse sequence
                    for(int i = 7; i > 0; i--)
                        // Convert byte-by-byte 
                        hexadecimalStr += Long.toHexString(current[i]);

                    // Convert hexadecimal address to binary
                    String binStr = Long.toBinaryString(Long.parseLong(hexadecimalStr, 16));
                    // Pad to safely extract lower 12 bits for virtual address
                    String paddedBinStr = String.format("%32s", binStr).replace(" ", "0");

                    // 32-12 = 20
                    String virtualAddress = paddedBinStr.substring(20);

                    // Now I have the virtual address, add it to the list
                    virtualAddresses.add(Long.parseLong(virtualAddress, 2));

                    // Clear the current array to parse the next address
                    Arrays.fill(current, 0);

                } else {
                    // Not on a multiple of 8, keep populating the current array
                    current[idx % 8] = data;
                }

                // Read next byte
                data = is.read();
                idx++;
            }

            for(long virtualAddr : virtualAddresses) {
                // Convert address to binary to get lower 7 bits
                String virtualAddrBinStr = Long.toBinaryString(virtualAddr);
                // Pad to safely access lower 7 bits and get offset
                String paddedVirtualAddrBinStr = String.format("%32s", virtualAddrBinStr).replace(" ", "0");
                
                // 32 - 7 = 25
                String offsetStr = paddedVirtualAddrBinStr.substring(25);

                // Convert offset (lower 7 bits) from binary to decimal
                long offset = Long.parseLong(offsetStr, 2);

                // Page index, based on the fact that each page is 128 bytes
                int virtualPageIdx;

                if(virtualAddr < 128) {
                    virtualPageIdx = 0;
                }
                
                else if(virtualAddr < 256) {
                    virtualPageIdx = 1;
                }
                
                else if(virtualAddr < 384) {
                    virtualPageIdx = 2;
                }
                
                else if(virtualAddr < 512) {
                    virtualPageIdx = 3;
                }
                
                else if(virtualAddr < 640) {
                    virtualPageIdx = 4;
                }
                
                else if(virtualAddr < 768) {
                    virtualPageIdx = 5;
                }

                else if(virtualAddr < 896) {
                    virtualPageIdx = 6;
                }
                
                else {
                    virtualPageIdx = 7;
                }

                // Get physical page by using the mapping
                int physicalPage = physicalPages[virtualPageIdx];

                // Get physical address by going to base index of corresponding page, then adding offset
                long physicalAddr = (physicalPage * 128) + offset;

                // Convert address to hexadecimal
                String physicalAddrHexStr = Long.toHexString(physicalAddr);
                
                // Add "0x" to final contents of file to be written
                finalContents += String.format("0x%s\n", physicalAddrHexStr);
            }

            // Trim last endline char
            finalContents = finalContents.substring(0, finalContents.length()-1);

            // Write to output file
            FileWriter fw = new FileWriter(new File("./output-OS1"));

            fw.write(finalContents);

            fw.close();
            is.close();
        }

        catch(Exception e) {
            e.printStackTrace();
        }
    }
}