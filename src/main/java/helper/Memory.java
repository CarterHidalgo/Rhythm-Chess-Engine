package helper;

public class Memory {
    public static String sizeInKilobytes(byte[] table) {
        long sizeInBytes = (table.length + 12) + ((table.length + 12) % 8);
        double sizeInKilobytes = sizeInBytes / 1000.0;
        
        return String.format("%.2f", sizeInKilobytes);
    }

    public static String sizeInKilobytes(short[] table) {
        long sizeInBytes = (table.length * 2 + 12) + ((table.length * 2 + 12) % 8);
        double sizeInKilobytes = sizeInBytes / 1000.0;
        
        return String.format("%.2f", sizeInKilobytes);
    }

    public static String sizeInKilobytes(int[] table) {
        long sizeInBytes = (table.length * 4 + 12) + ((table.length * 4 + 12) % 8);
        double sizeInKilobytes = sizeInBytes / 1000.0;
        
        return String.format("%.2f", sizeInKilobytes);
    }

    public static String sizeInKilobytes(long[] table) {
        long sizeInBytes = (table.length * 8 + 12) + ((table.length * 8 + 12) % 8);
        double sizeInKilobytes = sizeInBytes / 1000.0;
        
        return String.format("%.2f", sizeInKilobytes);
    }
}
