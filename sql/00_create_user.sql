DROP USER carddb;

CREATE TABLESPACE carddb_tabspace
datafile 'carddb_tabspace.dat'
size 10M autoextend on;

CREATE TEMPORARY TABLESPACE carddb_tabspace_temp
tempfile 'carddb_tabspace_temp.dat'
size 5M autoextend on;

CREATE USER carddb IDENTIFIED BY dbadmin
DEFAULT TABLESPACE carddb_tabspace
TEMPORARY TABLESPACE carddb_tabspace_temp;
GRANT CREATE SESSION TO carddb;
GRANT CREATE TABLE TO carddb;
GRANT UNLIMITED TABLESPACE TO carddb;