
ALTER TABLE students
DROP
COLUMN category;

ALTER TABLE students
    ADD category VARCHAR(20) NOT NULL;

ALTER TABLE lease_agreements
DROP
COLUMN semester;

ALTER TABLE lease_agreements
DROP
COLUMN status;

ALTER TABLE lease_agreements
    ADD semester VARCHAR(10) NULL;

ALTER TABLE lease_agreements
    ADD status VARCHAR(20) NULL;