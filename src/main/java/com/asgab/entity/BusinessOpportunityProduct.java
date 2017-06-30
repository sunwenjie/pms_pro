package com.asgab.entity;

import java.math.BigDecimal;
import java.util.Date;

public class BusinessOpportunityProduct {
  private Long id;
  private Long business_opportunity_id;
  private Long product_id;
  private Long product_category_id;
  private String sale_mode;
  private BigDecimal budget;
  private String budget_format;

  private Date deleted_at;
  private Date created_at;
  private Date updated_at;

  private Product product;
  private ProductCategory productCategory;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getBusiness_opportunity_id() {
    return business_opportunity_id;
  }

  public void setBusiness_opportunity_id(Long business_opportunity_id) {
    this.business_opportunity_id = business_opportunity_id;
  }

  public Long getProduct_id() {
    return product_id;
  }

  public void setProduct_id(Long product_id) {
    this.product_id = product_id;
  }

  public Long getProduct_category_id() {
    return product_category_id;
  }

  public void setProduct_category_id(Long product_category_id) {
    this.product_category_id = product_category_id;
  }

  public String getSale_mode() {
    return sale_mode;
  }

  public void setSale_mode(String sale_mode) {
    this.sale_mode = sale_mode;
  }

  public BigDecimal getBudget() {
    return budget;
  }

  public void setBudget(BigDecimal budget) {
    this.budget = budget;
  }

  public Date getDeleted_at() {
    return deleted_at;
  }

  public void setDeleted_at(Date deleted_at) {
    this.deleted_at = deleted_at;
  }

  public Date getCreated_at() {
    return created_at;
  }

  public void setCreated_at(Date created_at) {
    this.created_at = created_at;
  }

  public Date getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(Date updated_at) {
    this.updated_at = updated_at;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public ProductCategory getProductCategory() {
    return productCategory;
  }

  public void setProductCategory(ProductCategory productCategory) {
    this.productCategory = productCategory;
  }

  public String getBudget_format() {
    return budget_format;
  }

  public void setBudget_format(String budget_format) {
    this.budget_format = budget_format;
  }

  public String getDecodeProductCategoryId() {
    if (productCategory != null) {
      return productCategory.getValue();
    }
    return "";
  }

  public String getDecodeProductZH() {
    if (product != null) {
      return product.getName();
    }
    return "";
  }

  public String getDecodeProductEN() {
    if (product != null) {
      return product.getEn_name();
    }
    return "";
  }
}
