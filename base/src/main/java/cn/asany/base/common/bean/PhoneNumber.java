package cn.asany.base.common.bean;

import cn.asany.base.common.bean.enums.PhoneNumberStatus;

import javax.persistence.*;

@Embeddable
public class PhoneNumber {
    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private PhoneNumberStatus status;
    /**
     * 电话
     */
    @Column(name = "phone", nullable = false, length = 25)
    private String phone;

    public PhoneNumberStatus getStatus() {
        return status;
    }

    public void setStatus(PhoneNumberStatus status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
