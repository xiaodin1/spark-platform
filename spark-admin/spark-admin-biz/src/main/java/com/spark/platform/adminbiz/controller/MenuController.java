package com.spark.platform.adminbiz.controller;

import com.spark.platform.adminapi.vo.MenuVue;
import com.spark.platform.adminbiz.service.menu.MenuService;
import com.spark.platform.common.base.support.ApiResponse;
import com.spark.platform.common.base.support.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

/**
 * @ProjectName: spark-platform
 * @Package: com.spark.platform.adminbiz.controller
 * @ClassName: MenuController
 * @Author: wangdingfeng
 * @Description: 菜单APi
 * @Date: 2020/3/16 17:15
 * @Version: 1.0
 */
@RestController
@RequestMapping("/menu")
@Api(tags = "菜单管理")
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/build")
    @ApiOperation(value = "根据用户获取菜单信息")
    public ApiResponse build(Principal principal){
        List<MenuVue> menuVues = menuService.findMenuTree(principal.getName());
        return success(menuVues);
    }

    @GetMapping("/api/findAuthByUserId")
    @ApiOperation(value = "根据用户获取菜单信息")
    public ApiResponse findAuthByUserId(@RequestParam Long userId){
        return success(menuService.findAuthByUserId(userId));
    }

}