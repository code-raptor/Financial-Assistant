Table product_property {
  product_property_id int PK
  name varchar(100)
  type varchar(20)
}

Table product_category {
  product_category_id int PK
  name varchar(100)
  product_property_id int
  product_property_value varchar(100)
}

Table product { 
 product_id int PK 
 name varchar(100)
 product_category_id int
 
}

Table receipt {
  receipt_id int 
  product_id int 
  price int
  quantity int
  unit varchar(25)
  receipt_date date
}

Table transactions {
  id int PK
  amount int 
  date date
  name varchar 
  frequency varchar
}

Table shopping_list { 
 id int PK 
 product_id int
 quantity int
}

Ref: product_property.product_property_id >  product_category.product_property_id 

Ref:product_category.product_category_id > product.product_category_id

Ref: product.product_id > receipt.product_id

Ref: shopping_list.product_id > product.product_id