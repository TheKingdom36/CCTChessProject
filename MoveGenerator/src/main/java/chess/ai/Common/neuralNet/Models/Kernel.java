package chess.ai.Common.neuralNet.Models;

import java.io.Serializable;
import java.util.Random;

public class Kernel implements Serializable {
    private double[][][] values;
    private int width;
    private int height;
    private int depth;

    public int getDepth() {
        return depth;
    }

    Random rand = new Random();

    public Kernel(int width, int height,int depth){
        values = new double[depth][width][height];
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public double[][][] getValues(){
        return values;
    }

    public void setValues(double[][][] values){
        this.values = values;
    }

    public void setValue(int depth,int width,int height , double value){
        values[depth][width][height] = value;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void SetValue(int depth, int width,int height,double value){
        values[depth][width][height] = value;
    }

    public void InitializeRandomValues(){

        for(int i=0;i<depth;i++){
            for (int j=height-1;j>=0;j--){

                for(int k=0;k<width;k++){
                    // between 0.2 and 0.5
                    values[i][k][j] = 0.3*rand.nextDouble() + 0.2;
                }
            }
        }
    }

    public void Print(){
        for(int i=0;i<depth;i++){
            System.out.println(i +":");
            for (int j=height-1;j>=0;j--){
                System.out.println();
                for(int k=0;k<width;k++){

                    System.out.print(values[i][k][j]+ " ");

                }
            }
        }
    }
}
