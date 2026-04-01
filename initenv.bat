@echo off
echo Setting gcloud project...
call gcloud config set project first-web-application-489212

echo Setting DATASTORE_USE_PROJECT_ID_AS_APP_ID...
set DATASTORE_USE_PROJECT_ID_AS_APP_ID=true

echo Initializing Datastore emulator environment variables...
for /f "tokens=*" %%i in ('gcloud beta emulators datastore env-init 2^>^&1') do (
    %%i
)

echo.
echo Environment variables set:
echo   DATASTORE_USE_PROJECT_ID_AS_APP_ID=%DATASTORE_USE_PROJECT_ID_AS_APP_ID%
echo   DATASTORE_DATASET=%DATASTORE_DATASET%
echo   DATASTORE_EMULATOR_HOST=%DATASTORE_EMULATOR_HOST%
echo   DATASTORE_EMULATOR_HOST_PATH=%DATASTORE_EMULATOR_HOST_PATH%
echo   DATASTORE_HOST=%DATASTORE_HOST%
echo   DATASTORE_PROJECT_ID=%DATASTORE_PROJECT_ID%

echo.
echo Running Maven build and AppEngine...
mvn clean package appengine:run