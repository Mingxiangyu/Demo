package cn.cori.controller;

import cn.cori.execl.ExcelServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * (UserRecord)表控制层
 *
 * @author Ray。
 * @date 2020-07-18 10:15:38
 */
@Api(tags = "sql")
@RestController
public class UserRecordController {
  @Value("${ftp.startSheet:1}")
  private int startSheet;

  @Value("${ftp.cellLastIndex:3}")
  private int cellLastIndex;

  @Value("${ftp.filePath:C:\\Users\\T480S\\Desktop\\模板数据结构.xlsx}")
  private String filePath;

  @Value("${ftp.row}")
  private boolean row;

  @Value("${ftp.cell}")
  private boolean cell;

  @Value("${ftp.tableNameSql}")
  private String tableNameSql;

  @Value("${ftp.filedSql}")
  private String filedSql;

  @Value("${ftp.endSql}")
  private String endSql;

  @Value("${ftp.outFilePath}")
  private String outFilePath;

  @Value("${ftp.tableNameFlag}")
  private boolean tableNameFlag;

  @Value("${ftp.commentTableSql}")
  private String commentTableSql;

  @Value("${ftp.commentFiledSql}")
  private String commentFiledSql;

  @ApiOperation("sql")
  @GetMapping("/sql")
  public String selectAll() {
    ExcelServiceImpl excelUtils = new ExcelServiceImpl();
    List<List<String>> read =
        excelUtils.read(
            startSheet,
            cellLastIndex,
            filePath,
            row,
            cell,
            tableNameSql,
            filedSql,
            endSql,
            tableNameFlag,
            commentTableSql,
            commentFiledSql);
    ExcelServiceImpl.writeFile(read, outFilePath);
    return "成功";
  }
}
