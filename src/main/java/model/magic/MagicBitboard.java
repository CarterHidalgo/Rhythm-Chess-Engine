package model.magic;

import java.util.ArrayList;
import java.util.Random;

import helper.Bit;
import helper.Coord;
import helper.Printer;
import model.Bitboard;

public class MagicBitboard {
    /*
     * ABOUT: 
     *  An implementation of fancy magic bitboards with individual shifts and no constructive collision 
     *  optimization. 
     * 
     * DEFINE
     *  blockerSet: An in-order set of all possible configurations of blockers for a given square/piece combination
     *      Border squares are excluded since blockers are always attacked and blockers can only change the square
     *      behind them.
     *  config: A specific configuration of blockers pulled from a blockerSet.
     *  attackSet: An in-order set of all possible configuations of attacks given a square/piece/config combination.
     *  attack: A specific bitboard of possible attacks pulled from the attackSet.
     *  major: A boolean value where true represents rook movement and false represents bishop movement.
     *  base-magics: Magic numbers where the resulting 1D array of attacks = blockerSet.length
     *      (in other words every config maps to a unique index and many attack maps are redundently stored)
     */

    private static final Random random = new Random();
    private static final int rookSize = 102400; // portion of table that is for rooks in bytes
    private static final int bishopSize = 5248; // portion of table that is for bishops in bytes
    private static long[] table = new long[rookSize + bishopSize]; // rooks first bishops second | for all precomputed moves
    private static int[] offsets = new int[128];
    private static final int[] rookShift =  {
        52, 53, 53, 53, 53, 53, 53, 52,
        53, 54, 54, 54, 54, 54, 54, 53,
        53, 54, 54, 54, 54, 54, 54, 53,
        53, 54, 54, 54, 54, 54, 54, 53,
        53, 54, 54, 54, 54, 54, 54, 53,
        53, 54, 54, 54, 54, 54, 54, 53,
        53, 54, 54, 54, 54, 54, 54, 53,
        52, 53, 53, 53, 53, 53, 53, 52
    };
    private static final int[] bishopShift = {
        58, 59, 59, 59, 59, 59, 59, 58,
        59, 59, 59, 59, 59, 59, 59, 59,
        59, 59, 57, 57, 57, 57, 59, 59,
        59, 59, 57, 55, 55, 57, 59, 59,
        59, 59, 57, 55, 55, 57, 59, 59,
        59, 59, 57, 57, 57, 57, 59, 59,
        59, 59, 59, 59, 59, 59, 59, 59,
        58, 59, 59, 59, 59, 59, 59, 58
    };
    private static int tableIndex = 0;

    public static void init(boolean printNewMagics) {
        // find base-magics
        if(printNewMagics) {
            createRookMagics();
            createBishopMagics();
        }

        for(int square = 0; square < 64; square++) {
            createTable(square, MagicNumbers.ROOK_MAGICS[square], rookShift[square], true);
        }
        
        for(int square = 0; square < 64; square++) {
            createTable(square, MagicNumbers.BISHOP_MAGICS[square], bishopShift[square], false);
        }

        printTableSize(table);
    }

    public static long getRookMoves(int square) {
        return table[(int) (offsets[square] + transform(Bitboard.getBitboard("occupied") & MagicBitboardMask.getRookMask(square), MagicNumbers.ROOK_MAGICS[square], rookShift[square]))];
    }

    public static long getBishopMoves(int square) {
        return table[(int) (offsets[square + 64] + transform(Bitboard.getBitboard("occupied") & MagicBitboardMask.getBishopMask(square), MagicNumbers.BISHOP_MAGICS[square], bishopShift[square]))];
    }

    private static void createTable(int square, long magic, int shift, boolean major) {
        int majorOffset = (major) ? 0 : 64;
        long index, attack;
        long mask = (major) ? MagicBitboardMask.getRookMask(square) : MagicBitboardMask.getBishopMask(square);
        long[] blockerSet = createBlockerSet(mask);

        offsets[square + majorOffset] = tableIndex;
        
        for(long config : blockerSet) {
            index = transform(config, magic, shift);
            attack = createAttack(square, config, major);
            
            table[(int) (offsets[square + majorOffset] + index)] = attack;
        }

        tableIndex += blockerSet.length;
    }

    private static void createRookMagics() {
        ArrayList<Long> magicNumbers = new ArrayList<>();
        
        for(int square = 0; square < 64; square++) {
            long rookMask = MagicBitboardMask.getRookMask(square);
            long[] blockerSet = createBlockerSet(rookMask);
            long[] attackSet = createAttackSet(square, blockerSet, true);

            magicNumbers.add(findMagicNumber(square, rookMask, blockerSet, attackSet));

            System.out.print(".");
        }

        System.out.println("\npublic static final long[] ROOK_MAGICS = {");
        for(long magic : magicNumbers) {
            System.out.print(magic + "L, ");
        }
        System.out.println("};");
    }

    private static void createBishopMagics() {
        ArrayList<Long> magicNumbers = new ArrayList<>();

        for(int square = 0; square < 64; square++) {
            long bishopMask = MagicBitboardMask.getBishopMask(square);
            long[] blockerSet = createBlockerSet(bishopMask);
            long[] attackSet = createAttackSet(square, blockerSet, false);

            magicNumbers.add(findMagicNumber(square, bishopMask, blockerSet, attackSet));

            System.out.print(".");
        }

        System.out.println("\npublic static final long[] BISHOP_MAGICS = {");
        for(long magic : magicNumbers) {
            System.out.print(magic + "L, ");
        }
        System.out.println("};");
    }

