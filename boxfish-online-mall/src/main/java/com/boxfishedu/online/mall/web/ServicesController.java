package com.boxfishedu.online.mall.web;

import com.boxfishedu.online.mall.entity.ProductSkuCombo;
import com.boxfishedu.online.mall.entity.ProductSkuValue;
import com.boxfishedu.online.mall.service.ProductSkuComboService;
import com.boxfishedu.online.mall.service.ProductSkuService;
import com.boxfishedu.protocal.model.CommonResult;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.List;

import static com.boxfishedu.component.boxfish.util.string.FormatUtil.toJsonNoException;
import static com.boxfishedu.protocal.model.CommonResult.createCommonResult;

/**
 * Created by malu on 16/6/18.
 */
@RestController
@RequestMapping("s-mall")
public class ServicesController {

    @Autowired
    private ProductSkuComboService skuComboService;
    @Autowired
    private ProductSkuService skuService;

    private Logger logger = org.slf4j.LoggerFactory.getLogger(ServicesController.class);

    @RequestMapping(value = "/comboss", method = RequestMethod.GET)
    public CommonResult getAllCombos(){
        return createCommonResult(this.skuComboService.getAllCombos());
    }

    @RequestMapping(value = "/check/{comboId}/{price}", method = RequestMethod.GET)
    public CommonResult checkCombo(@PathVariable Long comboId, @PathVariable Long price){
        ProductSkuCombo skuCombo = ProductSkuCombo.createInstance();
        skuCombo.setId(comboId);
        skuCombo.setActualPrice(price);
        logger.debug("[/s-mall/check] - 请求参数 - [comboId = {},price = {}]", comboId, price);
        ProductSkuCombo combo = this.skuComboService.checkCombo(skuCombo);
        logger.debug("[/s-mall/check] - 响应内容 - [{}]", toJsonNoException(combo));

        return createCommonResult(combo);
    }

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public CommonResult initCombos(){
        return createCommonResult(this.skuComboService.generateSKUCombo());
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult getAllServices(){
        List<ProductSkuValue> skuValues = this.skuService.getAllSKU();
        return createCommonResult(skuValues);
    }
}
