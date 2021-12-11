package com.iglens.数据库.MongoDB.JPA;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoTestUserRepository extends MongoRepository<MongoTestUser, String> {
  public Page<MongoTestUser> findByUserNameLike(String userName, Pageable pageable);
}
//    原文链接：https://blog.csdn.net/weixin_39214304/article/details/84791953
