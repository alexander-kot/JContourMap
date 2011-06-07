package ru.eleron.terrain.jContourMap;

import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class MapInterpolatorClass implements MapInterpolator{
    private double[] heights;
    private List<Contour> contours = new LinkedList<Contour>();

    public void setHeights(double[] heights) throws IllegalStateException{
        if  (this.heights.length <= heights.length){
            this.heights = heights;
        }else{
            throw new IllegalStateException();
        }
    }


    public double[] getHeights(){
        //! попытка вернуть массив по значению
        double[] out = new double[heights.length];
        System.arraycopy(heights, 0, out, 0, heights.length);
        return out;
    }


    public void addContour(int hIndex, Contour contour)
            throws ContourIntersectionException{
        contour.setHIndex(hIndex);
        contours.add(contour);
    }


    public void buildMap(double[][] heightMap, Rectangle2D bounds)
            throws InvalidContourConfigurationException{
        // TODO. Оптимизация? Дерево вложенности.
        // TODO. Растеризовать изолинии, дотянуть изолинии до краёв.
        // TODO. Если контур не замкнут и дырка то поведение программы непредсказуемо.

        Arrays.fill(heightMap, java.lang.Double.NEGATIVE_INFINITY);
        ListIterator<Contour> iterator = contours.listIterator();
        Contour contour;

        while (iterator.hasNext()){
            contour = iterator.next();
            if (contour.isGap()) continue;
            Double[] points;
            Double[] pointsArray = (Double[])contour.getPoints().toArray();
            if (contour.isClosed()){
                points = Arrays.copyOfRange(pointsArray, 0, pointsArray.length+3,Double[].class);
                points[ pointsArray.length+1] = pointsArray[0];
                points[ pointsArray.length+2] = pointsArray[1];
                points[ pointsArray.length+3] = pointsArray[2];
            }else{
                //! не получается так изящно как выше
                //!Warning Manual array copy
                points = new Double[pointsArray.length+2];
                points[0]=pointsArray[0];
                for(int i=0;i<=pointsArray.length;i++){points[i+1] = pointsArray[i];}
                points[pointsArray.length+1]=pointsArray[pointsArray.length];
            }
            for (int index = 0; index< points.length-3; index++){
                // TODO. Место для всяческих оптимизаций с предвычислениями
                long n = 2*Math.round(Math.abs(points[index+1].x-points[index+2].x)+
                                   Math.abs(points[index+1].y-points[index+2].y));
                for (double T = 0; T<=n; T++){
                    double t = T/n;
                    int X = (int) Math.round(
                               0.5 * ((                   2*points[index+1].x) +
                                 t * (( -points[index].x                        +points[index+2].x) +
                             t * t * ((2*points[index].x -5*points[index+1].x +4*points[index+2].x -points[index+3].x) +
                         t * t * t * (  -points[index].x +3*points[index+1].x -3*points[index+2].x +points[index+3].x)))));
                    int Y = (int) Math.abs(
                               0.5 * ((                   2*points[index+1].y) +
                                 t * (( -points[index].y                        +points[index+2].y) +
                             t * t * ((2*points[index].y -5*points[index+1].y +4*points[index+2].y -points[index+3].y) +
                         t * t * t * (  -points[index].y +3*points[index+1].y -3*points[index+2].y +points[index+3].y)))));

                    if (heightMap[X][Y] == java.lang.Double.NEGATIVE_INFINITY)
                       heightMap[X][Y] = heights[contour.getHIndex()];
                    else
                        if (heightMap[X][Y] == heights[contour.getHIndex()]){}
                        else throw new InvalidContourConfigurationException();
                }
            }
        }// Контуры добавлены
        // TODO. Сформировать правила продления незамкнутых изолиний.
        // TODO. Создать двойной контур.
    }
}