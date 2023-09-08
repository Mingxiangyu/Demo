package com.iglens.jpa;

import cn.hutool.core.util.StrUtil;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class BusinessServiceImpl {

  private final BusinessJpaRepository repository;

  public BusinessServiceImpl(BusinessJpaRepository repository) {
    this.repository = repository;
  }

  public Page<BusinessEntity> 模糊查询(Pageable pageable, BusinessEntity dto) {
    // 名称
    String name = StringUtils.trim(dto.getFileName());
    String url = StringUtils.trim(dto.getLayerUrl());

    Specification<BusinessEntity> spec =
        (Specification<BusinessEntity>)
            (root, query, criteriaBuilder) -> {
              List<Predicate> predicates = new ArrayList<>();
              if (StringUtils.isNotBlank(name)) {
                predicates.add(criteriaBuilder.like(root.get("layerName"), "%" + name + "%"));
              }
              if (StringUtils.isNotBlank(url)) {
                predicates.add(criteriaBuilder.like(root.get("layerUrl"), "%" + url + "%"));
              }
              return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };
    return repository.findAll(spec, pageable);
  }

  /**
   * 基于字段使用group去重
   *
   * @param dto
   * @param pageable
   * @param count // 为了解决jpa分组后翻页的总数不对，添加了count变量
   * @return
   */
  public Page<BusinessEntity> 基于字段使用group去重(
      BusinessEntity dto, Pageable pageable, List<Long> count) {

    String dataName = StringUtils.trimToEmpty(dto.getFileName());
    String baikeType = StringUtils.trimToEmpty(dto.getBaikeType());

    Specification<BusinessEntity> spec =
        (Specification<BusinessEntity>)
            (root, criteriaQuery, cb) -> {
              List<Predicate> predicates = new ArrayList<>();
              String id = dto.getId().toString();
              if (StringUtils.isNotBlank(id)) {
                predicates.add(cb.equal(root.get("id"), NumberUtils.toLong(id)));
              }
              String dataType = dto.getDataType();
              if (StringUtils.isNotBlank(dataType)) {
                predicates.add(cb.equal(root.get("dataType"), dataType));
              }
              String year = dto.getYear();
              if (StringUtils.isNotBlank(year)) {
                predicates.add(cb.equal(root.get("year"), year));
              }
              String country = dto.getCountry();
              if (StringUtils.isNotBlank(country)) {
                predicates.add(cb.equal(root.get("country"), country));
              }
              String business = dto.getBusiness();
              if (StringUtils.isNotBlank(business)) {
                predicates.add(cb.equal(root.get("business"), business));
              }
              if (StringUtils.isNotBlank(dataName)) {
                predicates.add(cb.like(root.get("dataName"), ("%" + dataName + "%")));
              }
              if (StringUtils.isNotBlank(baikeType)) {
                predicates.add(cb.equal(root.get("baikeType"), baikeType));
              }

              // if (StringUtils.equals(dataType, "gadm") || StringUtils.equals(dataType, "oosm")
              //     || StringUtils.equals(dataType, "placename")) {
              criteriaQuery.groupBy(root.get("dataName"));
              criteriaQuery.where(cb.and(predicates.toArray(new Predicate[0])));
              return criteriaQuery.getRestriction();

              // criteriaQuery.where(cb.and(predicates.toArray(new Predicate[0])))
              //     .groupBy(root.get("dataName"));
              // return criteriaQuery.getRestriction();

              // // 通过子查询过滤重复字段
              // Subquery<String> subquery = criteriaQuery.subquery(String.class);
              // // 构建子查询表主体 subqueryRoot
              // Root<? extends MetaData> subqueryRoot = subquery.from(root.getJavaType());
              // subquery.select(subqueryRoot.get("dataName"));
              // subquery.groupBy(subqueryRoot.get("dataName"));
              // subquery.having(cb.gt(cb.count(subqueryRoot.get("dataName")), 1L));
              //
              // predicates.add(cb.not(root.get("dataName").in(subquery)));
              // predicates.add(cb.isNotNull(root.get("dataType")));
              // }
              // 去重操作
              // criteriaQuery.distinct(true);
              // criteriaQuery.where(predicates.toArray(new Predicate[0]));
              // return cb.and(predicates.toArray(new Predicate[0]));
            };
    // 为了获取 group by 后数据总数
    Pageable totle = PageRequest.of(0, 999999999, pageable.getSort());
    Page<BusinessEntity> all = repository.findAll(spec, totle);
    // count在外部传入，本函数内修改后，外部直接使用了
    count.set(0, all.getTotalElements());
    return repository.findAll(spec, pageable);
  }

  public List<String> 基于指定字段去重(
      String dataType, String baikeType, String year, String country, String groupBy) {
    Specification<BusinessEntity> spec =
        (Specification<BusinessEntity>)
            (root, criteriaQuery, cb) -> {
              List<Predicate> predicates = new ArrayList<>();
              if (StringUtils.isNotBlank(dataType)) {
                predicates.add(cb.equal(root.get("dataType"), dataType));
              }
              if (StringUtils.isNotBlank(baikeType)) {
                predicates.add(cb.equal(root.get("baikeType"), baikeType));
              }
              if (StringUtils.isNotBlank(year)) {
                predicates.add(cb.equal(root.get("year"), year));
              }
              if (StringUtils.isNotBlank(country)) {
                predicates.add(cb.equal(root.get("country"), country));
              }

              criteriaQuery
                  .multiselect(root.get(groupBy), cb.count(root))
                  .where(cb.and(predicates.toArray(new Predicate[0])))
                  .groupBy(root.get(groupBy));

              return criteriaQuery.getRestriction();
            };

    List<BusinessEntity> all = repository.findAll(spec);
    return null;
  }

  /**
   * springJpa Specification 时间查询范围
   *
   * <p>原文链接：https://blog.csdn.net/FSWZYC/article/details/128643876
   *
   * @param dto
   * @param pageable
   * @return
   */
  public List<String> 时间查询范围(BusinessEntity dto, Pageable pageable) {
    // 字段查询，模糊匹配
    Specification<BusinessEntity> specification =
        (root, query, criteriaBuilder) -> {
          List<Predicate> predicates = new ArrayList<>();
          // 文件名称
          // root.get("fileName") 是实体字段属性，不是数据库字段
          String fileName = dto.getFileName();
          if (StringUtils.isNotBlank(fileName)) {
            predicates.add(criteriaBuilder.like(root.get("fileName"), "%" + fileName + "%"));
          }

          String dataType = dto.getDataType();
          if (StringUtils.isNotBlank(dataType)) {
            predicates.add(criteriaBuilder.equal(root.get("dataType"), dataType));
          }

          // 解析时间 大于等于
          Instant createStamp = dto.getCreateStamp();
          if (null != createStamp) {
            predicates.add(
                criteriaBuilder.greaterThanOrEqualTo(
                    root.get("fileAnalysisTime").as(Instant.class), createStamp));
          }
          // 解析时间 小于等于
          // Date endFileAnalysisTime = dto.get();
          // if(null != endFileAnalysisTime){
          //
          // predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("fileAnalysisTime").as(Date.class), endFileAnalysisTime));
          // }

          // 返回查询条件
          return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    Page<BusinessEntity> fileEntitys = repository.findAll(specification, pageable);
    return null;
  }
  /**
   * springJpa Specification 条件查询
   *
   * <p>原文链接：https://blog.51cto.com/u_15061930/4188197https://blog.51cto.com/u_15061930/4188197
   *
   * @param dto
   * @return
   */
  public List<String> 条件查询(BusinessEntity dto) {
    String dataType = dto.getDataType();
    Specification<BusinessEntity> specification =
        new Specification<BusinessEntity>() {
          @Override
          @SneakyThrows
          public Predicate toPredicate(
              Root<BusinessEntity> root,
              CriteriaQuery<?> criteriaQuery,
              CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicateList = new ArrayList<>();

            /** 相等=== @参数 X @参数类型 */
            if (StrUtil.isNotEmpty(dataType)) {
              predicateList.add(criteriaBuilder.equal(root.get("dataType"), dataType));
            }

            /** not in @参数 XXXX @参数类型 List */
            // if (CollUtil.isNotEmpty(dataType)) {
            //   Predicate validDnaPredicate = root.get("dna").in(dataType).not();
            //   predicateList.add(validDnaPredicate);
            // }

            /** in @参数 XXXX @参数类型 List */
            // if (CollUtil.isNotEmpty(XXXX)) {
            //   Predicate invalidDna = root.get("dna").in(XXXX);
            //   predicateList.add(invalidDna);
            // }

            /** 大于等于>= @参数 XX @参数类型 数值 */
            // if (CollUtil.isNotEmpty(XX)) {
            //   predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("level"), XX));
            // }

            /** 小于等于<= @参数 XX @参数类型 数值 */
            // if (CollUtil.isNotEmpty(XX)) {
            //   predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("level"), XX));
            // }

            /** betweeen @参数 XX_1 ; XX_2 @参数类型 数值 */
            // if (CollUtil.isNotEmpty(XXX_1) && CollUtil.isNotEmpty(XXX_2)) {
            //   predicateList.add(criteriaBuilder.between(root.get("level"), XX_1, XX_2));
            // }

            /** 大于等于>= @参数 XXX @参数类型 Date */
            // if (CollUtil.isNotEmpty(XXX)) {
            //
            // predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("level").as(Date.class), XX));
            // }

            /** 小于等于<= @参数 XXX @参数类型 Date */
            // if (CollUtil.isNotEmpty(XXX)) {
            //
            // predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("level").as(Date.class),
            // XX));
            // }

            /** betweeen @参数 XXX_1 ; XXX_2 @参数类型 Date */
            // if (CollUtil.isNotEmpty(XXX_1) && CollUtil.isNotEmpty(XXX_2)) {
            //   predicateList.add(criteriaBuilder.between(root.get("level").as(Date.class), XX_1,
            // XX_2));
            // }

            Predicate[] pre = new Predicate[predicateList.size()];
            pre = predicateList.toArray(pre);
            return criteriaQuery.where(pre).getRestriction();
          }
        };
    List<BusinessEntity> all = repository.findAll(specification);
    return null;
  }
}
