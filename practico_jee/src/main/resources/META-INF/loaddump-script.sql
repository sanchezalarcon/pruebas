--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.5
-- Dumped by pg_dump version 9.6.5

-- Started on 2017-09-18 20:37:48

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2132 (class 1262 OID 16393)
-- Name: tsipractico; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE tsipractico WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'Spanish_Spain.1252' LC_CTYPE = 'Spanish_Spain.1252';


ALTER DATABASE tsipractico OWNER TO postgres;

\connect tsipractico

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2133 (class 1262 OID 16393)
-- Dependencies: 2132
-- Name: tsipractico; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON DATABASE tsipractico IS 'practico tsi2';


--
-- TOC entry 1 (class 3079 OID 12387)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2135 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 186 (class 1259 OID 16402)
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE role (
    id integer NOT NULL,
    description character varying(1024),
    name character varying(256)
);


ALTER TABLE role OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 16394)
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE usuario (
    username character varying(16) NOT NULL,
    password character varying(16),
    name character varying(128),
    lastname character varying(128),
    email character varying(128),
    creationdate date,
    roleid integer
);


ALTER TABLE usuario OWNER TO postgres;

--
-- TOC entry 2127 (class 0 OID 16402)
-- Dependencies: 186
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY role (id, description, name) FROM stdin;
2	Usuario	USER
1	Administrador	ADMIN
\.


--
-- TOC entry 2126 (class 0 OID 16394)
-- Dependencies: 185
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY usuario (username, password, name, lastname, email, creationdate, roleid) FROM stdin;
gsanchez	secreto1	Gabriel	Sánchez	g@s.com	2017-09-17	1
\.


--
-- TOC entry 2007 (class 2606 OID 16409)
-- Name: role Role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY role
    ADD CONSTRAINT "Role_pkey" PRIMARY KEY (id);


--
-- TOC entry 2005 (class 2606 OID 16417)
-- Name: usuario User_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT "User_pkey" PRIMARY KEY (username);


--
-- TOC entry 2008 (class 2606 OID 16410)
-- Name: usuario roleid_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT roleid_fk FOREIGN KEY (roleid) REFERENCES role(id);


-- Completed on 2017-09-18 20:37:48

--
-- PostgreSQL database dump complete
--

