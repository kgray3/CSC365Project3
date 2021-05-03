import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.awt.Color;

import javax.imageio.ImageIO;
import javax.sound.midi.Soundbank;

public class Main {
    
    public static void main(String[] args) throws Exception {
        boolean running = true;
        Scanner scanner = new Scanner(System.in);

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("dream.jpg"));
        } catch (IOException e){
            
        }

        while(running) {
            System.out.println("Blackout [b] or segment [s] image? ");
            String choice = scanner.next();

            if(choice.equalsIgnoreCase("b")) {
                blackoutTop(img);
            }
            else if (choice.equalsIgnoreCase("s")) {
                System.out.println("Enter k: ");
                int choice2 = scanner.nextInt();
                imageSegmentation(img, choice2);
            }
            else {
                running = false;
            }
        }
        scanner.close();
        
       

    }
//turn edge arraylist into an array for sorting -- use math to calclate number of edges?
public static void imageSegmentation(BufferedImage img, int k) throws IOException {
    //int k = 300;
    int numRows = img.getHeight();
    int numCols = img.getWidth();

    PixelNode[] vertexArray = new PixelNode[numCols*numRows];
    List<GraphEdge> edgeArray = new ArrayList<GraphEdge>();
    // Graph g = new Graph();
   int edgeIteration = 0;
   int vertexIteration = 0;
    for(int row = 0; row < numRows; row++) {
        for(int col = 0; col < numCols; col++) { //rightwards neighbor
            PixelNode currentVertex = new PixelNode(row, col,new Color(img.getRGB(col, row)));
            vertexArray[vertexIteration] = currentVertex;
            vertexIteration++;
        
            if(col + 1 < numCols) {
                PixelNode temp2 = new PixelNode(row, col+1, new Color(img.getRGB(col+1, row)));
                edgeArray.add(edgeIteration, new GraphEdge(currentVertex,temp2));
                edgeIteration++;
            } //neighbor below
            if(row + 1 < numRows) {
                PixelNode temp2 = new PixelNode(row+1, col,new Color(img.getRGB(col, row+1)));
                edgeArray.add(edgeIteration, new GraphEdge(currentVertex,temp2));
                edgeIteration++;
            } //right diagonal
            if(row + 1 < numRows && col + 1 < numCols) {
                PixelNode temp2 = new PixelNode(row+1, col+1, new Color(img.getRGB(col+1, row+1)));
                edgeArray.add(edgeIteration, new GraphEdge(currentVertex,temp2));
                edgeIteration++;
            } //left diagonal
            if(col - 1 > -1 && row + 1 < numRows) {
                PixelNode temp2 = new PixelNode(row+1, col-1, new Color (img.getRGB(col-1, row+1)));
                edgeArray.add(edgeIteration, new GraphEdge(currentVertex,temp2));
                edgeIteration++;
            }
        }
    }
    edgeArray.sort(Comparator.comparingInt(GraphEdge::getIntensityDifference)); //sorts edges by intensity difference (weight)
    DisjointSetForests d = new DisjointSetForests(vertexArray);
    int count = 0;
    for(GraphEdge currentEdge : edgeArray) {
        
        PixelNode pixelOneParent = d.findSet(currentEdge.getA());
        PixelNode pixelTwoParent = d.findSet(currentEdge.getB());
        if(!(pixelOneParent.toCoords().equalsIgnoreCase(pixelTwoParent.toCoords()))) {
            long thresholdOne = d.getInternalDiff(pixelOneParent) + (k/d.getCardinality(pixelOneParent));
            long thresholdTwo = d.getInternalDiff(pixelTwoParent) + (k/d.getCardinality(pixelTwoParent));
            int dif = currentEdge.getIntensityDifference();

            if(dif <= thresholdOne && dif <= thresholdTwo) {
                count++;
                d.union(currentEdge.getA(), currentEdge.getB(), dif);
            }

        }
    }
    System.out.println("count: " + count);

    for(int a = 0; a < numRows; a++) {
        for(int b = 0; b < numCols; b++) {
            PixelNode currentVertex = new PixelNode(a, b,new Color(img.getRGB(b, a)));
            img.setRGB(b, a, d.getColor(d.findSet(currentVertex)));
        }
    }
    
    File outputfile = new File("output2.jpg");
    ImageIO.write(img, "jpg", outputfile);
    System.out.println("Finished.");

}


public static void blackoutTop(BufferedImage img) throws IOException {
    int numRows = img.getHeight();
        int numCols = img.getWidth();

        for(int x = 0; x < numRows/2; x++) {
            for(int y = 0; y < numCols; y++) {
                img.setRGB(y, x, new Color(0,255,0).getRGB());
            }
        }

        
    File outputfile = new File("output.jpg");
    ImageIO.write(img, "jpg", outputfile);
    
  }
}
