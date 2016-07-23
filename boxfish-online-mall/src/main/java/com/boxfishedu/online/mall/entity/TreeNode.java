package com.boxfishedu.online.mall.entity;

import lombok.Data;

/**
 * Created by malu on 16/7/22.
 */
@Data(staticConstructor = "createInstance")
public class TreeNode {
    public String text;
    public String type;
    public AdditionalParameters additionalParameters;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getAdditionalParameters() {
        return additionalParameters;
    }

    public void setAdditionalParameters(AdditionalParameters additionalParameters) {
        this.additionalParameters = additionalParameters;
    }
}
