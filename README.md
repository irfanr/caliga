# Caliga

Caliga is an attempt for a simple yet robust application covering mostly basic business function.

### Prerequisite
1. Git 
2. PostgreSQL (OPTIONAL: PG Admin III)
3. JDK 8
4. Apache Maven
5. OPTIONAL: Spring Tool Suite / Eclipse with STS Plugin

### Installation
#### 1. Install Database
Create Login Role with following attributes:
* Username : __caliga__
* Password :  __caliga__

After that, create also a database with the same name:  __caliga__

You can use command-line for starter, but it is highly recommended to use Eclipse / STS as your IDE.
* #### 2.1 Command Line
Clone the project:
```sh
$ git clone https://github.com/irfanr/caliga.git
```
Go to your project directory and type:
```sh
$ mvn spring-boot:run
```
* #### 2.2 Eclipse / STS
