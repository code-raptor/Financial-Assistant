//https://dbdiagram.io/home

Table Termek {
  termekID int PK
  termekNev varchar(100)
  termekJotallas bit
  termekKaros bit
}

Table Egyseg {
  egysegID int PK
  egysegNev varchar(50)
}
//lebeg
Table Kategoria {
  kategoriaID int PK
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

Ref: Jotallas.termekID > Termek.termekID

Ref: Jotallas.egysegID > Egyseg.egysegID

Ref: RendszeresKiad_Kapcs.rendszeresKiadID > RendszeresKiad.rendszeresKiadID

Ref: Bevetel_Kapcs.bevetelID > Bevetel.bevetelID

Ref: Nyugta.termekNyugta_KapcsID > TermekNyugta_Kapcs.termekNyugta_KapcsID

Ref: TermekNyugta_Kapcs.termekID > Termek.termekID

Ref: TermekNyugta_Kapcs.egysegID > Egyseg.egysegID

Ref: Bevetel.kategoriaID > Kategoria.kategoriaID

Ref: RendszeresKiad.kategoriaID > Kategoria.kategoriaID
