package chess.ai.Common.neuralNet.Models;

import java.util.Random;

public class Map {
    private double[][] values;
    private int width;
    private int height;
    static Random rand = new Random();
    private int paddingSize;

    public Map(int width, int height){
        values = new double[width][height];
        this.width = width;
        this.height = height;
    }

    public Map(int width, int height,boolean randomValues,int min,int max){
        values = new double[width][height];
        this.width = width;
        this.height = height;

        if(randomValues ==true) {

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int in = rand.nextInt(max - min) + min;

                    values[i][j] = in;
                }
            }
        }


    }

    public double[][] getValues(){
        return values;
    }

    public void setValues(double[][] values){
        this.values = values;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setValue(int width, int height ,double newValue){
        values[width][height]= newValue;
    }

    public void addPadding(int paddingSize){
        this.paddingSize = paddingSize;

        double[][] newValues = new double[width+paddingSize*2][height+paddingSize*2];

        int countx=0;
        int county = 0;
        for(int i=paddingSize;i<(newValues.length-paddingSize);i++){
            county=0;
            for(int j=paddingSize;j<newValues[0].length-paddingSize;j++){
                newValues[i][j] = values[countx][county];
                county++;
            }
            countx++;
        }

        width = width+paddingSize*2;
        height = height+paddingSize*2;

        values = newValues;


    }

    public void Print(){
        for(int i=0;i<width;i++){
            System.out.println();
            for(int j=0;j<height;j++){
                System.out.print(values[i][j]+" ");
            }
        }
    }

    public static Map ConvertMapsToMap(Map[] maps, int newMapWidth, int newMapHeight){
        Map map = new Map(newMapWidth,newMapHeight);

        int pos =0;
        int widthPos=0;
        int heightPos=0;
        for(int i=0;i<maps.length;i++){
            for(int j=0;j<maps[0].getWidth();j++){
                for(int k=0;k<maps[0].getHeight();k++){
                    if(pos>newMapWidth*newMapHeight){
                        return map;
                    }
                    if(widthPos==newMapWidth){
                        heightPos++;
                        widthPos=0;

                        if(heightPos>=newMapHeight){
                            return map;
                        }
                    }
                    map.setValue(widthPos,heightPos,maps[i].getValues()[j][k]);
                    pos++;
                    widthPos++;
                }
            }


        }
        return map;
    }
}
