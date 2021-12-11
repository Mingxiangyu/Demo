package com.iglens.数据库.MongoDB.JPA;

import io.swagger.annotations.Api;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Mongo测试")
@RestController
@RequestMapping("/MongoTest/user")
public class MongoTestUserController {

  @Autowired private MongoTestUserRepository userRepository;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public MongoTestUser createUser(@RequestBody MongoTestUser user) {
    return userRepository.save(user);
  }

  /**
   * 根据id查询
   *
   * @param id
   * @return
   */
  @GetMapping(value = "/{id}")
  public MongoTestUser readUserById(@PathVariable("id") String id) {
    return userRepository.findById(id).orElse(null);
  }

  /**
   * 根据一个或者多个属性查询单个结果
   *
   * @param name
   * @return
   */
  @GetMapping(value = "/name/{name}")
  public MongoTestUser readUserByName(@PathVariable("name") String name) {
    MongoTestUser user = new MongoTestUser();
    user.setUserName(name);
    ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("age", "createTime");
    Example<MongoTestUser> example = Example.of(user, matcher);
    return userRepository.findOne(example).orElse(null);
  }

  /**
   * 根据一个或者多个属性分页查询
   *
   * @param pageNumber
   * @param pageSize
   * @return
   */
  @GetMapping(value = "/page/{pageNumber}/pagesize/{pageSize}/name/{name}")
  public Page<MongoTestUser> readUsersByPage(
      @PathVariable("pageNumber") int pageNumber,
      @PathVariable("pageSize") int pageSize,
      @PathVariable("name") String name) {
    MongoTestUser user = new MongoTestUser();
    user.setUserName(name);
    ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("age", "createTime");
    Example<MongoTestUser> example = Example.of(user, matcher);
    if (pageNumber < 1) {
      pageNumber = 1;
    } else if (pageSize == 0) {
      pageSize = 20;
    }

    PageRequest pageable = PageRequest.of(pageNumber - 1, pageSize);
    return userRepository.findAll(example, pageable);
  }

  /**
   * 根据用户年龄升序排序
   *
   * @return
   */
  @GetMapping
  public List<MongoTestUser> readUsers() {
    Order order = new Order(Direction.ASC, "age");
    Sort sort = Sort.by(order);
    return userRepository.findAll(sort);
  }

  /**
   * 模糊查询带分页
   *
   * @param pageNumber
   * @param pageSize
   * @param keyWords
   * @return
   */
  @GetMapping(value = "/page/{pageNumber}/pagesize/{pageSize}/keyword/{keyWords}")
  public Page<MongoTestUser> readUsersByKeywords(
      @PathVariable("pageNumber") int pageNumber,
      @PathVariable("pageSize") int pageSize,
      @PathVariable("keyWords") String keyWords) {
    if (keyWords == null) {
      keyWords = "";
    }
    if (pageNumber < 1) {
      pageNumber = 1;
    } else if (pageSize == 0) {
      pageSize = 20;
    }
    PageRequest pageable = PageRequest.of(pageNumber - 1, pageSize);
    return userRepository.findByUserNameLike(keyWords, pageable);
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping(value = "/{id}")
  public void removeUser(@PathVariable("id") String id) {
    userRepository.deleteById(id);
  }
}
