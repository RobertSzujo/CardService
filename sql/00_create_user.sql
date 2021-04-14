DROP USER CARD;

CREATE TABLESPACE card_tabspace
datafile 'card_tabspace.dat'
size 10M autoextend on;

CREATE TEMPORARY TABLESPACE card_tabspace_temp
tempfile 'card_tabspace_temp.dat'
size 5M autoextend on;

CREATE USER CARD IDENTIFIED BY dbadmin
DEFAULT TABLESPACE card_tabspace
TEMPORARY TABLESPACE card_tabspace_temp;
GRANT CREATE SESSION TO CARD;
GRANT CREATE TABLE TO CARD;
GRANT UNLIMITED TABLESPACE TO CARD;