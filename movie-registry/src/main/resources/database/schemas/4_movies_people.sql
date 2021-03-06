CREATE TABLE MOVIES_PEOPLE
(
  ID BIGINT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  MOVIE_ID BIGINT NOT NULL,
  PERSON_ID BIGINT NOT NULL,
  CONSTRAINT MOVIES_PEOPLE_MOVIES_ID_FK FOREIGN KEY (MOVIE_ID) REFERENCES MOVIES (ID) ON DELETE CASCADE,
  CONSTRAINT MOVIES_PEOPLE_PEOPLE_ID_FK FOREIGN KEY (PERSON_ID) REFERENCES PEOPLE (ID) ON DELETE CASCADE
)