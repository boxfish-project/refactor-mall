package com.boxfishedu.online.mall.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by malu on 16/7/15.
 */
@Data(staticConstructor = "createInstance")
public class ComboVo implements Serializable {
    private Integer page = 1;
    private Integer rows = 20;
    private Long id;
    private Integer skuAmount;
    private Long skuId;
    private String skuName;
    private Date createTime;
    private Date updateTime;
    private Long originalPrice;
    private Long actualPrice;
    private Integer skuCycle;
    private Long unitPrice;
    private String serviceName;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSkuAmount() {
        return skuAmount;
    }

    public void setSkuAmount(Integer skuAmount) {
        this.skuAmount = skuAmount;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Long originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Long getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(Long actualPrice) {
        this.actualPrice = actualPrice;
    }

    public Integer getSkuCycle() {
        return skuCycle;
    }

    public void setSkuCycle(Integer skuCycle) {
        this.skuCycle = skuCycle;
    }

    public Long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "ComboVo{" +
                "page=" + page +
                ", rows=" + rows +
                ", id=" + id +
                ", skuAmount=" + skuAmount +
                ", skuId=" + skuId +
                ", skuName='" + skuName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", originalPrice=" + originalPrice +
                ", actualPrice=" + actualPrice +
                ", skuCycle=" + skuCycle +
                ", unitPrice=" + unitPrice +
                ", serviceName='" + serviceName + '\'' +
                '}';
    }
}
