package AlgoSolver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Parser {
    
    private BufferedReader m_bufferReader;
    private final String m_fileName;
    private Map m_map;
    
    public Parser(String fileName) {
        m_bufferReader = null;
        m_fileName = fileName;
        m_map = null;
    }
    
    public Map getMap() {
        return m_map;
    }
    
    private void readHeader(int[] sizes) throws IOException {
        String data = m_bufferReader.readLine();
        
        try {
            // Regex => text: blancs nombre text blanc nombre
            Scanner in = readInt(data, "\\w*:\\s+\\d+\\s+\\w*\\s+\\d+\\s*");
            sizes[0] = in.nextInt() * 2 + 1; 
            sizes[1] = in.nextInt() * 2 + 1; 
        } catch (IOException e) {
            throw new IOException("Error while reading the header of: " + m_fileName);
        }
    }
    
    private void readInformations() throws IOException {
        m_bufferReader.readLine();
        String data = m_bufferReader.readLine();
        
        // Blanc text: blanc nombre
        try {
            // Blanc text: blanc nombre
            m_map.setNumberMonsters(readInt(data, "\\s+\\w*:\\s+\\d+\\s*").nextInt());
            data = m_bufferReader.readLine();
            m_map.setNumberCandies(readInt(data, "\\s+\\w*:\\s+\\d+\\s*").nextInt());
            
            // Emplacements:
            data = m_bufferReader.readLine();
            data = m_bufferReader.readLine();

            // Blanc text: blanc (nombre, nombre) -> Pakkuman
            Scanner in = readInt(data, "\\s+\\w*:\\s+\\(\\d+,\\d+\\)\\s*");
            // * 2 + 1 car dimensions intérieures ...
            m_map.set(in.nextInt() * 2 + 1, in.nextInt() * 2 + 1, MapEnum.BEGIN);
            
            data = m_bufferReader.readLine();
            // Blanc text: blanc (nombre, nombre) (nombre, nombre) ... -> Monster
            String regex = "\\s+\\w*:";
            for (int i = 0; i < m_map.getNumberOfMonsters(); ++i) {
                regex += "\\s+\\(\\d+,\\d+\\)";
            }
            in = readInt(data, regex + "\\s*");
            for (int i = 0; i < m_map.getNumberOfMonsters(); ++i) {
                m_map.set(in.nextInt() * 2 + 1, in.nextInt() * 2 + 1, MapEnum.MONSTER);                
            }

            data = m_bufferReader.readLine();
            // Blanc text: blanc (nombre, nombre) (nombre, nombre) ... -> Bonbons
            regex = "\\s+\\w*:";
            for (int i = 0; i < m_map.getNumberOfCandies(); ++i) {
                regex += "\\s+\\(\\d+,\\d+\\)";
            }
            in = readInt(data, regex + "\\s*");
            for (int i = 0; i < m_map.getNumberOfCandies(); ++i) {
                m_map.set(in.nextInt() * 2 + 1, in.nextInt() * 2 + 1, MapEnum.CANDY);                
            }
        } catch (IOException e) {
            throw new IOException("Error while reading the information:" + data + " Data non conform");
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
    
    public Scanner readInt(String data, String regex) throws IOException {
        if (!data.matches(regex))
            throw new IOException();
        
        Scanner in = new Scanner(data).useDelimiter("[^0-9]+");
        return in;
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
                readLabyrinth();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return;
            }
            
            try {
                // On remplit le labyrinthe avec les informations des positions.
                readInformations();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
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