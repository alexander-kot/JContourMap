package ru.eleron.terrain.jContourMap;

import java.util.List;

interface Contour {
    /**
     * добавить точку к контуру
     *
     * @param p добавляемая точка
     * @throws SelfIntersectionException точкаа p, если бы была добавлена, создала бы самопересечение в контуре
     */
    void appendPoint(Double p) throws SelfIntersectionException;

    /**
     * получить список точек
     */
    List<Double> getPoints();

    /**
     * установить флаг замкнутости контура
     */
    void setClosed(boolean isClosed);

    /**
     * является ли контур замкнутым?
     */
    boolean isClosed();

    /**
     * установить флаг дырчатости контура
     */
    void setGap(boolean isGap);

    /**
     * является ли контур дыркой?
     */
    boolean isGap();

    /**
     * пересекаются ли контуры?
     *
     * @param that контур, с которым ищется пересечение
     */
    boolean intersects(Contour that);
}