package com.boxfishedu.online.invitation.service;

import com.boxfishedu.component.boxfish.util.string.FormatUtil;
import com.boxfishedu.online.invitation.app.configure.Account;
import com.boxfishedu.online.invitation.app.configure.AccountProperties;
import com.boxfishedu.online.invitation.entity.Invitation;
import com.boxfishedu.online.invitation.mappers.InvitationMapper;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Created by malu on 16/6/15.
 */
@Service
public class InvitationService {
    @Autowired
    private InvitationMapper invitationMapper;

    Logger logger = LoggerFactory.getLogger(InvitationService.class);

    /**
     * 生成邀请码并保存到数据库
     * @param amount 邀请码数量
     * @param userId 用户id
     */
    public void generateInvitations(int amount, Long userId){
        try{
            this.invitationMapper.insertList(this.createInvitations(amount, userId));
        }catch (Exception e){
            this.invitationMapper.insertList(this.createInvitations(amount, userId));
        }
        logger.info("已生成[{}]个邀请码", amount);
    }

    /**
     * 生成邀请码list
     * @param amount 邀请码数量
     * @param userId 用户ID
     * @return 邀请码list
     */
    public List<Invitation> createInvitations(int amount, Long userId){
        String[] chars = new String[]{"A", "B", "C", "D", "E", "F", "2", "3", "4", "5", "S", "T",
                "U", "V", "W", "6", "7", "8", "9", "G", "H",
                "J", "K", "L", "M", "N", "P", "Q", "R", "X", "Y", "Z"};

        List<Invitation> list = Lists.newArrayList();
        for (int i = 0; i < amount; i++) {
            Invitation invitation = Invitation.createInstance();
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < 20; j++) {
                builder.append(chars[new Random().nextInt(chars.length)]);
            }
            invitation.setContent(builder.toString());
            invitation.setOperator(userId);
            invitation.setCreateTime(Calendar.getInstance().getTime());
            invitation.setStatusCode("unused");

            list.add(invitation);
        }

        return list;
    }

    /**
     * 按条件分页查找
     * @param invitation 页面获取的查询条件
     * @return 返回页面记录
     */
    public List<Invitation> findPage(Invitation invitation){
        if(!Objects.isNull(invitation.getPage()) && !Objects.isNull(invitation.getRows())
                && invitation.getRows() != -1){
            PageHelper.startPage(invitation.getPage(), invitation.getRows());
        }

        Example example = new Example(Invitation.class);
        Example.Criteria criteria = example.createCriteria();

        if(!StringUtils.isEmpty(invitation.getContent())){
            criteria.andLike("content", "%" + invitation.getContent() + "%");
        }
        if(!StringUtils.isEmpty(invitation.getUserId())){
            criteria.andEqualTo("userId", invitation.getUserId());
        }
        if(!StringUtils.isEmpty(invitation.getStatusCode())){
            criteria.andEqualTo("statusCode", invitation.getStatusCode());
        }
        example.setOrderByClause("id desc");

        return this.invitationMapper.selectByExample(example);
    }

    /**
     * 校验邀请码是否可用
     * @param content 邀请码
     * @return true/false
     */
    public boolean checkContent(String content){

        if(StringUtils.isEmpty(content) || content.length() != 20){
            return false;
        }
        Example example = new Example(Invitation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("content", content);
        criteria.andEqualTo("statusCode", "unused");
        Integer row = this.invitationMapper.selectCountByExample(example);
        if(1 == row){
            return true;
        }

        return false;
    }

    public boolean checkByUserId(Long userId){ return this.invitationMapper.findByUserId(userId) > 0;}

    public boolean updateStatus(String userId, String orderCode){

        logger.info("尝试修改用户[userId = {}]的邀请码状态为[used]", userId);
        Integer row = this.invitationMapper.updateStatus(userId, orderCode);
        if(1 == row){
            logger.info("用户[userId = {}]的邀请码状态已修改为[used], 关联订单号为[orderCode = {}]", userId, orderCode);
            return true;
        }

        return false;
    }
    /**
     * 查找全部邀请码
     * @return
     */
    public List<Invitation> findAll(){ return this.invitationMapper.selectAll(); }

    /**
     * 生成excel模版
     * @param fileName
     * @return
     */
    public HSSFWorkbook generateExcel(String fileName){
        List<Invitation> codeList = this.findAll();
        //创建workbook
        HSSFWorkbook wb = new HSSFWorkbook();
        //创建sheet
        HSSFSheet sheet = wb.createSheet(fileName);
        //设置第二行(邀请码)宽度
        sheet.setColumnWidth(1, 9200);

        //创建标题
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("ID");
        row.createCell(1).setCellValue("Content");
        row.createCell(2).setCellValue("userId");
        row.createCell(3).setCellValue("Status");
        //循环添加各行数据
        for (int i = 0; i < codeList.size(); i++) {
            HSSFRow r = sheet.createRow(i + 1);
            Invitation code = codeList.get(i);
            if (code.getId() != null) {
                r.createCell(0).setCellValue(code.getId());
            }
            if (code.getContent() != null) {
                r.createCell(1).setCellValue(code.getContent());
            }
            if (code.getUserId() != null) {
                r.createCell(2).setCellValue(code.getUserId());
            }
            if (code.getStatusCode() != null) {
                r.createCell(3).setCellValue(code.getStatusCode());
            }
        }
        return wb;
    }

    @Autowired
    private AccountProperties accountProperties;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 验证登录用户,若存在则分配token
     * @param account 登录用户
     * @return 为用户分配token后返回
     */
    public Account checkUser(Account account){

        for (Account conf : this.accountProperties.getAccounts()) {
            if(conf.getUsername().contentEquals(account.getUsername()) && conf.getPassword().contentEquals(account.getPassword())){
                String access_token = (System.nanoTime() - 1) + "";
                conf.setAccessToken(access_token);
                this.stringRedisTemplate.opsForValue().set("access_token_" + access_token, FormatUtil.toJsonNoException(conf));
                return conf;
            }
        }

        return null;
    }
}
