package com.boxfishedu.component.aliyun.mail;


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.boxfishedu.component.boxfish.util.mail.BeMailAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AliMailConnector implements BeMailAgent {

    private static Logger logger = LoggerFactory.getLogger(AliMailConnector.class);

    private IAcsClient client;
    private String accountName;
    private String msgTo;
    private String subject;


    public AliMailConnector(AliMailConnectorParameters parameters) {
        accountName = parameters.getAccountName();
        msgTo = parameters.getMsgTo();
        subject = parameters.getSubject();

        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", parameters.getAccessKey(), parameters.getAccessSecret());
        client = new DefaultAcsClient(profile);
    }

    public void sendMail(String content) {
        try {
            SingleSendMailRequest request = new SingleSendMailRequest();
            request.setAccountName(accountName);
            request.setAddressType(1);
            request.setReplyToAddress(true);
            request.setToAddress(msgTo);
            request.setSubject(subject);
            request.setTextBody(content);

            SingleSendMailResponse httpResponse = client.getAcsResponse(request);

        } catch (Exception e) {
            logger.error("[BoxFishComponent] [sendMail] 邮件发送异常. EXCEPTION=[{}]", e.toString());
        }
    }
}
