package model.attacks;

public class KnightAttacks {
    // Hardcode all possible squares a knight can attack from any given square to avoid board falloff computation on the fly

    private static final Long[] KNIGHT_ATTACKS = {
        0x20400L, // 0
        0x50800L, // 1
        0xa1100L, // 2
        0x142200L, // 3
        0x284400L, // 4
        0x508800L, // 5
        0xa01000L, // 6
        0x402000L, // 7

        0x2040004L, // 8
        0x5080008L, // 9
        0xa110011L, // 10
        0x14220022L, // 11
        0x28440044L, // 12
        0x50880088L, // 13
        0xa0100010L, // 14
        0x40200020L, // 15

        0x204000402L, // 16
        0x508000805L, // 17
        0xa1100110aL, // 18
        0x1422002214L, // 19
        0x2844004428L, // 20
        0x5088008850L, // 21
        0xa0100010a0L, // 22
        0x4020002040L, // 23

        0x20400040200L, // 24
        0x50800080500L, // 25
        0xa1100110a00L, // 26
        0x142200221400L, // 27
        0x284400442800L, // 28
        0x508800885000L, // 29
        0xa0100010a000L, // 30
        0x402000204000L, // 31

        0x2040004020000L, // 32
        0x5080008050000L, // 33
        0xa1100110a0000L, // 34
        0x14220022140000L, // 35
        0x28440044280000L, // 36
        0x50880088500000L, // 37
        0xa0100010a00000L, // 38
        0x40200020400000L, // 39

        0x204000402000000L, // 40
        0x508000805000000L, // 41
        0xa1100110a000000L, // 42
        0x1422002214000000L, // 43
        0x2844004428000000L, // 44
        0x5088008850000000L, // 45
        0xa0100010a0000000L, // 46
        0x4020002040000000L, // 47

        0x400040200000000L, // 48
        0x800080500000000L, // 49
        0x1100110a00000000L, // 50
        0x2200221400000000L, // 51
        0x4400442800000000L, // 52
        0x8800885000000000L, // 53
        0x100010a000000000L, // 54
        0x2000204000000000L, // 55

        0x4020000000000L, // 56
        0x8050000000000L, // 57
        0x110a0000000000L, // 58
        0x22140000000000L, // 59
        0x44280000000000L, // 60
        0x88500000000000L, // 61
        0x10a00000000000L, // 62
        0x20400000000000L, // 63
    };

    public static Long getKnightAttacks(int square) {
        if(square < 0 || square > 63) {
            square %= 64;
            System.out.println("WARNING: square has been modulated in getKnightAttacks() -> KnightAttacks.java");
        }

        return KNIGHT_ATTACKS[square];
    }
}