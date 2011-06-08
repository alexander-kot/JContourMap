package ru.eleron.terrain.jContourMap;

import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MapInterpolatorClass implements MapInterpolator{
    private double[] heights;
    private List<Contour> contours = new LinkedList<Contour>();

    public void setHeights(double[] heights) throws IllegalStateException{
        if  (this.heights.length <= heights.length){
            this.heights = heights.clone();
        }else{
            throw new IllegalStateException();
        }
    }


    public double[] getHeights(){
        return heights.clone();
    }


    public void addContour(int hIndex, Contour contour)
            throws ContourIntersectionException{
        contour.setHIndex(hIndex);
        contours.add(contour);
    }


    private Double nearestEdge( Double p, Rectangle2D bounds){
        return new Double( ((p.getX()-bounds.getMinX()-bounds.getWidth()/2  >0)? bounds.getMaxX(): bounds.getMinX()),
                           ((p.getY()-bounds.getMinY()-bounds.getHeight()/2 >0)? bounds.getMaxY(): bounds.getMinY()));
    }


    private int[] spline(double a, double b, double c, double d, long n){
        int[] x = new int[n];
        double t;
        for (int i = 0; i<n; i++){
            t = i/n;
            x[i] = (int) Math.round(0.5*( 2*b +t*( -a +c +t*( 2*a -5*b +4*c -d + t*(  -a +3*b -3*c +d)))));
        }
        return x;

    }


    public void buildMap(double[][] heightMap, Rectangle2D bounds)
            throws InvalidContourConfigurationException{
        // TODO. Оптимизация? Дерево вложенности.
        // TODO. Растеризовать изолинии, дотянуть изолинии до краёв.
        // TODO. Если контур не замкнут и дырка то поведение программы непредсказуемо.

        Arrays.fill(heightMap, java.lang.Double.NEGATIVE_INFINITY);

        for (Contour contour : contours){
            if (contour.isGap()) continue;
            List<Double> points = contour.getPoints();
            if (contour.isClosed()){
                points.add( points.get(0));
                points.add( points.get(1));
                points.add( points.get(2));
            }else{
                //! Попробую обойти проблемы, дотянув все незамкнутые контуры напрямик к краям

                if (bounds.contains(points.get( 0))){
                    points.add( 0, nearestEdge(points.get(0), bounds));
                }
                points.add( 0, points.get(0));

                if (bounds.contains(points.get( points.size()-1))){
                    points.add( nearestEdge(points.get( points.size()-1), bounds));
                }
                points.add( points.get( points.size()-1));
            }


            for (int i = 0; i < points.size()-3; i++){
                // TODO. Место для всяческих оптимизаций с предвычислениями
                //! количество промежуточных точек
                long n = 2*Math.round(Math.abs(points.get(i+1).getX()-points.get(i+2).getX())+
                                   Math.abs(points.get(i+1).getY()-points.get(i+2).getY()));

                int[] X = spline( points.get(i  ).getX(), points.get(i+1).getX(),
                                  points.get(i+2).getX(), points.get(i+3).getX(), n);
                int[] Y = spline( points.get(i  ).getY(), points.get(i+1).getY(),
                                  points.get(i+2).getY(), points.get(i+3).getY(), n);

                for (int t = 0; t<n; t++){
                    if (heightMap[X[t]][Y[t]] == java.lang.Double.NEGATIVE_INFINITY)
                       heightMap[X[t]][Y[t]] = heights[contour.getHIndex()];
                    else
                        if (heightMap[X[t]][Y[t]] == heights[contour.getHIndex()]){}
                        else throw new InvalidContourConfigurationException();
                }
            }
        }// Контуры добавлены
        // TODO. Сформировать правила продления незамкнутых изолиний.
        // TODO. Создать двойной контур.
    }
}