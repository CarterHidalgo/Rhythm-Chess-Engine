package helper;

public class Xray {
    private static final long[] XRAY_ATTACKS = {
        0x81412111090503feL, // 0
        0x2824222120a07fdL, // 1
        0x404844424150efbL, // 2
        0x8080888492a1cf7L, // 3
        0x10101011925438efL, // 4
        0x2020212224a870dfL, // 5
        0x404142444850e0bfL, // 6
        0x8182848890a0c07fL, // 7

        0x412111090503fe03L, // 8
        0x824222120a07fd07L, // 9
        0x4844424150efb0eL, // 10
        0x80888492a1cf71cL, // 11
        0x101011925438ef38L, // 12
        0x20212224a870df70L, // 13
        0x4142444850e0bfe0L, // 14
        0x82848890a0c07fc0L, // 15

        0x2111090503fe0305L, // 16
        0x4222120a07fd070aL, // 17
        0x844424150efb0e15L, // 18
        0x888492a1cf71c2aL, // 19
        0x1011925438ef3854L, // 20
        0x212224a870df70a8L, // 21
        0x42444850e0bfe050L, // 22
        0x848890a0c07fc0a0L, // 23

        0x11090503fe030509L, // 24
        0x22120a07fd070a12L, // 25
        0x4424150efb0e1524L, // 26
        0x88492a1cf71c2a49L, // 27
        0x11925438ef385492L, // 28
        0x2224a870df70a824L, // 29
        0x444850e0bfe05048L, // 30
        0x8890a0c07fc0a090L, // 31

        0x90503fe03050911L, // 32
        0x120a07fd070a1222L, // 33
        0x24150efb0e152444L, // 34
        0x492a1cf71c2a4988L, // 35
        0x925438ef38549211L, // 36
        0x24a870df70a82422L, // 37
        0x4850e0bfe0504844L, // 38
        0x90a0c07fc0a09088L, // 39

        0x503fe0305091121L, // 40
        0xa07fd070a122242L, // 41
        0x150efb0e15244484L, // 42
        0x2a1cf71c2a498808L, // 43
        0x5438ef3854921110L, // 44
        0xa870df70a8242221L, // 45
        0x50e0bfe050484442L, // 46
        0xa0c07fc0a0908884L, // 47

        0x3fe030509112141L, // 48
        0x7fd070a12224282L, // 49
        0xefb0e1524448404L, // 50
        0x1cf71c2a49880808L, // 51
        0x38ef385492111010L, // 52
        0x70df70a824222120L, // 53
        0xe0bfe05048444241L, // 54
        0xc07fc0a090888482L, // 55

        0xfe03050911214181L, // 56
        0xfd070a1222428202L, // 57
        0xfb0e152444840404L, // 58
        0xf71c2a4988080808L, // 59
        0xef38549211101010L, // 60
        0xdf70a82422212020L, // 61
        0xbfe0504844424140L, // 62
        0x7fc0a09088848281L, // 63
    };

    public static long get(int index) {
        return XRAY_ATTACKS[index];
    }

    public static void generateXrayAttacks() {
        for(int square = 0; square < 64; square++) {
            long attack = 0;
            Coord start = new Coord(square);

            for(Coord dir : Coord.rookDirections) {
                for(byte dist = 1; dist < 8; dist++) {
                    Coord attackCoord = start.add(dir.mul(dist));

                    if(attackCoord.isValid()) {
                        attack = Bit.setBit(attack, attackCoord.getIndex());
                    }
                }
            }

            for(Coord dir : Coord.bishopDirections) {
                for(byte dist = 1; dist < 8; dist++) {
                    Coord attackCoord = start.add(dir.mul(dist));
    
                    if(attackCoord.isValid()) {
                        attack = Bit.setBit(attack, attackCoord.getIndex());
                    }
                }
            }

            if(square % 8 == 0) {
                System.out.println();
            }

            System.out.println("0x" + Long.toHexString(attack) + "L, // " + square);
        }
    }
}
