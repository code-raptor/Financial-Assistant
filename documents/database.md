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

Table egyseg_tulajdonsag{
  egyseg_tulajdonsag_id int PK
  name varchar(50)
  type varchar(20)
}

Table nyugta {
  nyugta_id int PK
  termek_id int 
  quantity int
  price int
  nyugta_date date
  egyseg_tulajdonsag_id int
  egyseg_tulajdonsag_value int
}

Table bevetel_tulajdonsag {
  bevetel_tulajdonsag_id int PK
  name varchar(50)
  type varchar(20)
}

Table bevetel {
  bevetel_id int PK
  amount varchar
  bevetel_date date
  bevetel_tulajdonsag_id int
  bevetel_tulajdonsag_value varchar(100)
}

table kiadas_tulajdonsag { 
  kiadas_tulajdonsag_id int PK
  name varchar(50)
  type varchar(20)
}

table kiadas_tipusa { 
  kiadas_tipusa_id int PK
  name varchar(50)
  type varchar(20)
}

Table kiadas {
  kiadas_id int PK
  consumption double
  egyseg_tulajdonsag_id int
  amount int
  kiadas_date date
  kiadas_frequency int
  kiadas_tulajdonsag_id int
  kiadas_tulajdonsag_value varchar(100)
  kiadas_tipusa_id int
  kiadas_tipusa_value varchar(100)
}

Ref: termek_tulajdonsag.termek_tulajdonsag_id >  termek_kategoria.termek_tulajdonsag_id 

Ref:termek_kategoria.termek_kategoria_id > termek.termek_kategoria_id

Ref: termek.termek_id > nyugta.termek_id

Ref: egyseg_tulajdonsag.egyseg_tulajdonsag_id > nyugta.egyseg_tulajdonsag_id

Ref: bevetel_tulajdonsag.bevetel_tulajdonsag_id > bevetel.bevetel_tulajdonsag_id

Ref: kiadas_tulajdonsag.kiadas_tulajdonsag_id > kiadas.kiadas_tulajdonsag_id

Ref: kiadas_tipusa.kiadas_tipusa_id > kiadas.kiadas_tipusa_id
