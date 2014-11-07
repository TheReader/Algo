package AlgoSolver;

public class Map {
    private int m_height;
    private MapEnum[] m_map;
    private int m_width;    
    
    Map(int height, int width) {
        m_height = height;
        m_width = width;
        
        m_map = new MapEnum[getHeight() * getWidth()];
    }
    
    public MapEnum at(int x, int y) {
        return m_map[x * getWidth() + y];
    }
    
    public int getHeight() {
        return m_height;
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
    
    public String stringAt(int x, int y) {
        
        String output = new String();
        
        switch (m_map[x * getWidth() + y]) {
            case BEGIN:
                output = "P";
                break;
            case CANDY:
                output = "o";
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
                output = "M";
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
