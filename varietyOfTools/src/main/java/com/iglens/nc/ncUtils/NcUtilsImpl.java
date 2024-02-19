package com.iglens.nc.ncUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import ucar.ma2.DataType;
import ucar.ma2.InvalidRangeException;
import ucar.ma2.Range;
import ucar.nc2.Dimension;
import ucar.nc2.Variable;

public class NcUtilsImpl implements NcUtils {
    @Override
    public NcData getDataOfSlice(Variable variable) throws IOException, InvalidRangeException {
        return getDataOfSlice(variable, null);
    }

    @Override
    public NcData getDataOfSlice(Variable variable, Integer positionOf0) throws IOException, InvalidRangeException {
        return getDataOfSlice(variable, null, null);
    }

    @Override
    public NcData getDataOfSlice(Variable variable, Integer positionOf0, Integer positionOf1) throws IOException, InvalidRangeException {
        if (variable == null)
            return null;
        NcData ncData = new NcData();
        ncData.setDataType(variable.getDataType());

        int rank = variable.getRank();    //return shape.length,返回维度个数
        ncData.setDimensions(rank);     //设置维度
        /**
         * 根据维度和变量数据类型的不同，返回不同的数组
         */
        switch (rank) {
            case 1:
                if (ncData.getDataType() == DataType.FLOAT)
                    ncData.setData1Df((float[]) variable.read().getStorage());
                else
                    ncData.setData1Dd((double[]) variable.read().getStorage());
                break;
            case 2:
                if (ncData.getDataType() == DataType.FLOAT)
                    ncData.setData2Df((float[][]) variable.read().copyToNDJavaArray());
                else
                    ncData.setData2Dd((double[][]) variable.read().copyToNDJavaArray());
                break;
            case 3:
                //variable.getRanges(),返回决定该变量的各个维度的长度
                List<Range> ranges3D = new ArrayList<>(variable.getRanges());
                Range range3D = ranges3D.get(0);    //返回第 0 维度的长度
                positionOf0 = positionOf0 == null ? 0 : middle(positionOf0, 0, range3D.length() - 1);
                ranges3D.set(0, new Range(range3D.getName(), positionOf0, positionOf0, range3D.stride()));   //重新设置第0维度的长度，即获取第0维度的positionOf0层
                if (ncData.getDataType() == DataType.FLOAT)
                    ncData.setData2Df((float[][]) variable.read(ranges3D).reduce().copyToNDJavaArray());
                else
                    ncData.setData2Dd((double[][]) variable.read(ranges3D).reduce().copyToNDJavaArray());
                break;
            case 4:
                List<Range> ranges4D = new ArrayList<>(variable.getRanges());
                Range range4D0 = ranges4D.get(0);
                positionOf1 = positionOf1 == null ? 0 : middle(positionOf1, 0, range4D0.length() - 1);
                ranges4D.set(0, new Range(range4D0.getName(), positionOf1, positionOf1, range4D0.stride()));
                Range range4D1 = ranges4D.get(1);
                positionOf0 = positionOf0 == null ? 0 : middle(positionOf0, 0, range4D1.length() - 1);
                ranges4D.set(1, new Range(range4D1.getName(), positionOf0, positionOf0, range4D1.stride()));
                if (ncData.getDataType() == DataType.FLOAT)
                    ncData.setData2Df((float[][]) variable.read(ranges4D).reduce().copyToNDJavaArray());
                else
                    ncData.setData2Dd((double[][]) variable.read(ranges4D).reduce().copyToNDJavaArray());
                break;
        }
        return ncData;
    }

    @Override
    public Map<String, NcData> getAllDataOfSlice(List<Variable> variables) throws IOException, InvalidRangeException {
        return getAllDataOfSlice(variables, null);
    }

    @Override
    public Map<String, NcData> getAllDataOfSlice(List<Variable> variables, Integer positionOf0) throws IOException, InvalidRangeException {
        return getAllDataOfSlice(variables, null, null);
    }

