package com.boxfishedu.component.aliyun.mail;


import lombok.Data;


@Data
public class AliMailConnectorParameters {

    private String accessKey;
    private String accessSecret;
    private String accountName;

    private String msgTo;
    private String subject;
}
