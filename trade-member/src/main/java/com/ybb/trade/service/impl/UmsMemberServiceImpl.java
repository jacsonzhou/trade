package com.ybb.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ybb.trade.constant.SysConstant;
import com.ybb.trade.entity.AuthMember;
import com.ybb.trade.entity.UmsMember;
import com.ybb.trade.entity.UmsMemberReceiveAddress;
import com.ybb.trade.entity.UmsWallet;
import com.ybb.trade.service.UmsMemberService;
import com.ybb.trade.mapper.UmsMemberMapper;
import com.ybb.trade.service.UmsWalletService;
import com.ybb.trade.vo.LoginVo;
import com.ybb.trade.vo.MemberVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
* @author zhouf
* @description 针对表【ums_member(会员)】的数据库操作Service实现
* @createDate 2024-02-08 13:45:31
*/
@Service
public class UmsMemberServiceImpl extends ServiceImpl<UmsMemberMapper, UmsMember>
    implements UmsMemberService{
    public static List<String> coinList = Arrays.asList("usdt","cny","jpy");
    @Autowired
    private UmsWalletService umsWalletService;
    /**
     * 注册用户
     * 生成账户地址
     * @param phone
     * @param password
     */
    @Override
    @Transactional
    public void register(String phone, String password) {
        UmsMember member = new UmsMember();
        member.setMobile(phone);
        member.setLevelId(1L);//默认等级为1级
        //密码进行MD5加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(password);
        member.setPassword(encode);
        member.setMobile(phone);
        member.setGender(0);
        member.setCreateTime(new Date());
        this.save(member);
        List<UmsWallet> walletList = new ArrayList<>();
        coinList.stream().forEach((e)->{
            UmsWallet wallet = new UmsWallet();
//                    .memberId(member.getId().intValue())
//                    .coin(e)
//                    .createTime(new Date())
//                    .frozenBalance(BigDecimal.ZERO)
//                    .isLock(0)
//                    .status(1)
//                    .balance(BigDecimal.ZERO).build();
            walletList.add(wallet);
        });
        umsWalletService.saveBatch(walletList);
    }

    @Override
    public List<String> listSupportPayCoin() {
        return coinList;
    }

    @Override
    public LoginVo login(String username, String password, HttpServletRequest request, HttpServletResponse response) {

        //1、去数据库查询 SELECT * FROM ums_member WHERE username = ? OR mobile = ?
        UmsMember umsMember = this.baseMapper.selectOne(new QueryWrapper<UmsMember>()
                .eq("username", username).or().eq("mobile", username));

        if (umsMember == null) {
            throw new RuntimeException("用户不存在");
        } else {
            String password1 = umsMember.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean matches = passwordEncoder.matches(password, password1);
            if (matches) {
                //登录成功
                MemberVo memberVo = new MemberVo();
                BeanUtils.copyProperties(umsMember,memberVo);
                AuthMember authMember = AuthMember.toAuthMember(memberVo);
                request.getSession().setAttribute(SysConstant.SESSION_MEMBER, authMember);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.HOUR_OF_DAY, 24 * 7);
                umsMember.setExpiresIn(calendar.getTime().toString());
                LoginVo loginVo = LoginVo.getLoginInfo(memberVo);
                return loginVo;
            }else {
                throw new RuntimeException("密码不正确");
            }
        }


    }
}