    @Override
    public Map<String, NcData> getAllDataOfSlice(List<Variable> variables, Integer positionOf0, Integer positionOf1) throws IOException, InvalidRangeException {
        Map<String, NcData> map = new HashMap<>();
        for (Variable variable : variables) {
            map.put(variable.getFullName(), getDataOfSlice(variable, positionOf0, positionOf1));
        }
        return map;
    }

    @Override
    public NcData getDataWithLimit(Variable variable, NcDimension... ncDimensions) throws IOException, InvalidRangeException {
        if (variable == null)
            return null;
        NcData ncData = new NcData();
        ncData.setDataType(variable.getDataType());    //设置nc实体类的数据类型
        int limitLength = ncDimensions.length;  //有限制条件的维度数
        int rank = variable.getRank();    //return shape.length,返回维度个数
        ncData.setDimensions(rank);     //设置维度

        int[] origin = new int[rank];
        int[] s = new int[rank];
        for (int i = 0; i < s.length; i++) {
            s[i] = 1;
        }
        setOriginAndSection(variable, origin, s, ncDimensions); //重新给origin和s赋值
        //获取有限制条件的维度集合
        switch (rank) {
            case 1:
                if (ncData.getDataType() == DataType.FLOAT)
                    ncData.setData1Df((float[]) variable.read(origin, s).getStorage());
                else
                    ncData.setData1Dd((double[]) variable.read(origin, s).getStorage());
                break;
            case 2:
                if (ncData.getDataType() == DataType.FLOAT)
                    ncData.setData2Df((float[][]) variable.read(origin, s).copyToNDJavaArray());
                else
                    ncData.setData2Dd((double[][]) variable.read(origin, s).copyToNDJavaArray());
                break;
            case 3:
                if (ncData.getDataType() == DataType.FLOAT)
                    ncData.setData3Df((float[][][]) variable.read(origin, s).copyToNDJavaArray());
                else
                    ncData.setData3Dd((double[][][]) variable.read(origin, s).copyToNDJavaArray());
                break;
            case 4:
                //仅降低一个维度，转为三维数组
                for (int i = 0; i < s.length; i++) {
                    if (s[i] == 1) {
                        if (ncData.getDataType() == DataType.FLOAT)
                            ncData.setData3Df((float[][][]) variable.read(origin, s).reduce(i).copyToNDJavaArray());
                        else
                            ncData.setData3Dd((double[][][]) variable.read(origin, s).reduce(i).copyToNDJavaArray());
                        break;
                    }
                }
                break;
        }

        return ncData;
    }

    @Override
    public Map<String, NcData> getAllDataWithLimit(List<Variable> variables, NcDimension... ncDimensions) throws IOException, InvalidRangeException {
        Map<String, NcData> map = new HashMap<>();
        for (Variable variable : variables) {
            map.put(variable.getFullName(), getDataWithLimit(variable, ncDimensions));
        }
        return map;
    }

    private void setOriginAndSection(@NotNull Variable variable, int[] origin, int[] s, @NotNull NcDimension... ncDimensions) {
        int startIndex = 0;
        for (NcDimension ncDimension : ncDimensions) {
            Dimension dimension = ncDimension.getDimension();
            int dimensionIndex = variable.findDimensionIndex(dimension.getFullName());
            //判断variable是否存在该dimension
            if (dimensionIndex != -1) {
                startIndex = ncDimension.getStartIndex();
                origin[dimensionIndex] = middle(startIndex, 0, dimension.getLength() - 1);
                s[dimensionIndex] = middle(ncDimension.getSection(), 1, dimension.getLength() - 1 - startIndex);
            }
        }
    }

    public static int middle(int a, int b, int c) {
        return a > b ? (b > c ? b : (a > c ? c : a)) : (a > c ? a : (b > c ? c : b));
    }
}