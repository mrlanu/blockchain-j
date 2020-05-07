package io.lanu.blockchain;

import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Transaction {
    private String sender;
    private String recipient;
    private int amount;
    private String signature;

    public String getHash() {
        return DigestUtils.sha256Hex(sender + recipient + amount);
    }
}
