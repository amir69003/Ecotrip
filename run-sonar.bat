@echo off

set SONAR_TOKEN=squ_1da86c73737910a2fbd7b72155e117f56a440a6c

D:\sonarqube\sonar-scanner-7.1.0.4889-windows-x64\bin\sonar-scanner.bat ^
  -Dsonar.projectKey=EcoTrip ^
  -Dsonar.projectName=EcoTrip ^
  -Dsonar.host.url=https://sonar.info.univ-lyon1.fr ^
  -Dsonar.sources=backend/src/main/java ^
  -Dsonar.tests=backend/src/test/java ^
  -Dsonar.java.binaries=backend/target/classes ^
  -Dsonar.junit.reportPaths=backend/target/surefire-reports ^
  -Dsonar.jacoco.reportPaths=backend/target/jacoco.exec ^
  -Dsonar.coverage.jacoco.xmlReportPaths=backend/target/site/jacoco/jacoco.xml ^
  -Dsonar.token=%SONAR_TOKEN%
