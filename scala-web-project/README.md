DATABASE management:
`sudo docker run -P -e USER=local -e PASSWORD=local -e ENCODING=UTF8 jamesbrink/postgres` - our database

```
CREATE DATABASE "dblun";
\l - show databases
\q - quit
postgres@f2ecc14babd0:~$ psql dblun  - login to database dblun

```
Parsing of `*.csv` files in `../schemas.txt`

Run application:
`activator run` - development mode(localhost:9000)