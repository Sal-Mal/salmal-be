package com.salmalteam.salmal.member.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Setting {

    @Column(name = "marketing_infomation_consent")
    private Boolean marketingInformationConsent;

    private Setting(final Boolean marketingInformationConsent){
        this.marketingInformationConsent = marketingInformationConsent;
    }

    public static Setting of(final Boolean marketingInformationConsent){
        return new Setting(marketingInformationConsent);
    }
}
