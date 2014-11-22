package AlgoSolver;

public class Main {

    public static void main(String[] args) {
        
        Map map = new Map();
        
        Parser parser = new Parser("labyrinthe.txt");
        parser.parse(map);
        
        System.out.println(map);
    }
    
}
