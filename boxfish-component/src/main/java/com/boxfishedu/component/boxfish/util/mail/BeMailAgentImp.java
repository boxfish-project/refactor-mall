package com.boxfishedu.component.boxfish.util.mail;


import com.boxfishedu.component.boxfish.util.http.HttpAsyncClient;
import com.boxfishedu.component.boxfish.util.http.HttpAsyncClientBuilder;
import com.boxfishedu.component.boxfish.util.string.FormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeMailAgentImp implements BeMailAgent {

    private static Logger logger = LoggerFactory.getLogger(BeMailAgentImp.class);

    private HttpAsyncClient httpAsyncClient;

    private String url;
    private String msgTo;
    private String subject;


    public BeMailAgentImp(BeMailAgentImpParameters parameters) {
        httpAsyncClient = new HttpAsyncClient(HttpAsyncClientBuilder.build(parameters.getUsername(), parameters.getPassword()));

        url = parameters.getUrl();
        msgTo = parameters.getMsgTo();
        subject = parameters.getSubject();
    }

    public void sendMail(String content) {
        BeMail boxFishMail = new BeMail();
        try {
            boxFishMail.setMsgTo(msgTo);
            boxFishMail.setSubject(subject);
            boxFishMail.setContent(content);

            httpAsyncClient.post(url, FormatUtil.toJson(boxFishMail));
        } catch (Exception e) {
            logger.error("[BoxFishComponent] [sendMail] 邮件发送失败. EXCEPTION=[{}]", e.toString());
        }
    }

}
