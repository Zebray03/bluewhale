package com.seecoder.BlueWhale.po;

import com.seecoder.BlueWhale.vo.PhoneAddressCandidateVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;



/**
 * &#064;Author:  B Zhang
 * &#064;Date:  2024/6/13
 * 备选电话地址类，用于实现用户自定义收货地址功能需求
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "phone_address_candidate",indexes = {@Index(name = "phone_address_candidate_user_id_index", columnList = "user_id")})
public class PhoneAddressCandidate {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "user_id")
    private Integer userId;

    @Basic
    @Column(name = "phone_candidate")
    private String phoneCandidate;

    @Basic
    @Column(name = "address_candidate")
    private String addressCandidate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

    public PhoneAddressCandidateVO toVO() {
        PhoneAddressCandidateVO phoneAddressCandidateVO = new PhoneAddressCandidateVO();
        phoneAddressCandidateVO.setUserId(this.userId);
        phoneAddressCandidateVO.setPhoneCandidate(this.phoneCandidate);
        phoneAddressCandidateVO.setAddressCandidate(this.addressCandidate);
        return phoneAddressCandidateVO;
    }
}
