package com.boxfishedu.component.boxfish.util.mail;

import lombok.Data;


@Data
public class BeMail {
    private String msgTo;
    private String subject;
    private String content;
}
