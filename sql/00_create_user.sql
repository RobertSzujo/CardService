DROP USER CARD;
DROP TABLESPACE card_tablespace INCLUDING CONTENTS AND DATAFILES;
DROP TABLESPACE card_tablespace_temp INCLUDING CONTENTS AND DATAFILES;

CREATE TABLESPACE card_tablespace
datafile 'card_tablespace.dat'
size 10M autoextend on;

CREATE TEMPORARY TABLESPACE card_tablespace_temp
tempfile 'card_tablespace_temp.dat'
size 5M autoextend on;

CREATE USER CARD IDENTIFIED BY dbadmin
DEFAULT TABLESPACE card_tablespace
TEMPORARY TABLESPACE card_tablespace_temp;
GRANT CREATE SESSION TO CARD;
GRANT CREATE TABLE TO CARD;
GRANT UNLIMITED TABLESPACE TO CARD;