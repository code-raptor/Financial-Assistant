Table product_property {
  id int PK
  name varchar(100)
  type varchar(20)
}

Table product_category {
  id int PK
  name varchar(100)
  product_property_id int
  product_property_value varchar(100)
}

Table product {
 id int PK 
 name varchar(100)
 unit varchar(25)
 quantity int
 unitPrice int
 product_category_id int
}

Table receipt {
  id int 
  date date
  amount int
  product_id int 
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
 name varchar(40)
 quantity int
 product_id int
}

Ref: product_property.id >  product_category.product_property_id 

Ref: product_category.id > product.product_category_id

Ref: product.id > receipt.product_id

Ref: shopping_list.product_id > product.id