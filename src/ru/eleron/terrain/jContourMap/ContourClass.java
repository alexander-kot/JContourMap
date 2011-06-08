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

        for (Double point : points) {
            this.appendPoint(point);
        }
    }

    private boolean segmentsIntersect(Double start1, Double end1, Double start2, Double end2){
        double v1,v2,v3,v4;
        v1 = (end2.getX()-start2.getX())*(start1.getY()-start2.getY())-(end2.getY()-start2.getY())*(start1.getX()-start2.getX());
        v2 = (end2.getX()-start2.getX())*(  end1.getY()-start2.getY())-(end2.getY()-start2.getY())*(  end1.getX()-start2.getX());
        v3 = (end1.getX()-start1.getX())*(start2.getY()-start1.getY())-(end1.getY()-start1.getY())*(start2.getX()-start1.getX());
        v4 = (end1.getX()-start1.getX())*(  end2.getY()-start1.getY())-(end1.getY()-start1.getY())*(  end2.getX()-start1.getX());
        return ((v1*v2<0) & (v3*v4<0));
    }

    public void appendPoint(Double p) throws SelfIntersectionException{
        if (points.size()<2){
            points.add(p);
            return;
        }

        Iterator it = points.iterator();
        boolean intersection = false;
        Double S1, T1, S2, T2;

        S2 =  points.get(points.size()-1); // последний
        T2 = p;
        T1 = (Double)it.next();

        while (it.hasNext()){
            S1 = T1;
            T1 = (Double)it.next();
            intersection = intersection | segmentsIntersect( S1, T1, S2, T2);
            if (intersection) throw new SelfIntersectionException();
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

