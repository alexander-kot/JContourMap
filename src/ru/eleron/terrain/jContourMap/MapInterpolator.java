package ru.eleron.terrain.jContourMap;

import java.awt.geom.Rectangle2D;

interface MapInterpolator {
    /**
     * установить список допустимых значений высот
     *
     * @param heights список значений высот
     * @throws IllegalStateException если размер heights слишком мал т.к. уже были добавлены контуры, hIndex которых выходит за границу этого массива
     */
    void setHeights(double[] heights) throws IllegalStateException;

    /**
     * @return список допустимых значений высот
     * @throws IllegalStateException если размер heights слишком мал т.к. уже были добавлены контуры, hIndex которых выходит за границу этого массива
     */
    double[] getHeights();

    /**
     * Добавить замкнутый контур.
     * Все точки контура имеют одинаковую высоту, равную height
     *
     * @param contour контур
     * @param hIndex  - индекс высоты контура (его высота будет height[hIndex])
     * @throws ContourIntersectionException добавленный контур пересекает другие контуры
     */
    void addContour(int hIndex, Contour contour)
            throws ContourIntersectionException;
    //! если по хорошему, то пересечение контуров я не найду раньше растеризации.

    /**
     * Вычислить значения высоты в каждом узле решетки
     *
     * @param heightMap - карта высот, которую надо заполнить.
     *                  height[x][y] - Это высота узла решетки, координаты которого равны
     *                  (x*bounds.getWidth()/(heightMap[0].length-1), (y*bounds.getHeight()/(heightMap.length-1))
     * @param bounds    ограничивающий прямоугольник решетки
     * @throws InvalidContourConfigurationException
     *          информация о контурах противоречива
     */
    void buildMap(double[][] heightMap, Rectangle2D bounds)
            throws InvalidContourConfigurationException;
}