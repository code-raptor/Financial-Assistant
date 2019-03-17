Table termek_tulajdonsag {
  termek_tulajdonsag_id int PK
  name varchar(100)
  type varchar(20)
}

Table termek_kategoria {
  termek_kategoria_id int PK
  name varchar(100)
  termek_tulajdonsag_id int
  termek_tulajdonsag_value varchar(100)
}

Table termek { 
 termek_id int PK 
 name varchar(100)
 termek_kategoria_id int
 
}

Table nyugta {
  nyugta_id int 
  termek_id int 
  //nyugta_id + termek_id PK
  price int
  quantity int
  egyseg varchar(25)
  
  nyugta_date date
  egyseg_tulajdonsag_id int
  egyseg_tulajdonsag_value int
}

Table transaction {
  id int PK
  amount int 
  date date
  name varchar 
  frequency varchar
}

Table shopping_list { 
 id int PK 
 termek_id int
 quantity int
 
}

Ref: termek_tulajdonsag.termek_tulajdonsag_id >  termek_kategoria.termek_tulajdonsag_id 

Ref:termek_kategoria.termek_kategoria_id > termek.termek_kategoria_id

Ref: termek.termek_id > nyugta.termek_id

Ref: shopping_list.termek_id > termek.termek_id
