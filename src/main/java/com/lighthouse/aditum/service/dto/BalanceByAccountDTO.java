package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the BalanceByAccount entity.
 */
public class BalanceByAccountDTO implements Serializable {

    private Long id;

    private ZonedDateTime date;

    private Long accountId;

    private Integer balance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
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

        BalanceByAccountDTO balanceByAccountDTO = (BalanceByAccountDTO) o;
        if(balanceByAccountDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), balanceByAccountDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BalanceByAccountDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", accountId='" + getAccountId() + "'" +
            ", balance='" + getBalance() + "'" +
            "}";
    }
}
