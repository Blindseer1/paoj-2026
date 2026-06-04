DROP TABLE IF EXISTS tranzactii;
DROP TABLE IF EXISTS carduri;
DROP TABLE IF EXISTS conturi;
DROP TABLE IF EXISTS utilizatori;

-- ------------------------------------------------------------
CREATE TABLE utilizatori (
    id      VARCHAR(10)  PRIMARY KEY,          
    nume    VARCHAR(100) NOT NULL,
    email   VARCHAR(150) NOT NULL UNIQUE,
    cnp     VARCHAR(13)  NOT NULL UNIQUE
);

CREATE TABLE conturi (
    iban             VARCHAR(34)    PRIMARY KEY,
    tip              ENUM('CURENT','ECONOMII') NOT NULL,
    sold             DECIMAL(15,2)  NOT NULL DEFAULT 0.00,
    titular          VARCHAR(100)   NOT NULL,
    limata_overdraft DECIMAL(15,2),
    rata_dobanda     DECIMAL(5,2),
    sold_minim       DECIMAL(15,2),
    -- FK
    utilizator_id    VARCHAR(10)    NOT NULL,
    FOREIGN KEY (utilizator_id) REFERENCES utilizatori(id) ON DELETE CASCADE
);

CREATE TABLE carduri (
    numar_card      VARCHAR(20)  PRIMARY KEY,
    tip             ENUM('DEBIT','CREDIT') NOT NULL,
    cvv             CHAR(3)      NOT NULL,
    data_expirare   DATE         NOT NULL,
    titular_card    VARCHAR(100) NOT NULL,
    activ           BOOLEAN      NOT NULL DEFAULT TRUE,
    limita_zilnica  DECIMAL(15,2),
    limita_credit   DECIMAL(15,2),
    sold_utilizat   DECIMAL(15,2),
    cont_iban       VARCHAR(34)  NOT NULL,
    FOREIGN KEY (cont_iban) REFERENCES conturi(iban) ON DELETE CASCADE
);

-- ------------------------------------------------------------
CREATE TABLE tranzactii (
    id              VARCHAR(20)  PRIMARY KEY,   -- "TRX000001"
    tip             ENUM('DEPUNERE','RETRAGERE','TRANSFER') NOT NULL,
    suma            DECIMAL(15,2) NOT NULL,
    descriere       VARCHAR(255),
    data_ora        DATETIME     NOT NULL,
    iban_sursa      VARCHAR(34),   -- FIXED: Removed NOT NULL to allow DEPUNERE
    iban_destinatie VARCHAR(34),                
    FOREIGN KEY (iban_sursa)      REFERENCES conturi(iban) ON DELETE CASCADE, -- FIXED: Added cascade
    FOREIGN KEY (iban_destinatie) REFERENCES conturi(iban) ON DELETE CASCADE  -- FIXED: Added cascade
);
