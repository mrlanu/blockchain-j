package io.lanu.blockchain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Block<E> implements Serializable {

    private static final long serialVersionUID = 9L;
    private long id;
    public String hash;
    public String previousHash;
    private List<E> dataList;
    private long timeStamp;
    private int nonce;

    public Block(long id, String previousHash, List<E> dataList, int nonce) {
        this.id = id;
        this.previousHash = previousHash;
        this.dataList = dataList;
        this.timeStamp = new Date().getTime();
        this.nonce = nonce;
        this.hash = null;
    }
}
