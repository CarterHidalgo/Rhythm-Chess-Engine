package model.magic;

import helper.Bit;
import helper.Coord;
import model.Bitboard;

public class MagicBitboardMask {
    // These are white magic masks and do not include rays to outer squares
    // (outer squares are included when the source square is an outer square)
    
    private static final Long[] ROOK_MASKS = {
        0x101010101017eL, // 0
        0x202020202027cL, // 1
        0x404040404047aL, // 2
        0x8080808080876L, // 3
        0x1010101010106eL, // 4
        0x2020202020205eL, // 5
        0x4040404040403eL, // 6
        0x8080808080807eL, // 7

        0x1010101017e00L, // 8
        0x2020202027c00L, // 9
        0x4040404047a00L, // 10
        0x8080808087600L, // 11
        0x10101010106e00L, // 12
        0x20202020205e00L, // 13
        0x40404040403e00L, // 14
        0x80808080807e00L, // 15

        0x10101017e0100L, // 16
        0x20202027c0200L, // 17
        0x40404047a0400L, // 18
        0x8080808760800L, // 19
        0x101010106e1000L, // 20
        0x202020205e2000L, // 21
        0x404040403e4000L, // 22
        0x808080807e8000L, // 23

        0x101017e010100L, // 24
        0x202027c020200L, // 25
        0x404047a040400L, // 26
        0x8080876080800L, // 27
        0x1010106e101000L, // 28
        0x2020205e202000L, // 29
        0x4040403e404000L, // 30
        0x8080807e808000L, // 31

        0x1017e01010100L, // 32
        0x2027c02020200L, // 33
        0x4047a04040400L, // 34
        0x8087608080800L, // 35
        0x10106e10101000L, // 36
        0x20205e20202000L, // 37
        0x40403e40404000L, // 38
        0x80807e80808000L, // 39

        0x17e0101010100L, // 40
        0x27c0202020200L, // 41
        0x47a0404040400L, // 42
        0x8760808080800L, // 43
        0x106e1010101000L, // 44
        0x205e2020202000L, // 45
        0x403e4040404000L, // 46
        0x807e8080808000L, // 47

        0x7e010101010100L, // 48
        0x7c020202020200L, // 49
        0x7a040404040400L, // 50
        0x76080808080800L, // 51
        0x6e101010101000L, // 52
        0x5e202020202000L, // 53
        0x3e404040404000L, // 54
        0x7e808080808000L, // 55

        0x7e01010101010100L, // 56
        0x7c02020202020200L, // 57
        0x7a04040404040400L, // 58
        0x7608080808080800L, // 59
        0x6e10101010101000L, // 60
        0x5e20202020202000L, // 61
        0x3e40404040404000L, // 62
        0x7e80808080808000L, // 63 
    };

    private static final Long[] BISHOP_MASKS = {
        0x40201008040200L, // 0
        0x402010080400L, // 1
        0x4020100a00L, // 2
        0x40221400L, // 3
        0x2442800L, // 4
        0x204085000L, // 5
        0x20408102000L, // 6
        0x2040810204000L, // 7

        0x20100804020000L, // 8
        0x40201008040000L, // 9
        0x4020100a0000L, // 10
        0x4022140000L, // 11
        0x244280000L, // 12
        0x20408500000L, // 13
        0x2040810200000L, // 14
        0x4081020400000L, // 15

        0x10080402000200L, // 16
        0x20100804000400L, // 17
        0x4020100a000a00L, // 18
        0x402214001400L, // 19
        0x24428002800L, // 20
        0x2040850005000L, // 21
        0x4081020002000L, // 22
        0x8102040004000L, // 23

        0x8040200020400L, // 24
        0x10080400040800L, // 25
        0x20100a000a1000L, // 26
        0x40221400142200L, // 27
        0x2442800284400L, // 28
        0x4085000500800L, // 29
        0x8102000201000L, // 30
        0x10204000402000L, // 31

        0x4020002040800L, // 32
        0x8040004081000L, // 33
        0x100a000a102000L, // 34
        0x22140014224000L, // 35
        0x44280028440200L, // 36
        0x8500050080400L, // 37
        0x10200020100800L, // 38
        0x20400040201000L, // 39

        0x2000204081000L, // 40
        0x4000408102000L, // 41
        0xa000a10204000L, // 42
        0x14001422400000L, // 43
        0x28002844020000L, // 44
        0x50005008040200L, // 45
        0x20002010080400L, // 46
        0x40004020100800L, // 47

        0x20408102000L, // 48
        0x40810204000L, // 49
        0xa1020400000L, // 50
        0x142240000000L, // 51
        0x284402000000L, // 52
        0x500804020000L, // 53
        0x201008040200L, // 54
        0x402010080400L, // 55

        0x2040810204000L, // 56
        0x4081020400000L, // 57
        0xa102040000000L, // 58
        0x14224000000000L, // 59
        0x28440200000000L, // 60
        0x50080402000000L, // 61
        0x20100804020000L, // 62
        0x40201008040200L, // 63
    };

    public static Long getRookMask(int square) {
        return ROOK_MASKS[square];
    }

    public static Long getBishopMask(int square) {
        return BISHOP_MASKS[square];
    }

    // legacy method for generating rook masks
    public static void generateRookMasks() {
        for(int idx = 0; idx < 64; idx++) {
        int row = idx / 8;
        int col = idx % 8;

        long rowMask = 0xFFL << (row * 8);
        long colMask = 0x0101010101010101L << col;
        
        long finalMask = (rowMask | colMask);

        if(row != 0) {
            finalMask &= ~Bitboard.getBitboard("1Rank");
        }

        if(row != 7) {
            finalMask &= ~Bitboard.getBitboard("8Rank");
        }

        if(col != 0) {
            finalMask &= ~Bitboard.getBitboard("aFile");
        }

        if(col != 7) {
            finalMask &= ~Bitboard.getBitboard("hFile");
        }

        finalMask = Bit.clearBit(finalMask, idx);
        System.out.println("0x" + Long.toHexString(finalMask) + "L, // " + idx);
        }
    }

    // legacy method for generating bishop masks
    public static void generateBishopMasks() {
        Coord[] directions = Coord.bishopDirections;

        for(int square = 0; square < 64; square++) {
            Coord startSquare = new Coord(square);
            long mask = 0;
            
            for(Coord dir : directions) {
                for(int dist = 1; dist < 8; dist++) {
                    Coord maskCoord = startSquare.add(dir.mul(dist));

                    if(maskCoord.isNotEdge()) {
                        mask = Bit.setBit(mask, maskCoord.getIndex());
                    } else {
                        break;
                    }
                }
            }

            if(square % 8 == 0) {
                System.out.println();
            }

            System.out.println("0x" + Long.toHexString(mask) + "L, // " + square);
        }


    }
}
