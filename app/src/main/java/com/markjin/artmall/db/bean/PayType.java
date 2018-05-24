package com.markjin.artmall.db.bean;

import com.markjin.artmall.db.annotion.DBTable;

/**
 * 支付类型
 */

@DBTable("tb_pay_type")
public class PayType {
    public String pay_id;
    public String pay_code;
    public String pay_name;
    public byte[] pay_icon;

}
