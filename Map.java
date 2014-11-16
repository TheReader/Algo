package AlgoSolver;

// Should save a height * width of byte -> WALL_UP = 1, WALL_LEFT = 2, WALL_RIGHT = 4, WALL_DOWN = 8, EXIT = 16

public final class Map {
    private final int m_height;
    private final MapEnum[] m_map;
    private int m_numberCandies;
    private int m_numberMonsters;
    private final int m_width;    
    
    Map(int height, int width) {
        m_height = height;
        m_width = width;
        
        m_map = new MapEnum[getHeight() * getWidth()];
        
        m_numberMonsters = 0;
        m_numberCandies = 0;
    }
    
    public MapEnum at(int x, int y) {
        return m_map[x * getWidth() + y];
    }
    
    public int getHeight() {
        return m_height;
    }
    
    public int getNumberOfCandies() {
        return m_numberCandies;
    }
    
    public int getNumberOfMonsters() {
        return m_numberMonsters;
    }  
    
    public int getRealHeight() {
        return m_height / 2;
    }
    
    public int getRealWidth() {
        return m_width / 2;
    }
    
    public int getWidth() {
        return m_width;
    }
    
    public void set(int x, int y, MapEnum value) {
        m_map[x * getWidth() + y] = value;
    }
    
    public void setNumberCandies(int numberCandies) {
        m_numberCandies = numberCandies;
    }
    
    public void setNumberMonsters(int numberMonsters) {
        m_numberMonsters = numberMonsters;
    }
    
    public String stringAt(int x, int y) {
        
        String output = new String();
        
        switch (m_map[x * getWidth() + y]) {
            case BEGIN:
                output = " P ";
                break;
            case CANDY:
                output = " o ";
                break;
            case EMPTY:
                if (y % 2 == 0)
                    output = " ";
                else
                    output = "   ";
                break;
            case EXIT:
                if (y % 2 == 0)
                    output = "E";
                else
                    output = " E ";
                break;
            case MONSTER:
                output = " M ";
                break;
            case WALL:
                if (x % 2 == 0) {
                    if (y % 2 == 0)
                        output = "+";
                    else
                        output = "---";
                }
                else
                    output = "|";
                break;
                
        }
        return output;
    }
    
    @Override
    public String toString() {
        String output = new String();
        
        for (int i = 0; i < getHeight(); ++i)
        {
            for (int j = 0; j < getWidth() - 1; ++j)
            {
                output += stringAt(i, j);
            }
            output += stringAt(i, getWidth() - 1) + "\n";
        }
        
        return output;
    }
}
