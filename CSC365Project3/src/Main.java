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
        Scanner scanner = new Scanner(System.in); //initialize scanner

        BufferedImage img = null;
        System.out.println("Enter filename: "); //image to be manipulated
        String fileName = scanner.next();
        try {
            img = ImageIO.read(new File(fileName)); //reads image
        } catch (IOException e){
            
        }
            System.out.println("Blackout [b] or segment [s] image? "); //user input for image manipulation choice
            String choice = scanner.next();

            if(choice.equalsIgnoreCase("b")) {
                blackoutTop(img); //perform "blackout" for choice 'b'
            }
            else if (choice.equalsIgnoreCase("s")) {
                System.out.println("Enter k: "); //set 'k' for image segmentation
                int choice2 = scanner.nextInt();
                imageSegmentation(img, choice2); //perform image segmentation
            } 
        scanner.close(); //close scanner
        
       

    }
//GRAPH-BASED IMAGE SEGMENTATION ALGORITHM
public static void imageSegmentation(BufferedImage img, int k) throws IOException {
    int numRows = img.getHeight(); //image pixel height (rows)
    int numCols = img.getWidth(); //image pixel width (columns)

    PixelNode[] vertexArray = new PixelNode[numCols*numRows]; //# of vertices = rows * cols -- graph vertices
    List<GraphEdge> edgeArray = new ArrayList<GraphEdge>(); //ArrayList for # of graph edges
    
   int edgeIteration = 0; //counter to track edge iterations
   int vertexIteration = 0; //counter to track vertext iterations

   //For loop to build graph using vertex and edge arrays
    for(int row = 0; row < numRows; row++) {
        for(int col = 0; col < numCols; col++) { 
            PixelNode currentVertex = new PixelNode(row, col,new Color(img.getRGB(col, row))); //init current vertex as PixelNode object
            vertexArray[vertexIteration] = currentVertex; //add vertex to array
            vertexIteration++; //iterate vertex #
        
            if(col + 1 < numCols) { //add edge between currentVertex and rightwards neighbor
                PixelNode temp2 = new PixelNode(row, col+1, new Color(img.getRGB(col+1, row)));
                edgeArray.add(edgeIteration, new GraphEdge(currentVertex,temp2));
                edgeIteration++;
            } 
            if(row + 1 < numRows) { //add edge between currentVertex and neighbor below
                PixelNode temp2 = new PixelNode(row+1, col,new Color(img.getRGB(col, row+1)));
                edgeArray.add(edgeIteration, new GraphEdge(currentVertex,temp2));
                edgeIteration++;
            }
            if(row + 1 < numRows && col + 1 < numCols) { //add edge between currentVertex and lower right diagonal neighbor
                PixelNode temp2 = new PixelNode(row+1, col+1, new Color(img.getRGB(col+1, row+1)));
                edgeArray.add(edgeIteration, new GraphEdge(currentVertex,temp2));
                edgeIteration++;
            }
            if(col - 1 > -1 && row + 1 < numRows) { //add edge between currentVertex and lower left diagonal neighbor
                PixelNode temp2 = new PixelNode(row+1, col-1, new Color (img.getRGB(col-1, row+1)));
                edgeArray.add(edgeIteration, new GraphEdge(currentVertex,temp2));
                edgeIteration++;
            }
        }
    }
    //sorts edges in non-decreasing order by intensity difference (edge weight)
    edgeArray.sort(Comparator.comparingInt(GraphEdge::getIntensityDifference));
    DisjointSetForests d = new DisjointSetForests(vertexArray); //create disjoint set forest
    int count = 0; //counter for # of set unions

    //iterate through each graph edge for image segmentation
    for(GraphEdge currentEdge : edgeArray) {
        
        SetRep pixelOneParent = d.findSet(currentEdge.getA().toCoords()); //set representative for vertex 1
        SetRep pixelTwoParent = d.findSet(currentEdge.getB().toCoords()); //set representative for vertex 2
        int internalDiffOne = pixelOneParent.getSetRepInternalDiff(); //MInt for vertex 1's set rep
        int internalDiffTwo = pixelTwoParent.getSetRepInternalDiff(); //MInt for vertex 2's set rep

        //if the set representatives are not the same (dealing w/ 2 different sets)
        if(!(pixelOneParent.getSetRepCoords().equalsIgnoreCase(pixelTwoParent.getSetRepCoords()))) {
            long thresholdOne = internalDiffOne + (k/pixelOneParent.getSetRepCardinality()); //calculate threshold for set one
            long thresholdTwo = internalDiffTwo + (k/pixelTwoParent.getSetRepCardinality()); //calculate threshold for set two
            int dif = currentEdge.getIntensityDifference(); //outer edge weight (Euclidean distance between the two pixel's rgb values)

            //different calls to union based on what will be the new maximum internal difference
            if(dif <= thresholdOne && dif <= thresholdTwo) { //threshold check "if <= MInt + k/|C|"
                count++; //union count
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

    //Iterate through image again to set colors based on image segmentation
    for(int a = 0; a < numRows; a++) {
        for(int b = 0; b < numCols; b++) {
            PixelNode currentVertex = new PixelNode(a, b,new Color(img.getRGB(b, a)));
            SetRep cVertexParent = d.findSet(currentVertex.toCoords());
            img.setRGB(b, a, cVertexParent.getSetRepColor().getRGB()); //set color based on set representative's color
           
        }
    }
    
    //create segmented image file
    File outputfile = new File("imagesegmentation.jpg");
    ImageIO.write(img, "jpg", outputfile);
    System.out.println("Finished.");

}

//Method to "blackout" top 50% of image (it's more of a 'green-out')
public static void blackoutTop(BufferedImage img) throws IOException {
    int numRows = img.getHeight(); //rows = image height
        int numCols = img.getWidth(); //columns = image width

        for(int x = 0; x <= numRows/2; x++) { //only go through half of the rows (top 50%)
            for(int y = 0; y < numCols; y++) {
                //color top half green
                img.setRGB(y, x, new Color(0,255,0).getRGB()); //for some reason the color vals are reversed for BufferedImage
            }
        }
    
    //create output image
    File outputfile = new File("greenout.jpg");
    ImageIO.write(img, "jpg", outputfile);
    
  }
}