    private static long findMagicNumber(int square, long mask, long[] blockerSet, long[] attackSet) {
        boolean fail = false;
        int numMaskBits = Long.bitCount(mask);
        int shift = 64 - numMaskBits;
        int magicIndex;
        long trials = 1_000_000_000;
        long magic;
        long[] used = new long[(1 << numMaskBits)];

        for(int k = 0; k < trials; k++) {
            // generate a possible magic number 
            magic = randomSparseLong();

            // skip if negative
            if(magic < 0) {
                continue;
            }
            
            // skip if "unsuitable" magic was generated
            if(Long.bitCount((mask * magic) & 0xFF00000000000000L) < 6) {
                continue;
            }
            
            // reset for a new attempt (we are in a loop)
            fail = false;
            for(int i = 0; i < (1 << numMaskBits); i++) {
                used[i] = 0L;
            }
            
            // loop though all configs to generate each configs index and map index -> attack
            // (equivalent to mapping configs -> attack)
            for(int configIndex = 0; !fail && configIndex < (1 << numMaskBits); configIndex++) {
                // magic index using the current config, the possible magic, and the shift for this square (dependent on numMaskBits)
                // numMaskBits represents the number of bits in magicIndex
                magicIndex = transform(blockerSet[configIndex], magic, shift);

                // if array index is 0 something went wrong; negative will cause ArrayIndexOutOfBounds
                if(magicIndex < 0) {
                    System.out.println("Cannot have a negative index when generating magics; shutting down.");
                    System.exit(1);
                }

                if(used[magicIndex] == 0L) {
                    // if no attack is associated with the current magicIndex...
                    // store the attack that corresponds with the current config
                    used[magicIndex] = attackSet[configIndex]; 
                } else if(used[magicIndex] != attackSet[configIndex]) {
                    // if there is a value already associated with the magicIndex for the current config
                    // and the attack sets are not equal we have a destructive collision and the magic fails
                    fail = true;
                }
            }
            
            if(!fail) {
                return magic;
            }
        }

        System.out.println("Failed to find a magic for square " + square + "; shutting down.");
        System.exit(1);

        return 0;
    }

    private static int transform(long config, long magic, int shift) {
        long mul = config * magic;
        long result = mul >>> (shift);
        
        return (int) (result & 0xFFFFFFFFL);
    }

    private static long[] createBlockerSet(long mask) {
        /*
        * maxNumConfigs is dependent on what the square is and which piece we are considering.
        * Blocker set size will be 2^value where value is determined by the number of bits in the
        * pice mask. 
        */
        int maxNumConfigs = (int) Math.pow(2, Long.bitCount(mask));
        long[] blockerBitboards = new long[maxNumConfigs];
        long config = 0L;
        int configIndex = 0;

        // Carry-Rippler trick to enumerate non-contiguous subsets such as blockers (only enumerate inside the mask)
        do {
            blockerBitboards[configIndex++] = config;
            config = (config - mask) & mask;
        } while(config != 0);

        return blockerBitboards;
    }

    private static long[] createAttackSet(int index, long[] blockerSet, boolean major) {
        long[] attackSet = new long[blockerSet.length];
        int attackIndex = 0;

        for(long config : blockerSet) {
            attackSet[attackIndex++] = createAttack(index, config, major);
        }

        return attackSet;
    }

    private static long createAttack(int index, long config, boolean major) {
        // Create an attack bitboard for a given square, specific blocker config, and a piece major
        long attack = 0;

        Coord[] directions = (major) ? Coord.rookDirections : Coord.bishopDirections;
        Coord startSquare = new Coord(index);
        
        for(Coord dir : directions) {
            for(int dist = 1; dist < 8; dist++) {
                Coord attackCoord = startSquare.add(dir.mul(dist));
                
                // Detects board falloff using rank and file incrementation rather than index offsets
                if(attackCoord.isValid()) {
                    attack = Bit.setBit(attack, attackCoord.getIndex());
                    
                    if(Bit.isSet(config, attackCoord.getIndex())) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }

        return attack;
    }

    private static long randomLong() {
        long one, two, three, four;
        
        one = (long) (random.nextInt() & 0xFFFF);
        two = (long) (random.nextInt() & 0xFFFF);
        three = (long) (random.nextInt() & 0xFFFF);
        four = (long) (random.nextInt() & 0xFFFF);

        return one | (two << 16) | (three << 32) | (four << 48);
    }

    private static long randomSparseLong() {
        return randomLong() & randomLong();
    }

    private static void printTableSize(long[] array) {
        long sizeInBytes = (array.length * 8 + 12) + ((array.length * 8 + 12) % 8);
        double sizeInKilobytes = sizeInBytes / 1000.0;
        double sizeInMegabytes = sizeInKilobytes / 1_000_000.00;
        
        if(sizeInMegabytes >= 1) {
            System.out.printf("Size of the table: %.2f MB%n", sizeInMegabytes);
        } else if (sizeInKilobytes >= 1) {
            System.out.printf("Size of the table: %.2f KB | %.2f KiB%n", sizeInKilobytes, (sizeInKilobytes / 1.024));
        } else {
            System.out.printf("Size of the table: %d bytes%n", sizeInBytes);
        }
    }
}

/*

For square 63
...............
Previous best: 4096
New best: 2304
Magic: 1200772246015809234
.........................
Previous best: 2304
New best: 2176
Magic: -6227352389551664102




 */