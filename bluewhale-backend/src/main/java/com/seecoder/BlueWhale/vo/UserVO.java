package com.seecoder.BlueWhale.vo;

import com.seecoder.BlueWhale.enums.RoleEnum;
import com.seecoder.BlueWhale.po.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
public class UserVO {

    private Integer id;

    private String name;

    @NotBlank(message = "电话不为空")
    @Pattern(regexp = "^1(3[0-9]|4[579]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[189])\\d{8}$", message = "请输入正确的电话")
    private String phone;

    @NotBlank(message = "密码不为空")
    private String password;

    private Integer storeId;

    @NotNull(message = "身份不为空")
    private RoleEnum role;

    private Date createTime;

    private String storeName;

    public User toPO() {
        User user = new User();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setId(this.id);
        user.setName(this.name);
        user.setPhone(this.phone);
        user.setRole(this.role);
        user.setStoreId(this.storeId);
        user.setPassword(passwordEncoder.encode(this.password)); // 对password字段加密，将密文存入数据库
        user.setCreateTime(this.createTime);
        return user;
    }
}
