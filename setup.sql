/** Elimina l'utente 'map' se esiste gia' **/
DROP USER IF EXISTS MapUser;

/** Crea l'utente 'map' con password 'map' **/
CREATE USER 'MapUser' IDENTIFIED BY 'map';

/** Crea il database mapDB se non esiste gia' **/
CREATE DATABASE IF NOT EXISTS MapDB;

USE MapDB;

/** Da i privilegi all'utente 'map' **/
GRANT ALL PRIVILEGES ON MapDB.* TO 'MapUser'@'%';

/** Crea la tabella 'exampleTab' **/
DROP TABLE IF EXISTS exampleTab;

CREATE TABLE mapdb.exampleTab( 
	X1 float, 
	X2 float, 
	X3 float 
); 

/** Inserisce i valori nella tabella **/
INSERT INTO mapdb.exampleTab VALUES(1,2,0); 
INSERT INTO mapdb.exampleTab VALUES(0,1,-1); 
INSERT INTO mapdb.exampleTab VALUES(1,3,5); 
INSERT INTO mapdb.exampleTab VALUES(1,3,4); 
INSERT INTO mapdb.exampleTab VALUES(2,2,0); 

commit;
