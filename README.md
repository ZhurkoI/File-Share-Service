# File-Share-Service

## Overview

The 'File-Share-Service' is a REST-full application that can do the following:

* creating/editing/deleting users
* uploading files to the server filesystem
* providing a statistic of uploads

## Requirements

The 'File-Share-Service' app is creating for deploying on Heroku. Supported type of the database is MySQL.

## Deploy

### Prerequisites
1. Application must be created in Heroku
2. The MyQSL add-on must be added to the Heroku application (e.g. `JawsDB MySQL` add-on)
3. Integration with GitHub is configured

### Deploy via Heroku UI
1. Open application settings (created according to preconditions).
2. On the 'Deploy' tab select the branch of the application in GitHub and click 'Deploy' button

### Local deployment (for debug purposes)
To run application locally it is necessary to add the `.env` file in to the project directory. 
The `.env` file must contain the `JDBC_DATABASE_URL` environment variable to configure access to the database:

`JDBC_DATABASE_URL=jdbc:mysql://{SQL_server_address}:{port}/{DB_name}?password={DB_password}&user={DB_username}`

To deploy application locally run in terminal: 

`$ mvn clean install`   // to download all dependencies and compile application

`$ heroku local release`   // to run migrations in the database. 
The 'release' option executes the `release` process type from Procfile

`$ heroku local`    // deploy application locally. It will run on port 5000