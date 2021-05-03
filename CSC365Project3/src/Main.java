import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.awt.Color;

import javax.imageio.ImageIO;

public class Main {
    
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        BufferedImage img = null;
        System.out.println("Enter filename: ");
        String fileName = scanner.next();
        try {
            img = ImageIO.read(new File(fileName));
        } catch (IOException e){
            
        }

        
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
        
        SetRep pixelOneParent = d.findSet(currentEdge.getA().toCoords());
        SetRep pixelTwoParent = d.findSet(currentEdge.getB().toCoords());
        int internalDiffOne = pixelOneParent.getSetRepInternalDiff();
        int internalDiffTwo = pixelTwoParent.getSetRepInternalDiff();

        if(!(pixelOneParent.getSetRepCoords().equalsIgnoreCase(pixelTwoParent.getSetRepCoords()))) {
            long thresholdOne = internalDiffOne + (k/pixelOneParent.getSetRepCardinality());
            long thresholdTwo = internalDiffTwo + (k/pixelTwoParent.getSetRepCardinality());
            int dif = currentEdge.getIntensityDifference();

            //different calls to union based on what will be the new maximum internal difference
            if(dif <= thresholdOne && dif <= thresholdTwo) {
                count++;
                //case 1: Edge A's MInt becomes the new MInt
                if(internalDiffOne > dif && internalDiffOne > internalDiffTwo) { 
                   d.union(currentEdge.getA().toCoords(), currentEdge.getB().toCoords(), internalDiffOne); 
                }
                //case 2: Edge B's MInt becomes the new MInt 
                else if (internalDiffTwo > dif && internalDiffTwo > internalDiffOne) {
                    d.union(currentEdge.getA().toCoords(), currentEdge.getB().toCoords(), internalDiffTwo);
                } else { //case 3: the external difference becomes the new MInt
                    d.union(currentEdge.getA().toCoords(), currentEdge.getB().toCoords(), dif);
                }
            }

        }
    }
    System.out.println("Union count: " + count);

    for(int a = 0; a < numRows; a++) {
        for(int b = 0; b < numCols; b++) {
            PixelNode currentVertex = new PixelNode(a, b,new Color(img.getRGB(b, a)));
            SetRep cVertexParent = d.findSet(currentVertex.toCoords());
            img.setRGB(b, a, cVertexParent.getSetRepColor().getRGB());
           
        }
    }
    
    File outputfile = new File("imagesegmentation.jpg");
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

        
    File outputfile = new File("greenout.jpg");
    ImageIO.write(img, "jpg", outputfile);
    
  }
}
