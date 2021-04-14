DROP USER CARD;

CREATE TABLESPACE carddb_tabspace
datafile 'card_tabspace.dat'
size 10M autoextend on;

CREATE TEMPORARY TABLESPACE carddb_tabspace_temp
tempfile 'card_tabspace_temp.dat'
size 5M autoextend on;

CREATE USER CARD IDENTIFIED BY dbadmin
DEFAULT TABLESPACE card_tabspace
TEMPORARY TABLESPACE card_tabspace_temp;
GRANT CREATE SESSION TO card;
GRANT CREATE TABLE TO card;
GRANT UNLIMITED TABLESPACE TO card;