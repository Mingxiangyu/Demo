// package com.iglens.jpa;
//
// import cn.hutool.core.collection.CollUtil;
// import cn.hutool.core.util.StrUtil;
// import java.util.ArrayList;
// import javax.persistence.criteria.CriteriaBuilder;
// import javax.persistence.criteria.CriteriaQuery;
// import lombok.SneakyThrows;
//
// /** https://blog.51cto.com/u_15061930/4188197https://blog.51cto.com/u_15061930/4188197 */
// public class Jpa使用Specification条件查询 {
//   Specification<MinerExceptionTable> specification = new Specification<MinerExceptionTable>() {
//     @SneakyThrows
//     @Override
//     public Predicate toPredicate(Root<MinerExceptionTable> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//       List<Predicate> predicateList = new ArrayList<>();
//
//       /**
//        * 相等===
//        *
//        * @参数 X
//        * @参数类型
//        */
//       if (StrUtil.isNotEmpty()) {
//         predicateList.add(criteriaBuilder.equal(root.get("farmCode"), X));
//       }
//
//       /**
//        * not in
//        *
//        * @参数 XXXX
//        * @参数类型  List
//        */
//       if (CollUtil.isNotEmpty(XXXX)) {
//         Predicate validDnaPredicate = root.get("dna").in(XXXX).not();
//         predicateList.add(validDnaPredicate);
//       }
//
//       /**
//        * in
//        *
//        * @参数 XXXX
//        * @参数类型  List
//        */
//       if (CollUtil.isNotEmpty(XXXX)) {
//         Predicate invalidDna = root.get("dna").in(XXXX);
//         predicateList.add(invalidDna);
//       }
//
//       /**
//        * 大于等于>=
//        *
//        * @参数 XX
//        * @参数类型  数值
//        */
//       if (CollUtil.isNotEmpty(XX)) {
//         predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("level"), XX));
//       }
//
//       /**
//        * 小于等于<=
//        *
//        * @参数 XX
//        * @参数类型 数值
//        */
//       if (CollUtil.isNotEmpty(XX)) {
//         predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("level"), XX));
//       }
//
//       /**
//        * betweeen
//        *
//        * @参数 XX_1 ; XX_2
//        * @参数类型  数值
//        */
//       if (CollUtil.isNotEmpty(XXX_1) && CollUtil.isNotEmpty(XXX_2)) {
//         predicateList.add(criteriaBuilder.between(root.get("level"), XX_1, XX_2));
//       }
//
//       /**
//        * 大于等于>=
//        *
//        * @参数 XXX
//        * @参数类型  Date
//        */
//       if (CollUtil.isNotEmpty(XXX)) {
//         predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("level").as(Date.class), XX));
//       }
//
//       /**
//        * 小于等于<=
//        *Specification<MinerExceptionTable> specification = new Specification<MinerExceptionTable>() {
//        *     @SneakyThrows
//        *     @Override
//        *     public Predicate toPredicate(Root<MinerExceptionTable> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//        *         List<Predicate> predicateList = new ArrayList<>();
//        *
//        *         /**
//        *          * 相等===
//        *          *
//        *          * @参数 X
//        *          * @参数类型
//        *          */
//        *         if (StrUtil.isNotEmpty()) {
//        *             predicateList.add(criteriaBuilder.equal(root.get("farmCode"), X));
//        *         }
//        *
//        *         /**
//        *          * not in
//        *          *
//        *          * @参数 XXXX
//        *          * @参数类型  List
//        *          */
//        *         if (CollUtil.isNotEmpty(XXXX)) {
//        *             Predicate validDnaPredicate = root.get("dna").in(XXXX).not();
//        *             predicateList.add(validDnaPredicate);
//        *         }
//        *
//        *         /**
//        *          * in
//        *          *
//        *          * @参数 XXXX
//        *          * @参数类型  List
//        *          */
//        *         if (CollUtil.isNotEmpty(XXXX)) {
//        *             Predicate invalidDna = root.get("dna").in(XXXX);
//        *             predicateList.add(invalidDna);
//        *         }
//        *
//        *         /**
//        *          * 大于等于>=
//        *          *
//        *          * @参数 XX
//        *          * @参数类型  数值
//        *          */
//        *         if (CollUtil.isNotEmpty(XX)) {
//        *             predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("level"), XX));
//        *         }
//        *
//        *         /**
//        *          * 小于等于<=
//        *          *
//        *          * @参数 XX
//        *          * @参数类型 数值
//        *          */
//        *         if (CollUtil.isNotEmpty(XX)) {
//        *             predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("level"), XX));
//        *         }
//        *
//        *         /**
//        *          * betweeen
//        *          *
//        *          * @参数 XX_1 ; XX_2
//        *          * @参数类型  数值
//        *          */
//        *         if (CollUtil.isNotEmpty(XXX_1) && CollUtil.isNotEmpty(XXX_2)) {
//        *             predicateList.add(criteriaBuilder.between(root.get("level"), XX_1, XX_2));
//        *         }
//        *
//        *         /**
//        *          * 大于等于>=
//        *          *
//        *          * @参数 XXX
//        *          * @参数类型  Date
//        *          */
//        *         if (CollUtil.isNotEmpty(XXX)) {
//        *             predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("level").as(Date.class), XX));
//        *         }
//        *
//        *         /**
//        *          * 小于等于<=
//        *          *
//        *          * @参数 XXX
//        *          * @参数类型 Date
//        *          */
//        *         if (CollUtil.isNotEmpty(XXX)) {
//        *             predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("level").as(Date.class), XX));
//        *         }
//        *
//        *         /**
//        *          * betweeen
//        *          *
//        *          * @参数 XXX_1 ; XXX_2
//        *          * @参数类型  Date
//        *          */
//        *         if (CollUtil.isNotEmpty(XXX_1) && CollUtil.isNotEmpty(XXX_2)) {
//        *             predicateList.add(criteriaBuilder.between(root.get("level").as(Date.class), XX_1, XX_2));
//        *         }
//        *
//        *         Predicate[] pre = new Predicate[predicateList.size()];
//        *         pre = predicateList.toArray(pre);
//        *         return criteriaQuery.where(pre).getRestriction();
//        *     }
//        * };
//        * @参数 XXX
//        * @参数类型 Date
//        */
//       if (CollUtil.isNotEmpty(XXX)) {
//         predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("level").as(Date.class), XX));
//       }
//
//       /**
//        * betweeen
//        *
//        * @参数 XXX_1 ; XXX_2
//        * @参数类型  Date
//        */
//       if (CollUtil.isNotEmpty(XXX_1) && CollUtil.isNotEmpty(XXX_2)) {
//         predicateList.add(criteriaBuilder.between(root.get("level").as(Date.class), XX_1, XX_2));
//       }
//
//       Predicate[] pre = new Predicate[predicateList.size()];
//       pre = predicateList.toArray(pre);
//       return criteriaQuery.where(pre).getRestriction();
//     }
//   };
// }
