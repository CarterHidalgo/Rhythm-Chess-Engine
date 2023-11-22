package helper;

import java.util.ArrayList;

import model.Bitboard;
import model.GameInfo;

public class Convert {
    /*
     * Conversion Coordinate Systems
     * 
     * Mouse: (0,0) is top left of application window - raw double mouseX and mouseY
     * Corners: (0,0) is top left of application window - int value top left square corner
     * UV: (0,0) is bottom left of application window - int value as an index 0-7 (accessed like a 2D array)
     * BitIndex: 0 is bottom left of application window - int value as an index 0-63 into bitboard
     */
    
    public static Vec2 mouseToCorner(double mouseX, double mouseY) {
        int u = (int) (mouseX / GameInfo.getSquareLength());
        int v = (int) (mouseY / GameInfo.getSquareLength());

        u *= GameInfo.getSquareLength();
        v *= GameInfo.getSquareLength();

        return new Vec2(u, v);
    }

    public static Vec2 mouseToUV(double mouseX, double mouseY) {
        int u = 7 - ((int) (mouseY / GameInfo.getSquareLength()));
        int v = (int) (mouseX / GameInfo.getSquareLength());

        return new Vec2(u, v);
    }
    
    public static int mouseToBitIndex(double mouseX, double mouseY) {
        if(mouseX < 0 || mouseX > GameInfo.getBoardLength() || mouseY < 0 || mouseY > GameInfo.getBoardLength()) {
            return -1;
        }
        Vec2 uvCoords = mouseToUV(mouseX, mouseY);
        int bitIndex = uvToBitIndex(uvCoords.getXAsInt(), uvCoords.getYAsInt());

        return bitIndex;
    }

    public static void cornerToUV() {

    }

    public static void cornerToBitIndex() {

    }

    public static Vec2 uvToCorner(int u, int v) {
        int cx = v * GameInfo.getSquareLength();
        int cy = (7 - u) * GameInfo.getSquareLength();

        return new Vec2(cx, cy);
    }

    public static int uvToBitIndex(int u, int v) {
        return u * 8 + v;
    }

    public static Vec2 bitIndexToCorner(int bitIndex) {
        Vec2 uvCoords = bitIndexToUV(bitIndex);
        Vec2 corner = uvToCorner(uvCoords.getXAsInt(), uvCoords.getYAsInt());

        return corner;
    }
    
    public static Vec2 bitIndexToUV(int bitIndex) {
        int u = bitIndex / 8;
        int v = bitIndex % 8;

        return new Vec2(u, v);
    }

    public static ArrayList<Vec2> bitboardToCorners(long bitboard) {
        ArrayList<Vec2> corners = new ArrayList<>();
        int bitCount = Long.bitCount(bitboard);
    
        for(int i = 0; i < bitCount; i++) {
            int bitIndex = Long.numberOfTrailingZeros(bitboard);
            corners.add(bitIndexToCorner(bitIndex));
    
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
