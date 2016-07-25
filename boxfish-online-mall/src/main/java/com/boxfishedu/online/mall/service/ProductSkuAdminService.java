package com.boxfishedu.online.mall.service;

import com.boxfishedu.online.mall.entity.AdditionalParameters;
import com.boxfishedu.online.mall.entity.ComboVo;
import com.boxfishedu.online.mall.entity.ProductSkuCombo;
import com.boxfishedu.online.mall.entity.TreeNode;
import com.boxfishedu.online.mall.mappers.ComboMapper;
import com.boxfishedu.online.mall.mappers.SkuComboMapper;
import com.boxfishedu.online.mall.mappers.SkuValueMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.constructor.Construct;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by malu on 16/7/15.
 */
@Service
public class ProductSkuAdminService {
    @Autowired
    private ComboMapper comboMapper;
    @Autowired
    private SkuComboMapper skuComboMapper;
    @Autowired
    private SkuValueMapper skuValueMapper;
    @Value("${conf.defaultPageNum}")
    private int defaultPageNum;

    /**
     * 分页查询符合条件的套餐
     * @return
     */
    public List<ComboVo> queryCombos(ComboVo comboVo){
        Map<String, Object>paramMap = Maps.newHashMap();
        paramMap.put("skuId", comboVo.getSkuId());
        Integer skuAmount = comboVo.getSkuAmount();
        paramMap.put("skuAmount", (skuAmount!=null && skuAmount==0)?"0":skuAmount);
        Integer skuCycle = comboVo.getSkuCycle();
        paramMap.put("skuCycle", (skuCycle!=null && skuCycle==0)?"0":skuCycle);
        Long originallPrice = comboVo.getOriginalPrice();
        paramMap.put("originallPrice", (originallPrice!=null && originallPrice==0l)?"0":originallPrice);//mapper的查询条件将0(Long)判断为"",于是将0转为字符串类型
        Long price = comboVo.getActualPrice();
        paramMap.put("actualPrice", (price!=null && price==0l)?"0":price);//mapper的查询条件将0(Long)判断为"",于是将0转为字符串类型

        Map<String, String> pageMap = Maps.newHashMap();
        pageMap.put("pageNum", comboVo.getRows().toString());
        pageMap.put("pageNo", comboVo.getPage().toString());
        paramMap.putAll(this.generatePageParam(pageMap));

        return this.comboMapper.queryComboWithPage(paramMap);
    }

    /**
     * 新建套餐记录
     * @return
     */
    public Boolean createCombo(ComboVo comboVo){
        Integer row = this.comboMapper.checkComboByAmount(comboVo.getSkuId(), comboVo.getSkuAmount(), comboVo.getSkuCycle());
        if(row != 0){
            return false;
        }

        ProductSkuCombo combo = ProductSkuCombo.createInstance();
        combo.setSkuId(comboVo.getSkuId());
        combo.setSkuAmount(comboVo.getSkuAmount());
        combo.setSkuCycle(comboVo.getSkuCycle());
        combo.setActualPrice(comboVo.getActualPrice());
        combo.setOriginalPrice(comboVo.getUnitPrice()*comboVo.getSkuAmount());
        combo.setCreateTime(Calendar.getInstance().getTime());
        this.skuComboMapper.insert(combo);

        return true;
    }

    /**
     * 修该套餐价格
     * @return
     */
    public Boolean updateCom(Long comboId, Long actualPrice){
        return this.comboMapper.updateComboById(comboId, actualPrice) > 0;
    }

    /**
     * 按id删除指定套餐
     * @return
     */
    public Boolean deleteComboById(Long comboId){
        return this.comboMapper.deleteComboById(comboId) > 0;
    }

    /**
     * 创建订单页面下拉框数据查询
     * @return
     */
    public List<Map<String , Object>> getServiceData(){
        List<Map<String, Object>> result = Lists.newArrayList();

        List<ComboVo> list = this.comboMapper.queryServiceData();
        if (!list.isEmpty()){
            for (ComboVo vo : list) {
                Map<String, Object>map = Maps.newHashMap();
                map.put("id",vo.getId().toString());
                map.put("text", vo.getServiceName());

                result.add(map);
            }
        }
        return  result;
    }

    public List<Map<String, Object>> getSkuData(Long serviceId){
        List<Map<String, Object>> result = Lists.newArrayList();

        List<ComboVo> list = this.comboMapper.querySkuData(serviceId);
        if (!list.isEmpty()){
            for (ComboVo vo : list) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", vo.getId().toString());
                map.put("text", vo.getSkuName());
                result.add(map);
            }
        }
        return result;
    }

    public Long queryUnitPrice(Long skuId){
        return this.comboMapper.queryUnitPrice(skuId);
    }

    /**
     * 设置分页参数
     * @return
     */
    public Map<String, Object> generatePageParam(Map<String, String> param) {
        Map<String, Object> paramMap = Maps.newHashMap();
        //分页参数
        int pageNum = (!StringUtils.isEmpty(param.get("pageNum"))) ? Integer.valueOf(param.get("pageNum")) : defaultPageNum;
        int pageNo = (!StringUtils.isEmpty(param.get("pageNo"))) ? Integer.valueOf(param.get("pageNo")) : 0;
        int rowOffset = (pageNo-1) * pageNum;
        paramMap.put("rowOffset", rowOffset);
        paramMap.put("pageNum", pageNum);

        return paramMap;
    }

    /**
     * 树节点页面使用
     */
    public List<TreeNode> treeData(){
        //返回前台的json字符串
        List<TreeNode> result = Lists.newArrayList();
        //父节点的id/text信息
        List<ComboVo> list = this.comboMapper.queryParNode();
        if(!list.isEmpty()){
            for (ComboVo vo : list) {
                TreeNode node = TreeNode.createInstance();
                node.setText(vo.getServiceName());
                //设置父节点的AdditionalParameters属性(添加子节点)
                AdditionalParameters obj = AdditionalParameters.createInstance();
                obj.setId(vo.getId());
                List<TreeNode> children = this.getChildren(vo.getId());
                //判断有无子节点
                if(children.isEmpty()){
                    node.setType("item");
                }else{
                    node.setType("folder");
                    obj.setChildren(children);
                }

                node.setAdditionalParameters(obj);
                //将父节点添加到list中
                Map<String, Object> map = Maps.newHashMap();
                map.put(vo.getServiceName(), node);
                result.add(node);
            }

        }
        return result;
    }

    private List<TreeNode> getChildren(Long parentId){
        List<TreeNode> children = Lists.newArrayList();
        List<ComboVo> childlist = this.comboMapper.queryChildNode(parentId);
        if(!childlist.isEmpty()){
            for (ComboVo combo : childlist) {
                //生成子节点,设置子节点属性
                TreeNode child = TreeNode.createInstance();
                child.setText(combo.getSkuName());
                child.setType("item");
                AdditionalParameters o = AdditionalParameters.createInstance();
                o.setParentId(parentId);
                o.setId(combo.getId());
                child.setAdditionalParameters(o);
                //将子节点添加到子节点列表中
                children.add(child);
            }
        }
        return children;
    }

}
