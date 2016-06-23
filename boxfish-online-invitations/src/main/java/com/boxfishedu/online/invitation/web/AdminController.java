package com.boxfishedu.online.invitation.web;

import com.boxfishedu.online.invitation.entity.Invitation;
import com.boxfishedu.online.invitation.service.InvitationService;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by malu on 16/6/16.
 * 邀请码页面操作逻辑
 */
@Controller
@RequestMapping("/invitation")
@CrossOrigin
public class AdminController {
    @Autowired
    private InvitationService invitationService;
    /*
    5.删除接口,/delete
        参数content
        boolean

     */
    @RequestMapping(value = "/create/{amount}", method = RequestMethod.GET)
    @ResponseBody
    public void create(@PathVariable int amount, Long userId){
        this.invitationService.generateInvitations(amount, userId);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap list(Invitation invitation){
        ModelMap modelMap = new ModelMap();
        List<Invitation> invitations = this.invitationService.findPage(invitation);

        PageInfo<Invitation> pageInfo = new PageInfo<>(invitations);
        modelMap.addAttribute("page", pageInfo.getPageNum());//当前页号
        modelMap.addAttribute("total", pageInfo.getPages());//总页数
        modelMap.addAttribute("records", pageInfo.getTotal());//总记录数
        modelMap.addAttribute("rows", invitations);//记录

        return modelMap;
    }

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void exportCodes(HttpServletResponse response, Invitation invitation) throws IOException {
        String fileName = "All_Invitation_Codes_" + new SimpleDateFormat("yyyyMMddHHmmss").
                format(Calendar.getInstance().getTime());

        HSSFWorkbook wkb = this.invitationService.generateExcel(fileName);
        //输出Excel文件 
        OutputStream output = response.getOutputStream();
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
        response.setContentType("application/msexcel");
        wkb.write(output);
        output.close();
    }

}
