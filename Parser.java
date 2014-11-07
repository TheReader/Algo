package AlgoSolver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
    
    private BufferedReader m_bufferReader;
    private final String m_fileName;
    private Map m_map;
    
    public Parser(String fileName) {
        m_bufferReader = null;
        m_fileName = fileName;
        m_map = null;
    }
    
    private void readHeader(int[] sizes) throws IOException {
        String data = m_bufferReader.readLine();

        // Regex => text: blancs nombre blancs text blancs nombre.
        boolean matches = data.matches("(\\w*)(:)(\\s+)(\\d+)(\\s+)(\\w*)(\\s+)(\\d+)");
        if (!matches)
            throw new IOException("Error while reading the header of: " + m_fileName);

        String[] tokens = data.split("(\\s+)");

        int i = 0;
        for (String token : tokens) {
            // Si le string est un nombre
            if (token.matches("(\\d+)")) {
                // les dimensions données sont celles intérieures, si bordures -> * 2 + 1.
                sizes[i] = Integer.parseInt(token) * 2 + 1; 
                ++i;
            }
        }
    }
    
    private void readLabyrinth() throws IOException {
        String data;
        for (int x = 0; x < m_map.getHeight(); ++x) {
            data = m_bufferReader.readLine();
            
            // "+---" ou "+   " prend une taille de 2 * la largeur + le "+" final.
            if (data.length() != (m_map.getWidth() * 2 - 1))
                throw new IOException("The labyrinth is not conform");

            for (int j = 0, y = 0; j < data.length(); ++j, ++y) {                
                if (j % 2 == 1)
                    // On vérifie qu'on a bien une suite de "---" ou de "   " entre chaque "+", "|" ou " ".
                    if (!(data.substring(j, j + 3).equals("---") || data.substring(j, j + 3).equals("   ")))
                        throw new IOException("The labyrinth is not conform");
                
                switch (data.charAt(j)) {
                    case '+':
                    case '-':
                    case '|':
                        m_map.set(x, y, MapEnum.WALL);
                        break;
                    case ' ':
                        // Si la sortie est sur un bord (haut/bas) du labyrinthe. -> "+---+EXIT+---+"
                        if (x == 0 || x == m_map.getHeight() - 1)
                            m_map.set(x, y, MapEnum.EXIT);
                        // Si la sortie est sur un bord (gauche/droie) du labyrinthe et sur une rangée impaire. -> "EXIT   |"
                        else if (x % 2 == 1 && (j == 0 || j == data.length() - 1) && data.charAt(j) == ' ')
                            m_map.set(x, y, MapEnum.EXIT);
                        else
                            m_map.set(x, y, MapEnum.EMPTY);
                        break;
                    default:
                        break;
                }
                // "+---+" On saute du 1 au 3, puis ++j du for -> on se retrouve au "+" (4) qui est pair.
                if (j % 2 == 1)
                    j += 2;
            }
        }
    }
    
    public void parse() {

        try {

            m_bufferReader = new BufferedReader(new FileReader(m_fileName));
            
            int[] sizes = new int[2];
            try {
                // On récupère les dimensions du labyrinthe dans le header du fichier.
                // Exception est lancée si le header n'est pas "standard".
                readHeader(sizes);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return;
            }
            
            // Une map est initialisée de taille (hauteur * 2 + 1, largeur * 2 + 1) avec des null.
            m_map = new Map(sizes[0], sizes[1]);
            
            try {
                // On remplit le labyrinthe avec les vides et les murs.
                // Exception est lancée si ligne du labyrinthe n'est pas "standard".
                readLabyrinth();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return;
            }
            
            System.out.println(m_map);
            System.out.println(m_map.getRealHeight() + " " + m_map.getRealWidth());

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + m_fileName);
        } finally {
            try {
                if (m_bufferReader != null)
                    m_bufferReader.close();
            } catch (IOException e) {
                System.out.println("Error while closing: " + m_fileName);
            }
        }
    }
}