import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class OS1Assignment {
    public static void main(String []args) {
        try {
            InputStream is = new FileInputStream("OS1testsequence");

            long data = is.read();

            System.out.println(data);

            int idx = 1;

            long[] current = new long[8];
            ArrayList<Long> virtualAddresses = new ArrayList<Long>();

            int[] physicalPages = { 2, 4, 1, 7, 3, 5, 6 };

            String finalContents = "";

            while(data != -1) {
                if(idx % 8 == 0) {

                    current[0] = data;

                    String hexadecimalStr = "";
                    for(int i = 7; i > 0; i--)
                        hexadecimalStr += Long.toHexString(current[i]);

                    String binStr = Long.toBinaryString(Long.parseLong(hexadecimalStr, 16));
                    String paddedBinStr = String.format("%32s", binStr).replace(" ", "0");

                    // 32-12 = 20
                    String virtualAddress = paddedBinStr.substring(20);

                    virtualAddresses.add(Long.parseLong(virtualAddress, 2));

                    Arrays.fill(current, 0);

                } else {
                    current[idx % 8] = data;
                }

                data = is.read();
                idx++;
            }

            for(long _virtualAddr : virtualAddresses) {
                int virtualAddr = (int) _virtualAddr;

                String virtualAddrBinStr = Long.toBinaryString(virtualAddr);
                String paddedVirtualAddrBinStr = String.format("%32s", virtualAddrBinStr).replace(" ", "0");
                
                // 32 - 7 = 25
                String offsetStr = paddedVirtualAddrBinStr.substring(25);
                long offset = Long.parseLong(offsetStr, 2);

                long virtualPageIdx;

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

                int physicalPage = physicalPages[(int) virtualPageIdx];

                long physicalAddr = (physicalPage * 128) + offset;

                String physicalAddrHexStr = Long.toHexString(physicalAddr);

                String paddedHexStr = String.format("0x%8s", physicalAddrHexStr).replace(" ", "0") + "\n";
                
                finalContents += paddedHexStr;
            }

            finalContents = finalContents.substring(0, finalContents.length()-1);

            FileWriter fw = new FileWriter(new File("output-OS1"));

            fw.write(finalContents);

            fw.close();
            is.close();
        }

        catch(Exception e) {
            e.printStackTrace();
        }
    }
}