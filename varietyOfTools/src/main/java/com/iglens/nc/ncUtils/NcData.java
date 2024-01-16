package com.iglens.nc.ncUtils;

import java.util.Arrays;
import ucar.ma2.DataType;

/**
 * nc数据的实体类
 */
public class NcData {

    /**
     * 维度
     */
    private int dimensions;

    /**
     * 数据类型
     * nc基本数据类型有六种：boolean，byte，char，short，int，long，float，double
     * 用的是{@link ucar.ma2.DataType}
     */
    private DataType dataType;

    private float[] data1Df;    //float类型的一维数组

    private float[][] data2Df;    //float类型的二维数组

    private float[][][] data3Df;    //float类型的三维数组

    private double[] data1Dd;    //double类型的一维数组

    private double[][] data2Dd;    //double类型的二维数组

    private double[][][] data3Dd;    //double类型的三维数组

    @Override
    public String toString() {
        if (dataType == DataType.FLOAT) {
            return "NcData{" +
                    "dimensions=" + dimensions +
                    ", dataType=" + dataType +
                    ", data1Df=" + Arrays.toString(data1Df) +
                    ", data2Df=" + Arrays.deepToString(data2Df) +
                    ", data3Df=" + Arrays.deepToString(data3Df) +
                    '}';
        } else {
            return "NcData{" +
                    "dimensions=" + dimensions +
                    ", dataType=" + dataType +
                    ", data1Dd=" + Arrays.toString(data1Dd) +
                    ", data2Dd=" + Arrays.deepToString(data2Dd) +
                    ", data3Dd=" + Arrays.deepToString(data3Dd) +
                    '}';
        }
    }

    public int getDimensions() {
        return dimensions;
    }

    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public float[] getData1Df() {
        return data1Df;
    }

    public void setData1Df(float[] data1Df) {
        this.data1Df = data1Df;
    }

    public float[][] getData2Df() {
        return data2Df;
    }

    public void setData2Df(float[][] data2Df) {
        this.data2Df = data2Df;
    }

    public float[][][] getData3Df() {
        return data3Df;
    }

    public void setData3Df(float[][][] data3Df) {
        this.data3Df = data3Df;
    }

    public double[] getData1Dd() {
        return data1Dd;
    }

    public void setData1Dd(double[] data1Dd) {
        this.data1Dd = data1Dd;
    }

    public double[][] getData2Dd() {
        return data2Dd;
    }

    public void setData2Dd(double[][] data2Dd) {
        this.data2Dd = data2Dd;
    }

    public double[][][] getData3Dd() {
        return data3Dd;
    }

    public void setData3Dd(double[][][] data3Dd) {
        this.data3Dd = data3Dd;
    }
}