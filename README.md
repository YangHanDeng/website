# Installation
1. Java version: 
    - java version "19.0.1" 2022-10-18
2. Maven:
    - Apache Maven 4.0.0-alpha-4
    - what I've done:
        1. install Apache Maven 4.0.0-alpha-4 from official website
        2. add maven's binary folder to system path variable (Windows)
3. Install IntelliJ
    - be sure to link SDK: `File` on top left corner > `Project Structure` > `Project` > `SDK`
4. Database
    - `src/main/java/resources/application.properties` contains configurations to connect to PostGreSQL
    - you can change this file and choose whatever database you want
    - `spring.datasource.url` defines the database name, `spring.datasource.username` and `spring.datasource.password` should be changed based on your postgres configuration.
    - for the data in team/ team_stats, we can import them from `team_csv_file/Team_Rank.csv` and `team_csv_file/18-21_DB_Test.csv`

