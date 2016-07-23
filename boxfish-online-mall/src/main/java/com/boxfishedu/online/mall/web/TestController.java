package com.boxfishedu.online.mall.web;

import com.boxfishedu.online.mall.entity.ComboVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by malu on 16/7/20.
 */
@Controller
@RequestMapping("/mall")
public class TestController {

    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    public ModelAndView signIn(ComboVo comboVo){
        ComboVo combo = comboVo;
        return new ModelAndView("combos");
    }
}
