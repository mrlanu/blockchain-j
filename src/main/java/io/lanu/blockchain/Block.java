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
public class Block implements Serializable {

    private static final long serialVersionUID = 7L;
    private long id;
    private long timeStamp; //as number of milliseconds since 1/1/1970.
    private String previousHash;
    private List<Transaction> transactionList;
    private String hash;
    private long magicNumber;


    public Block(long id, String previousHash, List<Transaction> transactionList) {
        this.id = id;
        this.previousHash = previousHash;
        this.transactionList = transactionList;
        this.timeStamp = new Date().getTime();
    }
}
