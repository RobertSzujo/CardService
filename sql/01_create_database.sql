DROP TABLE cards;
DROP TABLE owners;
DROP TABLE contacts;
DROP TABLE card_types

CREATE TABLE cards
( card_number DECIMAL(16,0) NOT NULL PRIMARY KEY,
  valid_thru DECIMAL(4,0),
  card_hash VARCHAR2(128),
  card_type_id int,
  owner_id int,
  disabled CHAR(1) DEFAULT 'N' );
  
CREATE TABLE owners
( owner_id int NOT NULL PRIMARY KEY,
  owner VARCHAR2(128) );

CREATE TABLE contacts
( contact_id int NOT NULL PRIMARY KEY,
  owner_id int NOT NULL,
  contact_type CHAR(1) NOT NULL,
  contact VARCHAR2(128) );
  
CREATE TABLE card_types
( card_type_id int NOT NULL PRIMARY KEY,
  card_type VARCHAR2(128) );