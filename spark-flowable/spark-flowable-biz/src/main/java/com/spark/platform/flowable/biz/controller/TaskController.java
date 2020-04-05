package com.spark.platform.flowable.biz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spark.platform.common.base.support.ApiResponse;
import com.spark.platform.common.base.support.BaseController;
import com.spark.platform.flowable.api.enums.ActionEnum;
import com.spark.platform.flowable.biz.service.ActHistTaskService;
import com.spark.platform.flowable.biz.service.ActTaskQueryService;
import com.spark.platform.flowable.biz.service.ActTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author: wangdingfeng
 * @Date: 2020/4/5 14:33
 * @Description:
 */
@RestController
@RequestMapping("/runtime/tasks")
@Api(value = "Task", tags = {"流程任务"})
public class TaskController extends BaseController {

    @Autowired
    private ActTaskQueryService actTaskQueryService;

    @Autowired
    private ActTaskService actTaskService;

    @Autowired
    private ActHistTaskService actHistTaskService;


    @GetMapping("/page")
    @ApiOperation(value = "根据用户ID或者用户组ID，查询该用户代办", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "groupId", value = "用户组ID", required = true, dataType = "String")
    })
    public ApiResponse page(Page page, String userId, String groupId) {
        return success(actTaskQueryService.taskCandidateOrAssignedOrGroupPage(userId, groupId, page));
    }


    public ApiResponse query() {
        return success();
    }


    @GetMapping(value = "/comment")
    @ApiOperation(value = "查询批注信息", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "String")})
    public ApiResponse getTaskComments(String taskId) {
        return success(actTaskService.getTaskComments(taskId));
    }

    @GetMapping(value = "/his/page")
    @ApiOperation(value = "查询批注信息", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String")})
    public ApiResponse hisPage(Page page, String userId) {
        return success(actHistTaskService.pageListByUserId(userId, page));
    }

    @PostMapping(value = "/{taskId}")
    @ApiOperation(value = "执行任务", notes = "任务执行类型 claim：签收 unclaim 反签收 complete 完成 delegate 任务委派 resolve 任务签收完成 返回任务人 assignee 任务转办", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID", required = true),
            @ApiImplicitParam(name = "action", value = "执行任务类型", required = false),
            @ApiImplicitParam(name = "assignee", value = "受让人", required = false),
            @ApiImplicitParam(name = "localScope", value = "流程参数存储范围", required = false)
    })
    public ApiResponse executeTask(@PathVariable String taskId, @RequestParam("action") String action, @RequestParam(value = "assignee") String assignee,
                                   @RequestParam(value = "localScope") boolean localScope, @RequestBody Map<String, Object> variables) {
        Map<String, Object> map = actTaskService.execute(taskId, assignee, action, variables, localScope);
        return success(ActionEnum.actionOf(action).getName(), map);
    }

    @PutMapping
    @ApiOperation(value = "任务撤回",notes = "注意：当前与目标定义Key为设计模板时任务对应的ID,而非数据主键ID",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processInstanceId", value = "流程实例ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "currentTaskKey", value = "当前任务定义Key", required = true, dataType = "String"),
            @ApiImplicitParam(name = "targetTaskKey", value = "目标任务定义Key", required = true, dataType = "String")
    })
    public ApiResponse withdraw(String processInstanceId, String currentTaskKey, String targetTaskKey) {
        actTaskService.withdraw(processInstanceId, currentTaskKey, targetTaskKey);
        return success("任务撤回成功");
    }


}
