package com.boxfishedu.online.invitation.web;

import com.boxfishedu.online.invitation.entity.Invitation;
import com.boxfishedu.online.invitation.service.InvitationService;
import com.boxfishedu.protocal.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.boxfishedu.protocal.model.CommonResult.createCommonResult;

/**
 * Created by malu on 16/6/16.
 * 使用邀请码
 */
@RestController
@RequestMapping("/invitation-check")
public class ValidataController {

    @Autowired
    private InvitationService service;

    @RequestMapping(value = "/one", method = RequestMethod.GET)
    public String getOne(){
        Invitation invitation = Invitation.createInstance();
        invitation.setPage(0);
        invitation.setRows(1);
        invitation.setStatusCode("unused");
        return this.service.findPage(invitation).get(0).getContent();
    }

    @RequestMapping(value = "/check/{userId}", method = RequestMethod.GET)
    public CommonResult check(@PathVariable Long userId){
        return createCommonResult(this.service.checkByUserId(userId));
    }

    @RequestMapping(value = "/use/{userId}/{orderCode}", method = RequestMethod.GET)
    public CommonResult use(@PathVariable String userId, @PathVariable String orderCode){
        return createCommonResult(this.service.updateStatus(userId, orderCode));
    }

}
