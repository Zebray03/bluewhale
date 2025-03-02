package com.seecoder.BlueWhale.vo;

import com.seecoder.BlueWhale.po.PhoneAddressCandidate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class PhoneAddressCandidateVO {

    private Integer userId;

    private String phoneCandidate;

    private String addressCandidate;

    public PhoneAddressCandidate toPO() {
        PhoneAddressCandidate phoneAddressCandidate = new PhoneAddressCandidate();
        phoneAddressCandidate.setUserId(this.userId);
        phoneAddressCandidate.setPhoneCandidate(this.getPhoneCandidate());
        phoneAddressCandidate.setAddressCandidate(this.getAddressCandidate());
        return phoneAddressCandidate;
    }
}
