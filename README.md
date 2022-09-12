## Client Docs
#### Install dependencies
Should be yarn version 3.2.0
```
$ yarn install
```

#### Running local dev server
```
$ yarn dev
```

### Build Setup

```
$ yarn build
```

Build should be outputted to `dist/` folder

After that we can check production version with `yarn preview`

### Main Tech stack

* React
* React-router
* React-Context
* Vite.js
* Typescript
* Ant Design
* In plans (react-query) for client browser caching


## Server Docs

### To build and run:
- install JDK
- install maven
- Postgre

### Build
- create empty DB name - velox
- in /src/main/resources/application.properties set password for DB
- run 'mvn package'

### Run
- navigate to /target
- run 'java - jar *.jar'

### Check http://localhost:8080/swagger-ui.html