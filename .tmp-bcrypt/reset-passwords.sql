UPDATE users SET password='$2a$10$N6l9oiUuppJPIxr4m656zeUQSEdbFFi8Nrfzhk35tlu2ntBp2j2UW' WHERE username='admin';
UPDATE users SET password='$2a$10$LC/1x1cTFIuWAagT0YFIyO9Gueism32REKTMBuILEpqOPK6HhN83a' WHERE username='staff';
SELECT username, password FROM users ORDER BY id;
