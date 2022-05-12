package com.iglens.地理.GDAL;

import org.gdal.ogr.Feature;
import org.gdal.ogr.FeatureDefn;
import org.gdal.ogr.Geometry;
import org.gdal.ogr.Layer;

public class GDAL创建要素 {
  /**
   * 传入Geometry创建Feature，这里不定义属性字段
   *
   * @param layer
   * @param geometry
   */
  public static void createFeatureByGeometry(Layer layer, Geometry geometry) {
    FeatureDefn featureDefn = layer.GetLayerDefn();
    Feature feature = new Feature(featureDefn);
    feature.SetGeometry(geometry);
    layer.CreateFeature(feature);
  }
}
