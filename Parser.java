package AlgoSolver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Parser {
    
    private BufferedReader m_bufferReader;
    private final String m_fileName;
    
    public Parser(String fileName) {
        m_bufferReader = null;
        m_fileName = fileName;
    }
    
    private void readHeader(int[] sizes) throws IOException {
        String data = m_bufferReader.readLine();
        
        try {
            // Regex => text: blancs nombre text blanc nombre
            Scanner in = readInt(data, "\\w*:\\s+\\d+\\s+\\w*\\s+\\d+\\s*");
            sizes[0] = in.nextInt(); 
            sizes[1] = in.nextInt(); 
        } catch (IOException e) {
            throw new IOException("Error while reading the header of: " + m_fileName);
        }
    }
    
    private void readInformations(Map map) throws IOException {
        m_bufferReader.readLine();
        String data = m_bufferReader.readLine();
        
        // Blanc text: blanc nombre
        try {
            // Blanc text: blanc nombre
            map.setNumberMonsters(readInt(data, "\\s+\\w*:\\s+\\d+\\s*").nextInt());
            data = m_bufferReader.readLine();
            map.setNumberCandies(readInt(data, "\\s+\\w*:\\s+\\d+\\s*").nextInt());
            
            // Emplacements:
            data = m_bufferReader.readLine();
            data = m_bufferReader.readLine();

            // Blanc text: blanc (nombre, nombre) -> Pakkuman
            Scanner in = readInt(data, "\\s+\\w*:\\s+\\(\\d+,\\d+\\)\\s*");
            map.set(in.nextInt(), in.nextInt(), MapEnum.BEGIN);
            
            data = m_bufferReader.readLine();
            // Blanc text: blanc (nombre, nombre) (nombre, nombre) ... -> Monster
            String regex = "\\s+\\w*:";
            for (int i = 0; i < map.getNumberOfMonsters(); ++i) {
                regex += "\\s+\\(\\d+,\\d+\\)";
            }
            in = readInt(data, regex + "\\s*");
            for (int i = 0; i < map.getNumberOfMonsters(); ++i) {
                map.set(in.nextInt(), in.nextInt(), MapEnum.MONSTER);                
            }

            data = m_bufferReader.readLine();
            // Blanc text: blanc (nombre, nombre) (nombre, nombre) ... -> Bonbons
            regex = "\\s+\\w*:";
            for (int i = 0; i < map.getNumberOfCandies(); ++i) {
                regex += "\\s+\\(\\d+,\\d+\\)";
            }
            in = readInt(data, regex + "\\s*");
            for (int i = 0; i < map.getNumberOfCandies(); ++i) {
                map.set(in.nextInt(), in.nextInt(), MapEnum.CANDY);                
            }
        } catch (IOException e) {
            throw new IOException("Error while reading the information:" + data + " Data non conform");
        }
    }
    
    private void readLabyrinth(Map map) throws IOException {
	String data;
	for(int current_line=0;current_line<=map.getHeight()*2;++current_line){
		data = m_bufferReader.readLine();

// "+---" ou "+   " prend une taille de 2 * la largeur + le "+" final.
		if (data.length() != (map.getWidth() * 2 - 1))
			throw new IOException("The labyrinth is not conform");

		for(int current_col=0, x=2;current_col<map.getWidth();x+4, ++current_col){
			switch(data.charAt(x)){
				case '-':
					if(current_line!=(map.getHeight()*2)-1)
						map.set(current_line+1, current_col, MapEnum.WALL_UP);
					if(current_line!=0)
						map.set(current_line-1, current_col, MapEnum.WALL_DOWN);
				case ' ':
					if(current_line%2==1)//si nous sommes dans une case
						if(current_col==0 && data.charAt(j-2)==' ')//s'il y une ouverture a gauche
							map.set(current_line, current_col, MapEnum.EXIT);
						else if(current_col==map.getWidth()-1 && data.charAt(j+2)==' ')//s'il y a une ouverture a droite
							map.set(current_line, current_col, MapEnum.EXIT);
						else
							map.set(current_line, current_col, MapEnum.EMPTY);
							if(data.charAt(j+2)=='|')
								map.set(current_line, current_col, MapEnum.WALL_RIGHT)
							if(data.charAt(j-2)=='|')
								map.set(current_line, current_col, MapEnum.WALL_LEFT)
					else//si nous sommes sur un mur
						if(current_line==0)//si nous sommes au mur supérieur, il y a une ouverture vers le haut
							map.set(current_line+1, current_col, MapEnum.EXIT)
						else if(current_line==(map.getHeight()*2)-1)//si nous sommes sur le mur inférieur, il y a une ouverture vers le bas
							map.set(current_line-1, current_col, MapEnum.EXIT);
				}
		}
	}
}
    
    
    public Scanner readInt(String data, String regex) throws IOException {
        if (!data.matches(regex))
            throw new IOException();
        
        Scanner in = new Scanner(data).useDelimiter("[^0-9]+");
        return in;
    }
    
    public void parse(Map map) {
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
            map.setSizes(sizes[0], sizes[1]);
            
            try {
                // On remplit le labyrinthe avec les vides et les murs.
                readLabyrinth(map);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return;
            }
            
            try {
                // On remplit le labyrinthe avec les informations des positions.
                readInformations(map);
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
