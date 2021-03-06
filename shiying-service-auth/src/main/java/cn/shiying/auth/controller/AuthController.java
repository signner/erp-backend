package cn.shiying.auth.controller;

import cn.shiying.auth.client.UserClient;
import cn.shiying.auth.service.AuthService;
import cn.shiying.common.dto.Result;
import cn.shiying.common.entity.sys.SysMenu;
import cn.shiying.common.entity.sys.form.SysLoginForm;
import cn.shiying.common.entity.token.AuthToken;
import cn.shiying.common.entity.token.JwtUser;
import cn.shiying.common.enums.ErrorEnum;
import cn.shiying.common.exception.ExceptionCast;
import cn.shiying.common.utils.CookieUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/")
public class AuthController {

    @Value("${auth.clientId}")
    String clientId;
    @Value("${auth.clientSecret}")
    String clientSecret;
    @Value("${auth.cookieDomain}")
    String cookieDomain;
    @Value("${auth.cookieMaxAge}")
    int cookieMaxAge;

    @Autowired
    AuthService authService;

    @Autowired
    UserClient userClient;

    @PostMapping("/userlogin")
    public Result login(@RequestBody SysLoginForm loginRequest) {
        System.out.println(loginRequest);
        if(loginRequest == null || StringUtils.isEmpty(loginRequest.getUsername())){
            ExceptionCast.cast(ErrorEnum.USERNAME_IS_NULL);
        }
        if(loginRequest == null || StringUtils.isEmpty(loginRequest.getPassword())){
            ExceptionCast.cast(ErrorEnum.PASSWORD_IS_NULL);
        }
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        AuthToken authToken =  authService.login(username,password,clientId,clientSecret);
        String access_token = authToken.getAccess_token();
        return Result.ok().put("token",access_token);
    }

    @GetMapping("/info")
    public Result info(){
        return Result.ok().put("user",getJwtUser(Jwtdecode()));
    }

    @GetMapping("/nav")
    public Result nav(){
        Map<String, Object> jwtClaims=Jwtdecode();
        List<SysMenu> menuList=userClient.selectByUid(getJwtUser(jwtClaims).getUid());
        List<String> permissions= (List<String>) jwtClaims.get("authorities");
        return Result.ok().put("menuList",menuList).put("permissions",permissions);
    }

    @GetMapping("/jwtuser")
    public JwtUser jwtuser(){
        JwtUser user=getJwtUser(Jwtdecode());
        return user;
    }



    //退出s
    @PostMapping("/userlogout")
    public Result logout() {
        //取出cookie中的用户身份令牌
        String token = getTokenFormCookie();
        //删除redis中的token
        boolean result = authService.delToken(token);
        //清除cookie
        this.clearCookie(token);
        return Result.ok();
    }

    public Map<String,Object> Jwtdecode(){
        Jwt decode = JwtHelper.decode(userjwt());
        Map<String, Object> jwtClaims = JSON.parseObject(decode.getClaims(), Map.class);
        return jwtClaims;
    }

    public JwtUser getJwtUser(Map<String,Object> jwtClaims){
        if(jwtClaims == null){
            return null;
        }
        JSONObject jsonObject = (JSONObject)jwtClaims.get("user_name");
        return jsonObject.toJavaObject(JwtUser.class);
    }


    public String userjwt() {
        String token = getTokenFormCookie();
        if(token == null){
            ExceptionCast.cast(ErrorEnum.TOKEN_ERROR);
        }
        AuthToken userToken = authService.getUserToken(token);
        if(userToken!=null){
            //将jwt令牌返回给用户
            String jwt_token = userToken.getJwt_token();
            return jwt_token;
        }
        return null;
    }

    private String getTokenFormCookie(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String> map = CookieUtil.readCookie(request, "token");
        if(map!=null && map.get("token")!=null){
            String token = map.get("token");
            return token;
        } else {
            String token=request.getHeader("token");
            if (token!=null) return token;
        }
        return null;
    }


    private void clearCookie(String token){
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        CookieUtil.addCookie(response,cookieDomain,"/","token",token,0,false);
    }
}