package com.gfg.ewallet69.user.service.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WalletResponse {

    private Long userId;
    private Long walletId;
    private Double balance;
}
