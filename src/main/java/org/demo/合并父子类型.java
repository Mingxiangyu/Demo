package org.demo;

import static java.util.Comparator.comparing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

public class 合并父子类型 {
  /** 合并父子类型 */
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

  public CategoryDTO() {}

  public String getId() {
    return this.id;
  }

  public String getTenantId() {
    return this.tenantId;
  }

  public String getParentId() {
    return this.parentId;
  }

  public String getBaseType() {
    return this.baseType;
  }

  public String getMappedClassName() {
    return this.mappedClassName;
  }

  public List<String> getAncestors() {
    return this.ancestors;
  }

  public String getName() {
    return this.name;
  }

  public String getIdentifier() {
    return this.identifier;
  }

  public String getDescription() {
    return this.description;
  }

  public String getIcon() {
    return this.icon;
  }

  public Boolean getParent() {
    return this.parent;
  }

  public String getShortName() {
    return this.shortName;
  }

  public String getDetailUrl() {
    return this.detailUrl;
  }

  public String getSymbol() {
    return this.symbol;
  }

  public String getSort() {
    return this.sort;
  }

  public String getPath() {
    return this.path;
  }

  public Boolean getDisabled() {
    return this.disabled;
  }

  public Boolean getInstantiateDisabled() {
    return this.instantiateDisabled;
  }

  public List<CategoryDTO> getSubcategories() {
    return this.subcategories;
  }

  public Boolean getOperable() {
    return this.operable;
  }

  public String getBelongTenant() {
    return this.belongTenant;
  }

  public String getCodeId() {
    return this.codeId;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public void setTenantId(final String tenantId) {
    this.tenantId = tenantId;
  }

  public void setParentId(final String parentId) {
    this.parentId = parentId;
  }

  public void setBaseType(final String baseType) {
    this.baseType = baseType;
  }

  public void setMappedClassName(final String mappedClassName) {
    this.mappedClassName = mappedClassName;
  }

  public void setAncestors(final List<String> ancestors) {
    this.ancestors = ancestors;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void setIdentifier(final String identifier) {
    this.identifier = identifier;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public void setIcon(final String icon) {
    this.icon = icon;
  }

  public void setParent(final Boolean parent) {
    this.parent = parent;
  }

  public void setShortName(final String shortName) {
    this.shortName = shortName;
  }

  public void setDetailUrl(final String detailUrl) {
    this.detailUrl = detailUrl;
  }

  public void setSymbol(final String symbol) {
    this.symbol = symbol;
  }

  public void setSort(final String sort) {
    this.sort = sort;
  }

  public void setPath(final String path) {
    this.path = path;
  }

  public void setDisabled(final Boolean disabled) {
    this.disabled = disabled;
  }

  public void setInstantiateDisabled(final Boolean instantiateDisabled) {
    this.instantiateDisabled = instantiateDisabled;
  }

  public void setSubcategories(final List<CategoryDTO> subcategories) {
    this.subcategories = subcategories;
  }

  public void setOperable(final Boolean operable) {
    this.operable = operable;
  }

  public void setBelongTenant(final String belongTenant) {
    this.belongTenant = belongTenant;
  }

  public void setCodeId(final String codeId) {
    this.codeId = codeId;
  }

  @Override
  public String toString() {
    return "CategoryDTO(id="
        + this.getId()
        + ", tenantId="
        + this.getTenantId()
        + ", parentId="
        + this.getParentId()
        + ", baseType="
        + this.getBaseType()
        + ", mappedClassName="
        + this.getMappedClassName()
        + ", ancestors="
        + this.getAncestors()
        + ", name="
        + this.getName()
        + ", identifier="
        + this.getIdentifier()
        + ", description="
        + this.getDescription()
        + ", icon="
        + this.getIcon()
        + ", parent="
        + this.getParent()
        + ", shortName="
        + this.getShortName()
        + ", detailUrl="
        + this.getDetailUrl()
        + ", symbol="
        + this.getSymbol()
        + ", sort="
        + this.getSort()
        + ", path="
        + this.getPath()
        + ", disabled="
        + this.getDisabled()
        + ", instantiateDisabled="
        + this.getInstantiateDisabled()
        + ", subcategories="
        + this.getSubcategories()
        + ", operable="
        + this.getOperable()
        + ", belongTenant="
        + this.getBelongTenant()
        + ", codeId="
        + this.getCodeId()
        + ")";
  }
}
