package com.boxfishedu.online.mall.service;

import com.boxfishedu.online.mall.entity.*;
import com.boxfishedu.online.mall.mappers.*;
import com.boxfishedu.protocal.enums.Flag;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

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
    @Autowired
    private SkuKeyMapper skuKeyMapper;
    @Autowired
    private SkuComboVoMapper skuComboVoMapper;
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
    // FIXME: 16/7/25 并发?
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

    /**
     * 树节点维护
     */
    public List<ProductSkuKey> queryServices(ProductSkuKey productSkuKey){
        if(!Objects.isNull(productSkuKey.getPage()) && !Objects.isNull(productSkuKey.getRows())){
            PageHelper.startPage(productSkuKey.getPage(), productSkuKey.getRows());
        }

        Example example = new Example(ProductSkuKey.class);
        Example.Criteria criteria = example.createCriteria();

        if(!StringUtils.isEmpty(productSkuKey.getServiceName())){
            criteria.andLike("serviceName", "%" + productSkuKey.getServiceName() + "%");
        }
        if(!StringUtils.isEmpty(productSkuKey.getServiceCode())){
            criteria.andEqualTo("serviceCode", productSkuKey.getServiceCode());
        }
        if(!StringUtils.isEmpty(productSkuKey.getFlagEnable())){
            criteria.andEqualTo("flagEnable", productSkuKey.getFlagEnable());
        }
        example.setOrderByClause("id");

        return this.skuKeyMapper.selectByExample(example);
    }

    public Integer createService(ProductSkuKey productSkuKey){
        if(this.skuKeyMapper.queryCountByName(productSkuKey.getServiceName()) != 0){
            return 0;
        }
        ProductSkuKey key = ProductSkuKey.createInstance();
        key.setServiceName(productSkuKey.getServiceName());
        key.setFlagEnable(Flag.ENABLE);
        key.setDescription(productSkuKey.getDescription());
        key.setServiceCode(productSkuKey.getServiceCode());//自己填写
        key.setCreateTime(Calendar.getInstance().getTime());
        key.setStartTime(Calendar.getInstance().getTime());
        key.setStopTime(Calendar.getInstance().getTime());
        key.setDeadline(Calendar.getInstance().getTime());
        return this.skuKeyMapper.insert(key);
    }

    public Integer delService(ProductSkuKey productSkuKey){
        return this.skuKeyMapper.deleteByPrimaryKey(productSkuKey.getId());
    }

    public Integer updateService(ProductSkuKey productSkuKey){
        if(this.skuKeyMapper.queryCountByNameAndId(productSkuKey.getServiceName(), productSkuKey.getId()) != 0){
            return 0;
        }
        return this.skuKeyMapper.updateById(productSkuKey.getId(), productSkuKey.getServiceName(), productSkuKey.getFlagEnable().toString(), productSkuKey.getDescription());
    }

    public List<SkuValueVo>quertSkusWithPage(SkuValueVo skuValueVo){
        Map<String, Object>paramMap = Maps.newHashMap();
        paramMap.put("serviceName", skuValueVo.getServiceName());
        paramMap.put("skuName", skuValueVo.getSkuName());

        Map<String, String> pageMap = Maps.newHashMap();
        pageMap.put("pageNum", skuValueVo.getRows().toString());
        pageMap.put("pageNo", skuValueVo.getPage().toString());
        paramMap.putAll(this.generatePageParam(pageMap));

        return this.skuComboVoMapper.querySkusWithPage(paramMap);
    }

    public Integer updateSkuCombo(SkuValueVo skuValueVo){
        if(this.skuComboVoMapper.queryByNameAndName(skuValueVo.getSkuName(), skuValueVo.getId()) != 0){
            return 0;
        }
        return this.skuComboVoMapper.updateSkuCombo(skuValueVo.getId(), skuValueVo.getSkuName(), skuValueVo.getFlagEnable(),
                skuValueVo.getFlagVisible(), skuValueVo.getOriginalPrice(), skuValueVo.getDescription());
    }

    public Integer delSkuCombo(Long id){
        return this.skuValueMapper.deleteByPrimaryKey(id);
    }

    public Integer createSkuCombo(SkuValueVo skuValueVo){
        ProductSkuValue valueVo = ProductSkuValue.createInstance();
        if(this.skuComboVoMapper.querySkuComboByName(skuValueVo.getSkuName()) != 0){
            return 0;
        }
        valueVo.setServiceId(skuValueVo.getServiceId());
        valueVo.setSkuName(skuValueVo.getSkuName());
        valueVo.setOriginalPrice(skuValueVo.getOriginalPrice());
        valueVo.setDescription(skuValueVo.getDescription());

        valueVo.setFlagEnable(Flag.ENABLE);
        valueVo.setFlagVisible(Flag.ENABLE);
        valueVo.setDeadline(Calendar.getInstance().getTime());
        valueVo.setValidDay(365);
        valueVo.setCreateTime(Calendar.getInstance().getTime());
        valueVo.setSkuCode("1234");
        valueVo.setDeadline(Calendar.getInstance().getTime());
        return this.skuValueMapper.insert(valueVo);
    }

}
