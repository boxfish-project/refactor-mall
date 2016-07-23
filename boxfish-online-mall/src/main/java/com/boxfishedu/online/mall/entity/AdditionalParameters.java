package com.boxfishedu.online.mall.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by malu on 16/7/22.
 */
@Data(staticConstructor = "createInstance")
public class AdditionalParameters {
    public Long id;
    public List<TreeNode> children;
    public Long parentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
