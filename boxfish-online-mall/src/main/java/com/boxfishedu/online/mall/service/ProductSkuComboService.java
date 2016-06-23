package com.boxfishedu.online.mall.service;

import com.boxfishedu.online.mall.app.configure.ConfigProperties;
import com.boxfishedu.online.mall.entity.ProductSkuCombo;
import com.boxfishedu.online.mall.entity.ProductSkuValue;
import com.boxfishedu.online.mall.mappers.SkuComboMapper;
import com.boxfishedu.protocal.exceptions.BusinessException;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.boxfishedu.component.boxfish.util.string.FormatUtil.fromJson;
import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by malu on 16/6/18.
 */
@Service
public class ProductSkuComboService {
    @Autowired
    private SkuComboMapper skuComboMapper;
    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private ConfigProperties configProperties;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final String ALL_COMBOS_REDIS_KEY = "com.boxfishedu.services.SKUComboService.all-combos";
    private final Logger logger = LoggerFactory.getLogger(ProductSkuComboService.class);

    /**
     * 从redis中读取所有combo
     * @throws Exception
     */
    private List<ProductSkuCombo> getAllCombosFromRedis() throws Exception {
        List<String> values = this.redisTemplate.opsForList().range(ALL_COMBOS_REDIS_KEY, 0, -1);
        List<ProductSkuCombo> list = newArrayList();
        for (String value : values) {
            ProductSkuCombo combo = fromJson(value, ProductSkuCombo.class);
            list.add(combo);
        }
        return list;
    }

    /**
     * 从数据库获取所有Combo
     * @return combos
     */
    public List<ProductSkuCombo> getAllCombosFromMySQL(){ return this.skuComboMapper.selectAllCom();}

    /**
     * 获取combo
     * @return
     */
    public List<ProductSkuCombo> getAllCombos(){
        List<ProductSkuCombo> skuCombos;
        try{
            skuCombos = this.getAllCombosFromRedis();
            if(!skuCombos.isEmpty()){
                return skuCombos;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Redis exception,get combos from MySQL!");
        }

        try{
            return this.getAllCombosFromMySQL();
        }catch (Exception e){
            e.printStackTrace();
            logger.error("MySql exception,contact the administrator");
        }

        throw new BusinessException("sorry,service has some error,pls try later.");
    }

    /**
     * 生成skus
     * @return
     */
    public List<ProductSkuCombo> generateSKUCombo(){
        this.redisTemplate.delete(ALL_COMBOS_REDIS_KEY);

        List<ProductSkuCombo> iterable = newArrayList();
        List<ProductSkuValue> skuList = this.productSkuService.getAllSKU();

        boolean freeAdded = true;
        for (ProductSkuValue sku : skuList) {
            if (freeAdded) {
                ProductSkuCombo freeCombo = this.generateSKUCombo(sku, 2, -1);
                iterable.add(freeCombo);
                freeAdded = false;
            }

            for (String cnt : configProperties.getCountNeedCreate()) {
                Integer amount = Integer.parseInt(cnt);

                for (String cycleStr : configProperties.getCycleNeedCreate()) {
                    Integer cycle = Integer.parseInt(cycleStr);

                    ProductSkuCombo skuCombo = this.generateSKUCombo(sku, amount, cycle);
                    iterable.add(skuCombo);
                }
            }
        }

        List<ProductSkuCombo> saved = this.save(iterable);
        try {
            this.redisTemplate.delete(ALL_COMBOS_REDIS_KEY);
            for (ProductSkuCombo combo : saved) {
                this.redisTemplate.opsForList().rightPush(ALL_COMBOS_REDIS_KEY, combo.toJson());
            }
            this.redisTemplate.expire(ALL_COMBOS_REDIS_KEY, 24, TimeUnit.HOURS);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("there are sth. error when save data into redis.see the logs for debug.");
        }
        logger.info("All of the com.boxfishedu.order.service combos has been generated complete.");

        return saved;
    }

    public ProductSkuCombo generateSKUCombo(ProductSkuValue sku, Integer amount, Integer cycle){

        Example example = new Example(ProductSkuCombo.class);
        Example.Criteria criteria = example.createCriteria();
        if(!Objects.isNull(sku)){
            criteria.andEqualTo("skuId", sku.getId());
        }
        if(!StringUtils.isEmpty(amount)){
            criteria.andEqualTo("skuAmount", amount);
        }
        if(!StringUtils.isEmpty(cycle)){
            criteria.andEqualTo("skuCycle", cycle);
        }

        List<ProductSkuCombo> combos = this.skuComboMapper.selectByExample(example);
        ProductSkuCombo skuCombo = ProductSkuCombo.createInstance();
        if(!Objects.isNull(combos) && combos.size() > 0){
            skuCombo = combos.get(0);
        }

        //每月上课的次数 * 原始单价 * 周期 = 默认价格
        skuCombo.setSkuCycle(cycle);
        skuCombo.setSkuAmount(amount);
        //FIXME 现阶段把价格直接写死
        switch (amount) {
            case 8:
                skuCombo.setActualPrice(49800L);
                break;
            case 16:
                skuCombo.setActualPrice(98800L);
                break;
            default:
                skuCombo.setActualPrice(0L);
        }
        skuCombo.setOriginalPrice(skuCombo.getActualPrice());

        skuCombo.setServiceSKU(sku);
        skuCombo.setSkuId(sku.getId());
        skuCombo.setCreateTime(Calendar.getInstance().getTime());

        return skuCombo;
    }

    /**
     * 校验combo是否存在
     * @param skuCombo
     * @return
     */
    public ProductSkuCombo checkCombo(ProductSkuCombo skuCombo){
        Long comboId = skuCombo.getId();
        Long price = skuCombo.getActualPrice();
        logger.debug("Check validation of [sku_combo_id = {},price = {}]", comboId, price);
        List<ProductSkuCombo> list = this.getAllCombos();
        for (ProductSkuCombo combo : list) {
            logger.debug("origin_id = {},current_id = {}", combo.getId(), comboId);
            logger.debug("origin_price = {},current_price = {}", combo.getActualPrice(), price);

            if(Objects.equals(comboId, combo.getId()) && Objects.equals(price, combo.getActualPrice())){
                logger.info("[sku_combo_id = {},price = {}]exists and can be used.", comboId, price);
                return combo;
            }
        }
        logger.error("[sku_combo_id = {},price = {}]NOT EXISTS OR INVALID PRICE.", comboId, price);
        throw new BusinessException("Product NOT EXISTS OR INVALID PRICE.");
    }

    /**
     * insert or update skus
     * @param iterable
     * @return
     */
    public List<ProductSkuCombo> save(List<ProductSkuCombo> iterable){
        List<ProductSkuCombo> skuCombos = Lists.newArrayList();
        try{
            for (ProductSkuCombo skuCombo : iterable) {
                int row = 0;
                ProductSkuCombo combo = this.skuComboMapper.selectByPrimaryKey(skuCombo);
                if(!Objects.isNull(combo)){
                    row = this.skuComboMapper.updateByPrimaryKey(skuCombo);
                }else{
                    row = this.skuComboMapper.insert(skuCombo);
                }

                if(row == 1){
                    skuCombos.add(skuCombo);
                }
            }

        }catch (Exception e){
            logger.error("there are sth. error when save data. see the logs for debug.");
            e.printStackTrace();
        }
        return skuCombos;
    }
}
