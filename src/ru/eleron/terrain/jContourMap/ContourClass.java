package ru.eleron.terrain.jContourMap;

import java.awt.geom.Point2D.Double;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ContourClass implements Contour {
    private List<Double> points = new LinkedList<Double>();
    private boolean isClosed = false;
    private boolean isGap = false;
    private int hIndex = -1;

    public ContourClass(){

    }
 // нужен ли?
    public ContourClass(Double[] points, boolean isClosed, boolean isGap, int hIndex)throws SelfIntersectionException{
        this.isClosed = isClosed;
        this.isGap = isGap;
        this.hIndex = hIndex;

        for(int i=0; i<=points.length; i++){
            this.appendPoint(points[i]);
        }
    }

    public void appendPoint(Double p) throws SelfIntersectionException{
        if (points.size()<2){
            points.add(p);
            return;
        }

        Iterator it = points.iterator();
        boolean intersection = false;
        Double S1, T1, S2, T2;
        double v1,v2,v3,v4;

        S2 =  points.get(points.size()-1); // последний
        T2 = p;
        T1 = (Double)it.next();

        while (it.hasNext()){
            S1 = T1;
            T1 = (Double)it.next();

            // проверка пересечение отрезков с помощью векторного произведения
            //! неверно обработывает пересечение в конечных точках
            v1 = (T2.getX()-S2.getX())*(S1.getY()-S2.getY())-(T2.getY()-S2.getY())*(S1.getX()-S2.getX());
            v2 = (T2.getX()-S2.getX())*(T1.getY()-S2.getY())-(T2.getY()-S2.getY())*(T1.getX()-S2.getX());
            v3 = (T1.getX()-S1.getX())*(S2.getY()-S1.getY())-(T1.getY()-S1.getY())*(S2.getX()-S1.getX());
            v4 = (T1.getX()-S1.getX())*(T2.getY()-S1.getY())-(T1.getY()-S1.getY())*(T2.getX()-S1.getX());
            intersection = intersection | ((v1*v2<0) & (v3*v4<0));

            if (intersection)
                throw new SelfIntersectionException();

        }
        points.add(p);
    }


    public List<Double> getPoints(){
        return Collections.unmodifiableList(points);
    }

    public void setClosed(boolean isClosed){
        this.isClosed = isClosed;
    }


    public boolean isClosed(){
        return this.isClosed;
    }


    public void setGap(boolean isGap){
        this.isGap = isGap;
    }


    public boolean isGap(){
        return this.isGap;
    }

    public int getHIndex(){
        return this.hIndex;
    }

    public void setHIndex(int hIndex){
        this.hIndex = hIndex;
    }


    public boolean intersects(Contour that){
    //TODO. Заглушка - пересечение контуров надо считать при построении карты
        return false;
    }
}

