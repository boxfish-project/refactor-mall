package com.boxfishedu.online.mall.web;

import com.boxfishedu.online.mall.entity.ComboVo;
import com.boxfishedu.online.mall.entity.TreeNode;
import com.boxfishedu.online.mall.service.ProductSkuAdminService;
import com.boxfishedu.online.mall.service.ProductSkuComboService;
import com.boxfishedu.protocal.exceptions.InvalidInputResponseBuilder;
import com.boxfishedu.protocal.model.CommonResult;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.PanelUI;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.isNull;

/**
 * Created by malu on 16/7/15.
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ProductSkuAdminService adminService;
    @Autowired
    private ProductSkuComboService skuComboService;

    /**
     * 创建套餐记录
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult createCombo(@RequestBody ComboVo comboVo, HttpServletResponse response) throws IOException {
        Boolean created = this.adminService.createCombo(comboVo);
        if(!created){
            InvalidInputResponseBuilder.unauthorized(response, "套餐已存在或参数有误!");
            return null;
        }

        return CommonResult.createCommonResult(created);
    }

    /**
     * 表格数据加载
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap test(ComboVo comboVo){
        ModelMap modelMap = new ModelMap();
        List<ComboVo> comboVos = this.adminService.queryCombos(comboVo);

        PageInfo<ComboVo> pageInfo = new PageInfo<>(comboVos);
        modelMap.addAttribute("page", pageInfo.getPageNum());//当前页号
        modelMap.addAttribute("total", pageInfo.getPages());//总页数
        modelMap.addAttribute("records", pageInfo.getTotal());//总记录数
        modelMap.addAttribute("rows", comboVos);//记录

        return modelMap;
    }

    // FIXME: 16/7/22 表格的数据修改路径默认为post,找不到设置请求方式的地方
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult updateCombo(Long id, Long actualPrice){
        return CommonResult.createCommonResult(this.adminService.updateCom(id, actualPrice));
    }

    // FIXME: 16/7/22 表格数据删除路径默认为post,找不到设置请求方式的地方
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public CommonResult deleteCombo(Long id){
        Boolean row = this.adminService.deleteComboById(id);
        return CommonResult.createCommonResult(row);
    }

    /**
     * 新增combo页面的下拉框联动
     * @return
     */
    @RequestMapping(value = "/getServiceNameData", method = RequestMethod.GET)
    public CommonResult getServiceData(){
        List<Map<String, Object>>result = this.adminService.getServiceData();
        return CommonResult.createCommonResult(result);
    }

    @RequestMapping(value = "/getSkuNameData/{serviceId}", method = RequestMethod.GET)
    public CommonResult getSkuData(@PathVariable Long serviceId){
        List<Map<String, Object>> result = this.adminService.getSkuData(serviceId);
        return CommonResult.createCommonResult(result);
    }

    @RequestMapping(value = "/queryUnitPrice/{skuId}", method = RequestMethod.GET)
    public CommonResult queryUnitPrice(@PathVariable Long skuId){
        Long unitPrice = this.adminService.queryUnitPrice(skuId);
        return CommonResult.createCommonResult(unitPrice);
    }

    /**
     * 生成树的加载数据
     */
    @RequestMapping(value = "/tree/initDate", method = RequestMethod.GET)
    public CommonResult initTreeData(){
        List<TreeNode> treeData = this.adminService.treeData();
        return CommonResult.createCommonResult(treeData);
    }

}
