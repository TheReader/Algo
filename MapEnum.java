package AlgoSolver;

public enum MapEnum {
    CANDY,
    EMPTY,
    EXIT,
    MONSTER,
    WALL_DOWN,
    WALL_LEFT,
    WALL_RIGHT,
    WALL_UP;
    
    public static int getValue(MapEnum enumeration) {
        int value = -1;
        int i = 0;
        
        for (MapEnum it : MapEnum.values()) {
            if (it == enumeration) {
                value = i;
            }
            ++i;
        }
        
        return value;
    }
}