package com.time.canvas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mongodb.internal.bulk.UpdateRequest;
import com.time.canvas.domain.User;
import com.time.canvas.exception.BusinessException;
import com.time.canvas.mapper.UserMapper;
import com.time.canvas.model.UserLoginRequest;
import com.time.canvas.model.UserRegisterRequest;
import com.time.canvas.model.UserVO;
import com.time.canvas.model.dto.UserUpdateRequest;
import com.time.canvas.repository.UserRepository;
import com.time.canvas.service.UserService;
import com.time.canvas.util.ErrorCode;
import com.time.canvas.util.JwtTokenUtil;
import com.time.canvas.constant.UserConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * @author wangliang
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2025-05-14 14:12:43
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public long userRegister(UserRegisterRequest userRegisterRequest) {
        // 1. 校验参数
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String userName = userRegisterRequest.getUserName();
        
        // 校验参数是否为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, userName)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        
        // 账号长度不小于4位
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度不能小于4位");
        }
        
        // 密码长度不小于8位
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度不能小于8位");
        }
        
        // 账号不能包含特殊字符
        if (!userAccount.matches("^[a-zA-Z0-9_]+$")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能包含特殊字符");
        }
        
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        
        // 账号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在");
        }
        
        // 2. 加密密码
        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userPassword).getBytes());
        
        // 3. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName(userName);
        user.setUserEmail(userRegisterRequest.getUserEmail());
        user.setUserPhone(userRegisterRequest.getUserPhone());
        user.setUserRole(UserConstant.NORMAL_ROLE); // 默认为普通用户
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }
        
        return user.getId();
    }

    @Override
    public UserVO userLogin(UserLoginRequest userLoginRequest) {
        // 1. 校验参数
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        
        // 校验参数是否为空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        
        // 2. 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        
        // 3. 校验密码
        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userPassword).getBytes());
        if (!encryptPassword.equals(user.getUserPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        
        // 4. 更新登录时间
        user.setLastLoginTime(new Date());
        this.updateById(user);
        
        // 5. 生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("userRole", user.getUserRole());
        String token = jwtTokenUtil.generateToken(userAccount, claims);
        
        // 6. 返回用户信息
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setToken(token);
        
        return userVO;
    }

    @Override
    public UserVO getCurrentUser(String token) {
        if (StringUtils.isBlank(token)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        
        try {
            String userAccount = jwtTokenUtil.getUsernameFromToken(token);
            User user = getUserByAccount(userAccount);
            if (user == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
            }
            
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            userVO.setToken(token);
            return userVO;
        } catch (Exception e) {
            log.error("获取当前用户信息失败", e);
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
    }

    @Override
    public void userLogout(String token) {
        // JWT无状态，不需要服务端处理，客户端删除token即可
        // 如果需要服务端处理，可以使用Redis存储黑名单
    }

    @Override
    public User getUserByAccount(String userAccount) {
        if (StringUtils.isBlank(userAccount)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号为空");
        }
        
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        return this.getOne(queryWrapper);
    }

    /**
     * 更新用户个人资料
     * @param userUpdateRequest 包含需要更新的字段的请求体
     * @return 是否更新成功
     */
    @Override
    public boolean updateUserProfile(UserUpdateRequest userUpdateRequest) {
        // 1. 校验参数
        if (userUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        // 2. 从安全上下文中获取当前用户的认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        // 在JwtAuthenticationFilter中将用户账号设置为了principal
        String userAccount = null;
        if (authentication.getPrincipal() instanceof Map) {
            userAccount = (String) ((Map<?, ?>) authentication.getPrincipal()).get("username");
        } else if (authentication.getPrincipal() instanceof String) {
            userAccount = (String) authentication.getPrincipal();
        } else {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        User user = getUserByAccount(userAccount);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }

        // 3. 根据用户账号查询数据库中的用户记录
        User existingUser = getUserByAccount(userAccount);
        if (existingUser == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "当前登录用户不存在");
        }

        // 4. 更新允许修改的字段
        if (StringUtils.isNotBlank(userUpdateRequest.getUserName())) {
            existingUser.setUserName(userUpdateRequest.getUserName());
        }
        if (StringUtils.isNotBlank(userUpdateRequest.getUserEmail())) {
            existingUser.setUserEmail(userUpdateRequest.getUserEmail());
        }
        if (StringUtils.isNotBlank(userUpdateRequest.getUserPhone())) {
            existingUser.setUserPhone(userUpdateRequest.getUserPhone());
        }
        if (StringUtils.isNotBlank(userUpdateRequest.getBio())) {
            existingUser.setBio(userUpdateRequest.getBio());
        }
        if (userUpdateRequest.getGender() != null) {
            existingUser.setGender(String.valueOf(userUpdateRequest.getGender()));
        }
        if (StringUtils.isNotBlank(userUpdateRequest.getBirthDate())) {
            String birthDateStr = userUpdateRequest.getBirthDate();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false); // 设置严格日期解析，防止无效日期如2月30日
                 // 校验格式和是否是有效日期
                Date birthDate = sdf.parse(birthDateStr);
                // Optionally: Add checks for reasonable date range (e.g., not in the future)
                // if (birthDate.after(new Date())) {
                //     throw new BusinessException(ErrorCode.PARAMS_ERROR, "生日不能是未来的日期");
                // }
                existingUser.setBirthDate(birthDate);
            } catch (ParseException e) {
                log.error("生日日期格式错误或无效日期", e);
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "生日日期格式不正确或无效日期，应为 yyyy-MM-dd");
            } catch (Exception e) {
                 // 捕获其他可能的异常
                 log.error("处理生日日期时发生未知错误", e);
                 throw new BusinessException(ErrorCode.SYSTEM_ERROR, "处理生日日期时发生未知错误");
            }
        }

        // 5. 设置更新时间
        existingUser.setUpdateTime(new Date());

        // 6. 调用Mapper更新数据库
        boolean updateResult = this.updateById(existingUser);

        if (!updateResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新用户信息失败");
        }

        return true;
    }
}




