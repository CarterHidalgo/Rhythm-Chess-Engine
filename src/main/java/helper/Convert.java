package helper;

import java.util.ArrayList;

import model.Bitboard;
import model.GameInfo;

public class Convert {
    /*
     * Conversion Coordinate Systems
     * 
     * Mouse: (0,0) is top left of application window - float mouseX and mouseY (pixel value)
     * Corners: (0,0) is top left of application window - byte value top left square corner (pixel value)
     * UV: (0,0) is bottom left of application window - byte value as an index 0-7 (accessed like a 2D array)
     * BitIndex: 0 is bottom left of application window - byte value as an index 0-63 into bitboard
     */
    
    public static Vec2 mouseToCorner(float mouseX, float mouseY) {
        short u = (short) (mouseX / GameInfo.getSquareLength());
        short v = (short) (mouseY / GameInfo.getSquareLength());

        u *= GameInfo.getSquareLength();
        v *= GameInfo.getSquareLength();

        return new Vec2(u, v);
    }

    public static Vec2 mouseToUV(float mouseX, float mouseY) {
        byte u = (byte) (7 - ((byte) (mouseY / GameInfo.getSquareLength())));
        byte v = (byte) (mouseX / GameInfo.getSquareLength());

        return new Vec2(u, v);
    }
    
    public static byte mouseToBitIndex(float mouseX, float mouseY) {
        if(mouseX < 0 || mouseX > GameInfo.getBoardLength() || mouseY < 0 || mouseY > GameInfo.getBoardLength()) {
            return -1;
        }
        
        Vec2 uvCoords = mouseToUV(mouseX, mouseY);
        byte bitIndex = uvToBitIndex(uvCoords.getXAsByte(), uvCoords.getYAsByte());

        return bitIndex;
    }

    public static void cornerToUV() {
        // TODO: Implement corner to uv conversion
    }

    public static void cornerToBitIndex() {
        // TODO: Implement corner to bit index conversion
    }

    public static Vec2 uvToCorner(byte u, byte v) {
        short cx = (short) (v * GameInfo.getSquareLength());
        short cy = (short) ((7 - u) * GameInfo.getSquareLength());

        return new Vec2(cx, cy);
    }

    public static byte uvToBitIndex(byte u, byte v) {
        return (byte) (u * 8 + v);
    }

    public static Vec2 bitIndexToCorner(byte bitIndex) {
        Vec2 uvCoords = bitIndexToUV(bitIndex);
        Vec2 corner = uvToCorner(uvCoords.getXAsByte(), uvCoords.getYAsByte());

        return corner;
    }
    
    public static Vec2 bitIndexToUV(byte bitIndex) {
        byte u = (byte) (bitIndex / 8);
        byte v = (byte) (bitIndex % 8);

        return new Vec2(u, v);
    }

    public static ArrayList<Vec2> bitboardToCorners(long bitboard) {
        ArrayList<Vec2> corners = new ArrayList<>();
        int bitCount = Long.bitCount(bitboard);
    
        for(int i = 0; i < bitCount; i++) {
            int bitIndex = Long.numberOfTrailingZeros(bitboard);
            corners.add(bitIndexToCorner((byte) bitIndex));
    
            bitboard &= ~(1L << bitIndex);
        }
    
        return corners;
    }

    // Side should be the side of the piece that will be on the square with bitIndex `value`
    public static int bitIndexShiftBySide(String side, int value, int offset) {
        if(side == "white") {
            return value + offset;
        } else {
            return value - offset;
        }
    }

    public static boolean bitboardIsSet(String key, int index) {
        return (Bitboard.getBitboard(key) & (1L << index)) != 0;
    }
}
