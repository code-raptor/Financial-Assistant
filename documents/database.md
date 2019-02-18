Table Termek {
  termekID int PK
  termekNev varchar(100)
  termekJotallas bit
  termekKaros bit
  TermekTulajdonsagID int
}

Table EgysegTulajdonsag{
  EgysegTulajdonsagID int PK
  egysegTulajdonsagNev varchar(50)
}

Table KategoriaTulajdonsag {
  KategoriaTulajdonsagID int PK
  KategoriaNev varchar(100)
}

Table Jotallas {
  jotallasID int PK
  termekID int 
  egysegID int 
  jotallasLejarat date
}

Table RendszeresKiad {
  rendszeresKiadID int PK
  kategoriaID int 
  RendszeresKiadNev varchar
}

Table Bevetel {
  bevetelID int PK
  kategoriaID int
  bevetelNev varchar
}

Table RendszeresKiad_Kapcs {
  rendszeresKiad_KapcsID int PK
  rendszeresKiadID int 
  rendszeresKiadDatum date
}

Table Bevetel_Kapcs {
  bevetel_KapcsID int PK
  bevetelID int 
  bevetelDatum date
}

Table Nyugta{
  nyugtaID int PK
  termekNyugta_KapcsID int
  nyugtaDatum date
}

Table TermekNyugta_Kapcs {
  termekNyugta_KapcsID int
  termekID int 
  mennyiseg int
  egysegID int 
  ar int
}

Table TermekTulajdonsag {
  TermekTulajdonsagID int PK
  name varchar(100)
}

Table TermekFlex {
  termekID int
  TermekTulajdonsagID int PK
  value varchar(100)
}

Table EgysegFlex {
  termekNyugta_KapcsID int
  egysegTulajdonsagID int
  jotallasID int
  value int
}

Table KategoriaFlex {
  KategoriaTulajdonsagID int
  bevetelID int
  rendszeresKiadID int
  value int
}

Ref: Jotallas.termekID > Termek.termekID

Ref: EgysegTulajdonsag.EgysegTulajdonsagID > EgysegFlex.egysegTulajdonsagID

Ref: RendszeresKiad_Kapcs.rendszeresKiadID > RendszeresKiad.rendszeresKiadID

Ref: Bevetel_Kapcs.bevetelID > Bevetel.bevetelID

Ref: Nyugta.termekNyugta_KapcsID > TermekNyugta_Kapcs.termekNyugta_KapcsID

Ref: TermekNyugta_Kapcs.termekID > Termek.termekID

Ref: Bevetel.bevetelID > KategoriaFlex.bevetelID

Ref: RendszeresKiad.rendszeresKiadID > KategoriaFlex.rendszeresKiadID

Ref: TermekTulajdonsag.TermekTulajdonsagID > TermekFlex.TermekTulajdonsagID

Ref: TermekFlex.termekID > Termek.termekID

Ref: EgysegFlex.termekNyugta_KapcsID > TermekNyugta_Kapcs.termekNyugta_KapcsID

Ref: EgysegFlex.jotallasID > Jotallas.jotallasID

Ref: KategoriaTulajdonsag.KategoriaTulajdonsagID > KategoriaFlex.KategoriaTulajdonsagID
