package cn.ruiyeclub.controller;

import cn.ruiyeclub.entity.UserRecord;
import cn.ruiyeclub.service.UserRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * (UserRecord)表控制层
 *
 * @author Ray。
 * @date 2020-07-18 10:15:38
 */
@RestController
@RequestMapping("userRecord")
public class UserRecordController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private UserRecordService userRecordService;

    /**
     * 分页查询所有数据
     * @param page       分页对象
     * @param userRecord 查询实体
     * @return 所有数据
     */
    @GetMapping("page")
    public R selectAll(Page<UserRecord> page, UserRecord userRecord) {
        return success(this.userRecordService.page(page, new QueryWrapper<>(userRecord)));
    }

    /**
     * 通过主键查询单条数据
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Integer id) {
        return success(this.userRecordService.getById(id));
    }

    /**
     * 新增数据
     * @param userRecord 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public R insert(@RequestBody UserRecord userRecord) {
        return success(this.userRecordService.save(userRecord));
    }

    /**
     * 修改数据
     * @param userRecord 实体对象
     * @return 修改结果
     */
    @PutMapping("update")
    public R update(@RequestBody UserRecord userRecord) {
        return success(this.userRecordService.updateById(userRecord));
    }

    /**
     * 删除数据
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping("delete")
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.userRecordService.removeByIds(idList));
    }
}