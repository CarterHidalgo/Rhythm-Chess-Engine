package helper;

public class FEN {
    public static int charToPieceID(char c) {
        int ID = 0;

        if(Character.isUpperCase(c)) {
            ID += 8;
        } else {
            ID += 16;
        }

        switch(Character.toLowerCase(c)) {
            case 'p':
                ID += 6;
            break;
            case 'n':
                ID += 4;
            break;
            case 'b':
                ID += 3;
            break;
            case 'r':
                ID += 5;
            break;
            case 'q':
                ID += 2;
            break;
            case 'k':
                ID += 1;
            break;
            default:
                System.out.println("Error in \"FEN.java\": Expected valid piece char");
                return -1;
        }

        return ID;
    }
}
