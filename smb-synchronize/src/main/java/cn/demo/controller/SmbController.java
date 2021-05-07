package cn.demo.controller;

import cn.demo.service.SmbService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * (SmbLasttime)表控制层
 *
 * @author Ray。
 * @date 2020-07-22 14:06:53
 */
@Api("ceshi ")
@RestController
public class SmbController {
  /** 服务对象 */
  @Resource private SmbService smbService;

  /**
   * 同步该文件夹下所有文件
   *
   * @return 所有数据
   */
  @ApiOperation("sql")
  @GetMapping("/init")
  public String init() {
    smbService.init();
    return "同步所有数据成功 " + System.currentTimeMillis();
  }

}
