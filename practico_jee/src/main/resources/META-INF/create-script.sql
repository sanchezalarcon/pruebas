CREATE DATABASE tsipractico WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'Spanish_Spain.1252' LC_CTYPE = 'Spanish_Spain.1252';

ALTER DATABASE tsipractico OWNER TO postgres;

\connect tsipractico

CREATE TABLE role (
    id integer NOT NULL,
    description character varying(1024),
    name character varying(256)
);

ALTER TABLE ONLY role ADD CONSTRAINT "Role_pkey" PRIMARY KEY (id);

INSERT INTO role VALUES (1, 'Administrador', 'ADMIN');
INSERT INTO role VALUES (2, 'Usuario', 'USER');

CREATE TABLE usuario (
    username character varying(16) NOT NULL,
    password character varying(16),
    name character varying(128),
    lastname character varying(128),
    email character varying(128),
    creationdate date,
    roleid integer
);

ALTER TABLE ONLY usuario ADD CONSTRAINT "User_pkey" PRIMARY KEY (username);
	
ALTER TABLE ONLY usuario ADD CONSTRAINT roleid_fk FOREIGN KEY (roleid) REFERENCES role(id);
	
INSERT INTO usuario VALUES ('gsanchez', 'secreto1', 'Gabriel', 'Sánchez', 'g@s.com', '2017-09-17', 1);
