package com.gfg.ewallet69.user.feingclient;

import com.gfg.ewallet69.user.service.resource.TransactionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="TRANSACTION")
public interface TransactionClient {

    @PostMapping("/transaction/{user-id}")
    public ResponseEntity<Boolean> transaction(@PathVariable("user-id") Long userId, @RequestBody TransactionRequest transactionRequest);

}
