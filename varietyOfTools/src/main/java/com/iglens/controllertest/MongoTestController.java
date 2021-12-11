package com.iglens.controllertest;

import com.iglens.数据库.MongoDB.MongoDBHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "MongoTest")
@RestController
public class MongoTestController {
  private static String ADDRESS = "d:\\workbook.xlsx"; // 导出Excel文件的路径

  @Autowired private MongoTemplate mongoTemplate;
  @Autowired private MongoDBHelper mongoDBHelper;

  @ApiOperation("mongoToExcel")
  @GetMapping("/MongoToExcel")
  public String mongoToExcel() {
    mongoDBHelper.exportExcel(ADDRESS);
    return null;
  }

  @ApiOperation("importExcel")
  @GetMapping("/importExcel")
  public String importExcel() {
    mongoDBHelper.importExcel(ADDRESS);
    return null;
  }
}
