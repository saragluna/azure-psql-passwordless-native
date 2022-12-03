This demo project shows how to connect Azure Database for PostgreSQL use Azure AD credentials instead of using passwords.

It also shows how this project can be built into a native executable. 

## Provision Azure Resources

### Set up environments

```bash
export AZ_RESOURCE_GROUP=database-workshop
export AZ_DATABASE_SERVER_NAME=<YOUR_DATABASE_SERVER_NAME>
export AZ_DATABASE_NAME=<YOUR_DATABASE_NAME>
export AZ_LOCATION=<YOUR_AZURE_REGION>
export AZ_LOCAL_IP_ADDRESS=<YOUR_LOCAL_IP_ADDRESS>
export CURRENT_USERNAME=$(az ad signed-in-user show --query userPrincipalName -o tsv)
export CURRENT_USER_OBJECTID=$(az ad signed-in-user show --query id -o tsv)
```
### Provision
1. Log in to Azure CLI
```shell
az login --scope https://graph.microsoft.com/.default
```
2. Create resource group
```shell
az group create \
    --name $AZ_RESOURCE_GROUP \
    --location $AZ_LOCATION \
    --output tsv
```
3. Create PostgreSQL
```shell
az postgres server create \
    --resource-group $AZ_RESOURCE_GROUP \
    --name $AZ_DATABASE_SERVER_NAME \
    --location $AZ_LOCATION \
    --sku-name B_Gen5_1 \
    --storage-size 5120 \
    --output tsv
```
4. Set the Azure AD admin to current login user:
```shell
az postgres server ad-admin create \
    --resource-group $AZ_RESOURCE_GROUP \
    --server-name $AZ_DATABASE_SERVER_NAME \
    --display-name $CURRENT_USERNAME \
    --object-id $CURRENT_USER_OBJECTID
```
5. Create a database
```shell
az postgres db create \
    --resource-group $AZ_RESOURCE_GROUP \
    --name $AZ_DATABASE_NAME \
    --server-name $AZ_DATABASE_SERVER_NAME \
    --output tsv
```
6. Configure firewall rule
```shell
az postgres server firewall-rule create \
    --resource-group $AZ_RESOURCE_GROUP \
    --name $AZ_DATABASE_SERVER_NAME-database-allow-local-ip-wsl \
    --server $AZ_DATABASE_SERVER_NAME \
    --start-ip-address $AZ_LOCAL_IP_ADDRESS \
    --end-ip-address $AZ_LOCAL_IP_ADDRESS \
    --output tsv
```

### Connect to Azure Spring Apps
```shell
az spring connection create postgres \
    --resource-group $AZ_RESOURCE_GROUP \
    --service <service-name> \
    --app <service-instance-name> \
    --target-resource-group $AZ_RESOURCE_GROUP \
    --server $AZ_DATABASE_SERVER_NAME \
    --database $AZ_DATABASE_NAME \
    --system-identity
```
NOTE: when use manged identity, the database username is different from az cli signed-in user.

## Run the application in JVM mode
```shell
./gradlew bootRun
```

### Validation
1. Add a todo
```shell
http --json http://localhost:8080 description='configuration' details='congratulations, you have set up JDBC correctly' done='true'
```
2. get
```shell
http http://localhost:8080 
```

## Build to native
```shell
 ./gradlew clean nativeCompile
```

```shell
./build/native/nativeCompile/azure-psql-passwordless-native
```