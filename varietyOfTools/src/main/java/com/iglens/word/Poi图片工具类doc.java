package com.iglens.word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;

/**
 * Provides access to the pictures both by offset, iteration over the
 * un-claimed, and peeking forward
 */
public class Poi图片工具类doc {

  private PicturesTable picturesTable;
  private Set<Picture> output = new HashSet<Picture>();
  private Map<Integer, Picture> lookup;
  private List<Picture> nonU1based;
  private List<Picture> all;
  private int pn = 0;

  public Poi图片工具类doc(HWPFDocument doc) {
    picturesTable = doc.getPicturesTable();
    all = picturesTable.getAllPictures();

    // Build the Offset-Picture lookup map
    lookup = new HashMap<Integer, Picture>();
    for (Picture p : all) {
      lookup.put(p.getStartOffset(), p);
    }

    // Work out which Pictures aren't referenced by
    //  a \u0001 in the main text
    // These are \u0008 escher floating ones, ones
    //  found outside the normal text, and who
    //  knows what else...
    nonU1based = new ArrayList<Picture>();
    nonU1based.addAll(all);
    Range r = doc.getRange();
    for (int i = 0; i < r.numCharacterRuns(); i++) {
      CharacterRun cr = r.getCharacterRun(i);
      if (picturesTable.hasPicture(cr)) {
        Picture p = getFor(cr);
        int at = nonU1based.indexOf(p);
        nonU1based.set(at, null);
      }
    }
  }

  private boolean hasPicture(CharacterRun cr) {
    return picturesTable.hasPicture(cr);
  }

  private void recordOutput(Picture picture) {
    output.add(picture);
  }

  private boolean hasOutput(Picture picture) {
    return output.contains(picture);
  }

  private int pictureNumber(Picture picture) {
    return all.indexOf(picture) + 1;
  }

  public Picture getFor(CharacterRun cr) {
    return lookup.get(cr.getPicOffset());
  }

  /**
   * Return the next unclaimed one, used towards the end
   */
  private Picture nextUnclaimed() {
    Picture p = null;
    while (pn < nonU1based.size()) {
      p = nonU1based.get(pn);
      pn++;
      if (p != null) {
        return p;
      }
    }
    return null;
  }
}