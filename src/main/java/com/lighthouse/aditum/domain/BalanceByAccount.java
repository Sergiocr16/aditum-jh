package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A BalanceByAccount.
 */
@Entity
@Table(name = "balance_by_account")
public class BalanceByAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_date")
    private ZonedDateTime date;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "balance")
    private String balance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public BalanceByAccount date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Long getAccountId() {
        return accountId;
    }

    public BalanceByAccount accountId(Long accountId) {
        this.accountId = accountId;
        return this;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getBalance() {
        return balance;
    }

    public BalanceByAccount balance(String balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BalanceByAccount balanceByAccount = (BalanceByAccount) o;
        if (balanceByAccount.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), balanceByAccount.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BalanceByAccount{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", accountId='" + getAccountId() + "'" +
            ", balance='" + getBalance() + "'" +
            "}";
    }
}
