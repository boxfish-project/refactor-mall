package com.boxfishedu.online.invitation.web;

import com.boxfishedu.online.invitation.app.configure.Account;
import com.boxfishedu.online.invitation.service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

/**
 * Created by malu on 16/6/15.
 * 邀请码登录逻辑
 */
@Controller
public class InvitationController {

    @Autowired
    private InvitationService invitationService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(){ return "login";}

    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    public ModelAndView signIn(Account account){
        ModelAndView modelAndView = new ModelAndView("invitations");
        //校验 返回access_token和role
        account = this.invitationService.checkUser(account);
        if(Objects.isNull(account)){
            modelAndView.setViewName("login");
            modelAndView.addObject("error", "无效的用户名/密码");

            return modelAndView;
        }

        modelAndView.addObject("role", account.getRole());
        modelAndView.addObject("access_token", account.getAccessToken());

        return modelAndView;
    }
}
