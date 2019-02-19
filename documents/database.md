Table termek_tulajdonsag {
  termek_tulajdonsag_id int PK
  name varchar(100)
  type varchar(20)
}

Table termek { 
 termek_id int PK 
 name varchar(100)
 termek_tulajdonsag_id int
 termek_tulajdonsag_value varchar(100)
}

Table egyseg_tulajdonsag{
  egyseg_tulajdonsag_id int PK
  name varchar(50)
  type varchar(20)
}

// ?
Table jotallas {
  jotallas_id int PK
  termek_id int 
  expires_date date
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

//kiadas neve (víz,gaz, spotify,teljesítmény ado, hitel)
table kiadas_tulajdonsag { 
  kiadas_tulajdonsag_id int PK
  name varchar(50)
  type varchar(20)
}

// rendszeres kiadas 
Table kiadas {
  kiadas_id int PK
  consumption double //fogyaztás vagy mennyiség pl.: 958.5 köbméter gáz
  //? köbméter, liter, mj
  egyseg_tulajdonsag_id int
  amount int
  kiadas_date date
  kiadas_frequency int
  kiadas_tulajdonsag_id int
  kiadas_tulajdonsag_value varchar(100)
}



Ref: termek_tulajdonsag.termek_tulajdonsag_id >  termek.termek_tulajdonsag_id 

Ref: termek.termek_id > nyugta.termek_id

Ref: egyseg_tulajdonsag.egyseg_tulajdonsag_id > nyugta.egyseg_tulajdonsag_id

Ref: jotallas.termek_id > nyugta.termek_id

Ref: bevetel_tulajdonsag.bevetel_tulajdonsag_id > bevetel.bevetel_tulajdonsag_id

Ref: kiadas_tulajdonsag.kiadas_tulajdonsag_id > kiadas.kiadas_tulajdonsag_id
