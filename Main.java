package AlgoSolver;

public class Main {

    public static void main(String[] args) {
        
        Parser parser = new Parser("labyrinthe.txt");
        parser.parse();
        
        Map map = parser.getMap();
        System.out.println(map);
    }
    
}
