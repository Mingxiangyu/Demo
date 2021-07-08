package org.demo.对象;

import static java.util.Comparator.comparing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

public class 构建树结构 {
  /** 构建树结构 */
  public List<CategoryDTO> merge(List<CategoryDTO> items) {
    if (CollectionUtils.isEmpty(items)) {
      return Collections.emptyList();
    }
    Map<String, CategoryDTO> map =
        items.stream()
            .collect(Collectors.toMap(CategoryDTO::getId, Function.identity(), (k1, k2) -> k2));
    List<CategoryDTO> roots = new ArrayList<>();
    items.forEach(
        item -> {
          CategoryDTO parentItem = map.get(item.getParentId());
          if (parentItem != null) {
            if (parentItem.getSubcategories() == null) {
              parentItem.setSubcategories(new ArrayList<>());
            }
            parentItem.getSubcategories().add(item);
          } else {
            roots.add(item);
          }
        });
    return roots.stream().sorted(comparing(CategoryDTO::getSort)).collect(Collectors.toList());
  }
}

@Data
class CategoryDTO implements Serializable {
  private static final long serialVersionUID = -1L;
  private String id;
  private String tenantId;
  private String parentId;
  private String baseType;
  private String mappedClassName;
  private List<String> ancestors;
  private String name;
  private String identifier;
  private String description;
  private String icon;
  private Boolean parent;
  private String shortName;
  private String detailUrl;
  private String symbol;
  private String sort;
  private String path;
  private Boolean disabled;
  private Boolean instantiateDisabled;
  private List<CategoryDTO> subcategories;
  private Boolean operable;
  private String belongTenant;
  private String codeId;

}
