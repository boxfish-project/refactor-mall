package com.boxfishedu.component.boxfish.util.mail;

import lombok.Data;


@Data
public class BeMailAgentImpParameters {

    private String url;
    private String username;
    private String password;

    private String msgTo;
    private String subject;
}
