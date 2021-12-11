package com.iglens.爬虫.基金;

import lombok.Data;

@Data
public class T003ProductInfo {

  private String bankName;
  private String prdId;
  private String prdName;
  private String incomeRate;
  private String investDuration;
  private String durationUnit;
  private String floor;
  private String totalAmount;
  private String leftAmount;
  private String prdType;
  private String financingType;
  private String startDay;
  private String endDay;
  private String activeDay;
  private String overdueDay;

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public void setPrdId(String prdId) {
    this.prdId = prdId;
  }

  public String getPrdId() {
    return prdId;
  }

  public void setPrdName(String prdName) {
    this.prdName = prdName;
  }

  public String getPrdName() {
    return prdName;
  }

  public void setIncomeRate(String incomeRate) {

    this.incomeRate = incomeRate;
  }

  public String getIncomeRate() {
    return incomeRate;
  }

  public void setInvestDuration(String investDuration) {
    this.investDuration = investDuration;
  }

  public String getInvestDuration() {
    return investDuration;
  }

  public void setDurationUnit(String durationUnit) {
    this.durationUnit = durationUnit;
  }

  public String getDurationUnit() {
    return durationUnit;
  }

  public void setFloor(String floor) {
    this.floor = floor;
  }

  public String getFloor() {
    return floor;
  }

  public void setTotalAmount(String totalAmount) {
    this.totalAmount = totalAmount;
  }

  public String getTotalAmount() {
    return totalAmount;
  }

  public void setLeftAmount(String leftAmount) {
    this.leftAmount = leftAmount;
  }

  public String getLeftAmount() {
    return leftAmount;
  }

  public void setPrdType(String prdType) {
    this.prdType = prdType;
  }

  public String getPrdType() {
    return prdType;
  }

  public void setFinancingType(String financingType) {
    this.financingType = financingType;
  }

  public String getFinancingType() {
    return financingType;
  }

  public void setStartDay(String startDay) {
    this.startDay = startDay;
  }

  public String getStartDay() {
    return startDay;
  }

  public void setEndDay(String endDay) {
    this.endDay = endDay;
  }

  public String getEndDay() {
    return endDay;
  }

  public void setActiveDay(String activeDay) {
    this.activeDay = activeDay;
  }

  public String getActiveDay() {
    return activeDay;
  }

  public void setOverdueDay(String overdueDay) {
    this.overdueDay = overdueDay;
  }

  public String getOverdueDay() {
    return overdueDay;
  }
}
